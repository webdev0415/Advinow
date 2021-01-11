package com.advinow.mica.services.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataframeTracker;
import com.advinow.mica.domain.queryresult.DataframeQueryResultEntity;
import com.advinow.mica.model.dataframe.CodingRules;
import com.advinow.mica.model.dataframe.DFIllness;
import com.advinow.mica.model.dataframe.DFIllnessSymptom;
import com.advinow.mica.model.dataframe.DFSymptomData;
import com.advinow.mica.model.dataframe.DFSymptomKeys;
import com.advinow.mica.model.dataframe.DFSymptomTemplate;
import com.advinow.mica.model.dataframe.DFSymptomTypes;
import com.advinow.mica.model.dataframe.DataFrameJsonModel;
import com.advinow.mica.model.dataframe.DataframeBaddata;
import com.advinow.mica.model.dataframe.SymptomSettings;
import com.advinow.mica.repositories.DataFrameRepository;
import com.advinow.mica.repositories.DataframeTrackerRepository;
import com.advinow.mica.services.DataFrameCreateService;
import com.advinow.mica.util.DF_MICAUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@Service
@Retry(name = "neo4j")
public class DataFrameCreateServiceImpl implements DataFrameCreateService {/*

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DataFrameRepository dataFrameRepository;
	
	@Autowired
	DataframeTrackerRepository dfRepository;
	
	List<DataframeBaddata> invalidList = null;

	//List<String> ignoreSymptoms = null;

	List<String> externalSymptoms = null;
	
	//List<String> timePeriodsMasterList = null;
	

	//Map<String,Long> secondsConvertionMap= null;

	 List of master illnesses (icd10Code, name) loaded from MITA database,  this should be loaded when any icd10Code new release is available	 
	Map<String, String> groupNames = null;

	 List of symptoms for given group name  
//	Map<String,Set<String>> basicSymptomGroups = null;

	List<DataframeQueryResultEntity>  masterIllnesses = null;

	// key, illnesses(icd10code,likelihood)
	// Holds symptom key, illness(icd10Code,likihood)
	Map<Integer, Map<String,Float>> illnessLikelihoodMap = null;
	
	
	Map<Integer, Map<String,SymptomSettings>> illnessLikelihoodMap = null;

	List<String> availbleTimeBuckets = null;
	
	List<DataframeQueryResultEntity> system_groups = null;



	@Value("${aws.access_key_id}")
	String awsId ;
	@Value("${aws.secret_access_key}")
	String awsKey ;
	@Value("${aws.region}")
	String region ;
	@Value("${aws.bucket_name}")
	String bucketName ;
	@Value("${dataframe.fileWrite}")
	Boolean writefile ;
	@Value("${dataframe.s3Upload}")
	Boolean uploadtoS3 ;

	 Includes all the illnesses and symptoms in the data frame for false, selected illnesses and symptoms includes for true.
	@Value("${dataframe.preConfigIllnessData}")
	Boolean preConfig ;
	@Value("${filelocation}")
	String jsonLocation;

	@Value("${dataframe.name}")
	String dataFrameName;

	@Value("${dataframe.s3.relativepath}")
	String dataFrameRelPath;

	private static final String CONTENT_TYPE = "application/json";


	@Override
	public void generateDFJson() {

		 Generate the DFIllness table with illness from all configured sources 
	//	String status = populateDFIllnesses();

	//logger.info( "DF illness status :  "+ status);

		 List of external symptoms, multipliers mapped at runtime with external services   
		externalSymptoms = getExternalSymptoms();
		//	ignoreSymptoms = getIgnoreSymptoms();
		
	
		DataframeTracker olddfTracker = dfRepository.findMostRecentDataframe();
		
		invalidList = new ArrayList<DataframeBaddata>();
				
		
		 List contains icd10Code,source,criticality..etc 
		List<DFIllness> diseases = null;
		
		 
			Map<String, List<String>> mdcMap = new Hashtable<String, List<String>>();
			Map<String, List<String>> mdcFalseMap = new Hashtable<String, List<String>>();
			Map<String, List<String>> mustMap = new Hashtable<String, List<String>>();
			Map<String, List<String>> roleOutMap = new Hashtable<String, List<String>>();

			

		 Holds symptom templates  
		List<DFSymptomTypes> symptomTemplates =null;

		 Holds symptom options with anthithesis values  
		List<DFSymptomTemplate> templateAnthithesis = null;
		 List of approved illnesses  
		List<DFSymptomData> illnessSymptomData = null;

		 Approved illnesses with symptom ID's  mapping   
		List<DFIllnessSymptom> dFIllnessSymptoms = null;

		/// CodingRules coding_rules = new CodingRules();

		 List of symptoms which will be added to DF JSON 
		List<DFSymptomKeys> dfSymptoms = new ArrayList<DFSymptomKeys>();

		 

		masterIllnesses =getMasterIllnesses();
		groupNames =getIllnessGroupNames(masterIllnesses);
		availbleTimeBuckets = getTimeBuckets();
		system_groups =dataFrameRepository.getLogicalGroups(); 
		
		if(preConfig) {
			symptomTemplates = getSymptomTemplates(true);
			templateAnthithesis = getSymptomAntithesis(true);
			illnessSymptomData = getApprovedIllneness(true);
			diseases = getApprovedDiseases(true);

			// List of symptoms included in the data frame
			List<String> dfIncludeSymptoms = symptomTemplates.stream().map(DFSymptomTypes::getCode).distinct().collect(Collectors.toList());
			// Illness data for the selected symptoms
			dFIllnessSymptoms = getIllnessDataForGivenSymptoms(true,dfIncludeSymptoms);

		} else {
			symptomTemplates = getSymptomTemplates();
			templateAnthithesis = getSymptomAntithesis();
			illnessSymptomData = getApprovedIllneness();
			diseases = getApprovedDiseases();
			dFIllnessSymptoms = getIcd10CodesWithSymptomsFromAllSources();
		}
		logger.info("Total Illnesses ::   "+ diseases.size() );

		// key, illnesses(icd10code,likelihood)
		illnessLikelihoodMap = new Hashtable<Integer, Map<String,SymptomSettings>>();

		//	logger.info("symptomsIllnessData :: "+ symptomsIllnessData.size());

		// Unique symtpoms generated from the illness data.
		List<DFSymptomKeys> dFSymptomKeys = createSymptomKeys(symptomTemplates,illnessSymptomData,templateAnthithesis,dfSymptoms);

		//	logger.info("dFSymptomKeys :: "+ dFSymptomKeys.size());

		//logger.info("SymptomGroups "+ basicSymptomGroups);

		logger.info("dFIllnessSymptoms :: "+ dFIllnessSymptoms.size());

		if(dFSymptomKeys != null && !dFSymptomKeys.isEmpty())  {
			createIllnessWithLikelihoods(dFIllnessSymptoms, illnessSymptomData, dFSymptomKeys, symptomTemplates);
		}


		//logger.info("illnessLikelihoodMap ::   "+ illnessLikelihoodMap.size() );

		DataFrameJsonModel dataFrameJsonModel = new DataFrameJsonModel();
		dataFrameJsonModel.setSymptoms(dfSymptoms);
		dataFrameJsonModel.setDiseases(diseases);

		Map<String, Map<String, Float>> likelihoods = new Hashtable<String, Map<String, Float>>();
		
	
		
		
		
		for (DFSymptomKeys symptoms : dFSymptomKeys) {
			Map<String, Float> icdCodesWithLikelihoods = illnessLikelihoodMap.get(symptoms.getKey());
			if(icdCodesWithLikelihoods != null && !icdCodesWithLikelihoods.isEmpty()) {
				likelihoods.put(symptoms.getDfKey(), icdCodesWithLikelihoods);
			}
		}
		
		
		//dataFrameJsonModel.setLikelihoods(likelihoods);
		
	//	Map<String, Map<String,IllnessLikelihood>> likelihoods = new Hashtable<String, Map<String,IllnessLikelihood>>();
		
		// Holds all negative symptoms for all the illnesses
		Map<String, Map<String,SymptomSettings>> negative_bias = new Hashtable<String, Map<String,SymptomSettings>>();
		
		for (DFSymptomKeys symptoms : dFSymptomKeys) {
			
			Map<String, SymptomSettings> icdCodesWithLikelihoods = illnessLikelihoodMap.get(symptoms.getKey());
			
		//	logger.info("DF key :: " + symptoms.getDfKey() +  " key :: " + symptoms.getKey()  +  "::: " + icdCodesWithLikelihoods);
			
			  if(icdCodesWithLikelihoods != null && !icdCodesWithLikelihoods.isEmpty()) {
					Map<String, Float>  biasTrueLikelihoods = new Hashtable<String, Float>();
					List<String>  mdcIllnesses =new ArrayList<String>();
					List<String>  mdcFalseIllnesses =new ArrayList<String>();
					List<String>  mustIllnesses =new ArrayList<String>();
					List<String>  ruleOutIllnesses =new ArrayList<String>();
				    Map<String, SymptomSettings>  biasfalseLikelihoods = new Hashtable<String, SymptomSettings>();
					 icdCodesWithLikelihoods.forEach((k,v)->{
						 
					
						 
						 
						 if( java.util.Objects.equals(v.getMdc() ,true)  &&  java.util.Objects.equals(v.getBias(), true)) 
							 {
								  mdcIllnesses.add(k);
							 }
							 
						 
						 if( java.util.Objects.equals(v.getMdc() ,true)  &&  java.util.Objects.equals(v.getBias(), false)) 
							 {
							 mdcFalseIllnesses.add(k);
							 }
						 
							 
							
							if( java.util.Objects.equals(v.getMust() ,true)) {
								mustIllnesses.add(k);
							}
						
							if( java.util.Objects.equals(v.getRuleOut() ,true)) {
								ruleOutIllnesses.add(k);
							}
						 
						if( java.util.Objects.equals(v.getBias(), true)) {
							biasTrueLikelihoods.put(k, v.getValue());
						}  else {
							 biasfalseLikelihoods.put(k, v);
						}
					});
					
					 
					 if(! biasTrueLikelihoods.isEmpty()) {
						 
						 likelihoods.put(symptoms.getDfKey(),biasTrueLikelihoods);
				      } 
					 
					 if(! biasfalseLikelihoods.isEmpty()) {
						 negative_bias.put(symptoms.getDfKey(), biasfalseLikelihoods);
					 }
					
					 if(! mdcFalseIllnesses.isEmpty()) {
						 mdcFalseMap.put(symptoms.getDfKey(), mdcFalseIllnesses);
					 }
				
					 if(! mdcIllnesses.isEmpty()) {
						 mdcMap.put(symptoms.getDfKey(), mdcIllnesses);
					 }
					 
					 
					 if(! mustIllnesses.isEmpty()) {
						 mustMap.put(symptoms.getDfKey(), mustIllnesses);
					 }
				
					 if(! ruleOutIllnesses.isEmpty()) {
						 roleOutMap.put(symptoms.getDfKey(), ruleOutIllnesses);
					 }
			  }
	
		}
		
		dataFrameJsonModel.setLikelihoods(likelihoods);
		dataFrameJsonModel.setNegative_bias(negative_bias);
		dataFrameJsonModel.setNegative_mdc(mdcFalseMap);
		dataFrameJsonModel.setPositive_mdc(mdcMap);
		dataFrameJsonModel.setRuleOut(roleOutMap);
		dataFrameJsonModel.setMust(mustMap);
		//dataFrameJsonModel.setMasterTimeBuckets(dataFrameRepository.getTimePeriodsMasterList());
	

		
	//	dataFrameJsonModel.setPriors(diseases.stream().collect(Collectors.toMap(DFIllness::getIcd10Code, DFIllness::getPrior, (oldValue, newValue) -> newValue)));
		//dataFrameJsonModel.setDcls(dFSymptomKeys.stream().filter(s->s.getLikelihood() != null).collect( Collectors.toMap(DFSymptomKeys::getDfKey, DFSymptomKeys::getLikelihood, (oldValue, newValue) -> newValue)));

		List<String> micadiseases = diseases.stream().map(DFIllness::getIcd10Code).collect(Collectors.toList());
		
		dataFrameJsonModel.setCoding_rules(getMICACodingRules(micadiseases));

		List<DFSymptomKeys> invalidKeys = dFSymptomKeys.stream().filter(s->s.getLikelihood() == null).collect(Collectors.toList());
		logger.info("==============Invalid keys ==============");
	
		for (DFSymptomKeys dfInvalidkey : invalidKeys) {
			logger.info("dfKey :: " +  dfInvalidkey.getDfKey()  + "  <==>   Likelihood :: " + dfInvalidkey.getLikelihood());
		}
		
		

		logger.info("Likelihoods ::   "+ dataFrameJsonModel.getLikelihoods().size() );
	//	logger.info("Priors ::   "+ dataFrameJsonModel.getPriors().size() );
		logger.info("Diseases ::   "+ dataFrameJsonModel.getDiseases().size() );
		//logger.info("Dcls ::   "+ dataFrameJsonModel.getDcls().size() );
		logger.info("Conditions ::  + "+ dataFrameJsonModel.getSymptoms().size() );
			
		dataFrameJsonModel.setDiseasesTracker(getIllnessChanges(olddfTracker,micadiseases));
		 
		dFIllnessSymptoms = null;
		illnessSymptomData = null;
		symptomTemplates = null;


		if(writefile) {
			createDFJson(dataFrameJsonModel);
			if (!invalidList.isEmpty()) {
			createInvalidCSV(invalidList);
			}
		}
		if(uploadtoS3) {
        String  s3Path =uploadDFJsonToS3(dataFrameJsonModel);
        updatedfTracker(micadiseases,s3Path);
        
		}

	}

 *//**
  * 
  * @param dataframeTracker
  * @param new_df_illnesses
  * @return Map<String, List<String>>
  *//*
	private Map<String, List<String>> getIllnessChanges(DataframeTracker dataframeTracker,List<String> new_df_illnesses) {
		Map<String, List<String>>  diseasesTracker =new HashMap<String, List<String>>();
		List<String> removedIllnesses = new ArrayList<String>();
		List<String> illnessesAdded = new ArrayList<String>();	
		if(dataframeTracker!= null) {  
			List<String> old_df_illnesses = dataframeTracker.getIcd10Codes();
		  illnessesAdded = new_df_illnesses.stream().filter((s) -> old_df_illnesses.stream().noneMatch(s::equals))
                .collect(Collectors.toList());
         removedIllnesses = old_df_illnesses.stream().filter((s) -> new_df_illnesses.stream().noneMatch(s::equals))
                .collect(Collectors.toList());
         diseasesTracker.put("Added", illnessesAdded);		
        } else {
			diseasesTracker.put("Added", new_df_illnesses);			
		}
		diseasesTracker.put("Removed", removedIllnesses);
		return diseasesTracker;
	}


	private void updatedfTracker(List<String> micadiseases, String s3Path) {
		DataframeTracker dfTracker = new DataframeTracker();
		dfTracker.setUrl(s3Path);
		dfTracker.setIcd10Codes(micadiseases);
		dfRepository.save(dfTracker);
	}






	private void createInvalidCSV(List<DataframeBaddata> baddata) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
			Path path = Paths.get(jsonLocation+ File.separator +  dataFrameRelPath  + File.separator +  DF_MICAUtil.getAdvinowDateFormat()+  File.separator + dataFrameName + "invalid.Json");
			Files.createDirectories(path.getParent());
			String json = gson.toJson(baddata);
			byte[] utf8JsonString = json.getBytes(StandardCharsets.UTF_8);
			Files.write(path,utf8JsonString, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}




	private List<DataframeQueryResultEntity>  getMasterIllnesses() {
		return	dataFrameRepository.getMasterIllnessInfo();

	}



	*//**
	 *
	 * Creates symptom conditions from symptom templates and illness data
	 *
	 * @param symptomTemplates
	 * @param symptomsIllnessData
	 * @param templateAnthithesis
	 * @return List<DFSymptomKeys>
	 *//*
	private List<DFSymptomKeys> createSymptomKeys(List<DFSymptomTypes> symptomTemplates, List<DFSymptomData> symptomsIllnessData, List<DFSymptomTemplate> templateAnthithesis,List<DFSymptomKeys> dfSymptoms) {

		// All possible keys for the illnesses data
		List<DFSymptomKeys> dFSymptomUniqueKeys = new ArrayList<DFSymptomKeys>();

	//	basicSymptomGroups = new Hashtable<String, Set<String>>();
		for (DFSymptomTypes symptomTemplate : symptomTemplates) {
			if(symptomTemplate.getSymptomType() != null ) {
				List<DFSymptomData> symptomdDataByID= symptomsIllnessData.parallelStream().filter(s->s.getSymptomID().equals(symptomTemplate.getCode())).collect(Collectors.toList());
				if(symptomdDataByID != null &&  !symptomdDataByID.isEmpty() ) {
					// used in the Json to display all the conditions for symptoms
					DFSymptomKeys dfSymptom  = creatgeDFSymptomKey(symptomTemplate);

					List<String> rosGroups =getSystemGroups(symptomTemplate.getCode(),"ROS");
					if (rosGroups != null && ! rosGroups.isEmpty()) {
						dfSymptom.setRos_system_groups(rosGroups);
					}
					List<String> deGroups = getSystemGroups(symptomTemplate.getCode(),"DE");
					if (deGroups != null && ! deGroups.isEmpty()) {
						dfSymptom.setDe_system_groups(deGroups);
					}

					Map<Integer, DFSymptomData> 	symptomDataMap = getAllIllnessSymptoms(symptomdDataByID);

				//	List<String> locations = symptomdDataByID.stream().flatMap(s->s.getBodyParts().stream().sorted()).filter(Objects::nonNull).distinct().collect(Collectors.toList());	
					List<String> locations = symptomdDataByID.stream().flatMap(s->s.getBodyParts().stream()).filter(Objects::nonNull).distinct().collect(Collectors.toList());					

					
					List<String> multipliers = symptomdDataByID.stream().flatMap(s->s.getMultiplier().stream()).filter(Objects::nonNull).distinct().collect(Collectors.toList());				
					List<String> timeFrames = symptomdDataByID.stream().map(DFSymptomData::getTimeFrame).filter(Objects::nonNull).distinct().collect(Collectors.toList());

					for (Map.Entry<Integer, DFSymptomData> entry : symptomDataMap.entrySet()) {
						Integer k = entry.getKey();
						DFSymptomData v =  entry.getValue();
						DFSymptomKeys symptomKey = creatgeDFSymptomKey(symptomTemplate);					
						symptomKey.setKey(k);
						Boolean likelihood   =  validateLikelihood(v,symptomTemplate) ; 
						Boolean localtionFlag = valdateBodyParts(symptomTemplate,v,symptomKey,locations);
					//	Boolean categoryIdFlag = validateCategory(symptomTemplate,v);

						if (symptomTemplate.getSymptomType().equalsIgnoreCase(DF_MICAUtil.SIMPLE)) {
						//	Float anthisis = getSimpleDCLs(symptomTemplate.getCode(),templateAnthithesis);
							Boolean simpleSymptom  = validateSimpleSymptoms(symptomKey,multipliers,timeFrames);
							if(anthisis != null) {
								symptomKey.setLikelihood(anthisis);
							}

							if(!localtionFlag  && !simpleSymptom && ! likelihood) {
							//	updateBasicSymptoms(symptomKey);
								createDfKey(symptomKey);
								dFSymptomUniqueKeys.add(symptomKey);
							}
						}  else  if(symptomTemplate.getSymptomType().equalsIgnoreCase(DF_MICAUtil.LIST)) {
							Boolean multiplierFlag= validateListSymptoms(symptomTemplate,templateAnthithesis,v,symptomKey, multipliers,timeFrames );
							if(!multiplierFlag &&  ! localtionFlag && ! likelihood) {
							//	updateBasicSymptoms(symptomKey);
								updateRangeORMultiplier(v,symptomKey);
								createDfKey(symptomKey);
								dFSymptomUniqueKeys.add(symptomKey);

							}


							if(multipliers != null && ! multipliers.isEmpty()) {
								dfSymptom.setDescriptors(multipliers);
							}    


						} 	 
						else if(symptomTemplate.getSymptomType().equalsIgnoreCase(DF_MICAUtil.SIMPLE_TIME)){
							//Float anthisis = getSimpleDCLs(symptomTemplate.getCode(),templateAnthithesis);
							Boolean timePeriodFlag = validateTimeFrames(symptomTemplate,v,symptomKey,timeFrames);
							Boolean simpleTimeFlag =	validateSimpleTimeSymptoms(symptomKey, multipliers);
							if(anthisis != null) {
								symptomKey.setLikelihood(anthisis);
							}

							if(!localtionFlag && !timePeriodFlag  && !simpleTimeFlag && ! likelihood) {
								createDfKey(symptomKey);															
							//	updateBasicSymptoms(symptomKey);
								dFSymptomUniqueKeys.add(symptomKey);	
							}




							if(timeFrames != null && ! timeFrames.isEmpty()) {
								dfSymptom.setTimeBuckets(timeFrames);
							}    

						}	
						else if(symptomTemplate.getSymptomType().equalsIgnoreCase(DF_MICAUtil.LIST_TIME)) {
							Boolean multiplierFlag=  validateListTimeMultipliers(symptomTemplate,templateAnthithesis,v,symptomKey,multipliers);
							Boolean timePeriodFlag = validateTimeFrames(symptomTemplate,v,symptomKey,timeFrames);
							if( !multiplierFlag  && !localtionFlag  && !timePeriodFlag && ! likelihood) {
							//	updateBasicSymptoms(symptomKey);
								updateRangeORMultiplier(v,symptomKey);
								createDfKey(symptomKey);
								dFSymptomUniqueKeys.add(symptomKey);

							}


							if(multipliers != null && ! multipliers.isEmpty()) {
								dfSymptom.setDescriptors(multipliers);
							}    

							if(timeFrames != null && ! timeFrames.isEmpty()) {
								dfSymptom.setTimeBuckets(timeFrames);
							}    

						} else{
							logger.info("Error in the templates");
							logger.info("Symptom Code :: "+ symptomTemplate.getCode() );
							logger.info("Symptom Type :: "+ symptomTemplate.getSymptomType() );
							
							DataframeBaddata invalidData = new DataframeBaddata();
							invalidData.setCode(symptomTemplate.getCode());						
							invalidData.setType(symptomTemplate.getSymptomType() );
							invalidData.setIcd10Code(v.getIcd10Code());
							invalidData.setError("Error in the templates");
							invalidList.add(invalidData);
						}

						if(locations != null && ! locations.isEmpty()) {
							dfSymptom.setLocations(locations);
						} 
						
						

						

					}
					dfSymptoms.add(dfSymptom);
				}
			}  else {
				logger.info("Symptom Type is required ... " + symptomTemplate.getCode());
				DataframeBaddata invalidData = new DataframeBaddata();
				invalidData.setCode(symptomTemplate.getCode());						
				invalidData.setType(symptomTemplate.getSymptomType() );		
				invalidData.setError("Symptom Type is required");
				invalidList.add(invalidData);
			}

		}
		return dFSymptomUniqueKeys;
	}







*//**
 * Creates DF symptom key for given symptom template.
 * 
 * @param symptomTemplate
 * @return
 *//*
	private DFSymptomKeys creatgeDFSymptomKey(DFSymptomTypes symptomTemplate) {
		DFSymptomKeys dfSymptom = new DFSymptomKeys();
		dfSymptom.setSymptomID(symptomTemplate.getCode());
		dfSymptom.setGroupName(getSubGroupName(symptomTemplate));
		dfSymptom.setTierName(getTierName(symptomTemplate));
		dfSymptom.setBasic( symptomTemplate.getBasicSymptom());
		dfSymptom.setSymptomName(symptomTemplate.getName());
		dfSymptom.setChiefComplaint(symptomTemplate.getDisplaySymptom());
		return dfSymptom;

	}

	private List<String> getSystemGroups(String symptomID,String systemGrooupName) {
		DataframeQueryResultEntity deQueryResults = system_groups.parallelStream().filter(s->s.getSymptomID().equals(symptomID) && s.getType().equalsIgnoreCase(systemGrooupName)).findAny().orElse(null);
		if(deQueryResults != null && deQueryResults.getLogicalGroupNames() != null &&  !deQueryResults.getLogicalGroupNames().isEmpty() ) {
		return deQueryResults.getLogicalGroupNames();
		}
		return null;
	}

	private Boolean validateLikelihood(DFSymptomData v,DFSymptomTypes symptomTemplate) {
		if(v.getLikelihood() == null) {
			logger.info(" Likelihood is null for " +   v.getSymptomID()  + "     " );
		 	DataframeBaddata invalidData = new DataframeBaddata();
			invalidData.setCode(v.getSymptomID() );						
			invalidData.setType(symptomTemplate.getSymptomType() );
			invalidData.setIcd10Code(v.getIcd10Code());
			invalidData.setError("Likelihood is null");
			invalidList.add(invalidData);
			
			return true;
		}
		return false;
	}


	*//**
	 * Builds the list of basic symptoms for given group.
	 *
	 * @param dFSymptomKey
	 *//*
	private void updateBasicSymptoms(DFSymptomKeys dFSymptomKey) {
		String groupName = dFSymptomKey.getGroupName();
		if(groupName != null ) {
			Set<String> basicSymptoms = basicSymptomGroups.get(groupName);
			if(basicSymptoms == null) {
				basicSymptoms = new HashSet<String>();
			}
			basicSymptoms.add(dFSymptomKey.getSymptomID());
			basicSymptomGroups.put(groupName, basicSymptoms);
		}

	}


	private String getTierName(DFSymptomTypes symptomTemplate) {
		String symptomGroupName = symptomTemplate.getGroupName();
		if((symptomGroupName.equalsIgnoreCase("pain") || symptomGroupName.equalsIgnoreCase("swelling"))   && symptomTemplate.getPartOfGroup()) {
			List<String> subGroups = symptomTemplate.getSubGroups();
			if(subGroups != null && ! subGroups.isEmpty()) {
				return subGroups.get(2);
			}
		}
		return null;
	}




	private void createIllnessWithLikelihoods(List<DFIllnessSymptom> dFIllnessSymptoms,List<DFSymptomData> symptomsIllnessData,List<DFSymptomKeys> dFSymptomKeys,List<DFSymptomTypes> symptomTemplates) {

		for ( DFIllnessSymptom   llnessSymptom : dFIllnessSymptoms) {
		
			String dbIcd10Code = llnessSymptom.getIcd10Code();

		// Symptom data  for given illness
			List<DFSymptomData> illnessData= symptomsIllnessData.parallelStream().filter(s->s.getIcd10Code().equals(dbIcd10Code)).collect(Collectors.toList());
			// Symptom for the given illness
			List<String> illnessSymptomIDs = llnessSymptom.getSymptoms();
			//	logger.info("Symptom Id :: " + illnessSymptomIDs);
			for (String symptomID : illnessSymptomIDs) {
				DFSymptomTypes dbSymptomTemplateType = symptomTemplates.parallelStream().filter(s->s.getCode().equals(symptomID)).findAny().orElse(null);
				if(dbSymptomTemplateType != null) {
					// raw symptom data for given symptom id includes all the multiplier values
					List<DFSymptomData> rawSymptoms = illnessData.parallelStream().filter(s->s.getSymptomID().equals(symptomID)).collect(Collectors.toList());
										
					if(rawSymptoms != null  && ! rawSymptoms.isEmpty() && rawSymptoms.size() > 0) {
					
						List<DFSymptomKeys> finaldFSymptomKeys = dFSymptomKeys.stream().filter(s->s.getSymptomID().equals(symptomID)).collect(Collectors.toList());
						
						createLikelihoodsFromIllnessData(dbIcd10Code,finaldFSymptomKeys,rawSymptoms,dbSymptomTemplateType);
					}

				} else {
					logger.info(symptomID + "is s not mapped to any illness and not includeded in DF ");					
					DataframeBaddata invalidData = new DataframeBaddata();
					invalidData.setCode(symptomID );
					invalidData.setError("Symptom is not mapped to any illness or partially mapped.");
					invalidList.add(invalidData);
				}
			}


		}

	}


	private void createLikelihoodsFromIllnessData(String icd10Code,
			List<DFSymptomKeys> dFSymptomKeys, List<DFSymptomData> rawSymptoms, DFSymptomTypes dbSymptom) {
		if(dbSymptom.getSymptomType() != null ) {
			//Map<String, Float> basicSymtompMaxLikelihoods = getMaximumLikelihood(rawSymptoms,dbSymptom.getCode(),dFSymptomKeys,icd10Code);
			Map<String, Float> basicSymtompMaxLikelihoods = null;
			if (dbSymptom.getSymptomType().equalsIgnoreCase(DF_MICAUtil.SIMPLE) || dbSymptom.getSymptomType().equalsIgnoreCase(DF_MICAUtil.LIST) ) {
				createLikelihoodsForSimpleAndList(icd10Code,dFSymptomKeys,rawSymptoms,basicSymtompMaxLikelihoods);
			} 
			if(dbSymptom.getSymptomType().equalsIgnoreCase(DF_MICAUtil.SIMPLE_TIME)) {
				processListTimeSymptoms(icd10Code,dFSymptomKeys,rawSymptoms,false);
			} 
			if(dbSymptom.getSymptomType().equalsIgnoreCase(DF_MICAUtil.LIST_TIME)) {
				processListTimeSymptoms(icd10Code,dFSymptomKeys,rawSymptoms,true);

			}
		}
	}

	private void updateRangeORMultiplier(DFSymptomData v, DFSymptomKeys symptomKey) {

		if(v.getMultiplier() != null &&  !v.getMultiplier().isEmpty() && v.getMultiplier().size() >  0) {
			Integer length = v.getMultiplier().size();
			if(length==1){
				if(v.getMultiplier().get(0) != "") {
					symptomKey.setDescriptor(v.getMultiplier().get(0));
				}
			} else{
				String Str = v.getMultiplier().get(length-1);
				if(Str != ""  ) {
					symptomKey.setDescriptor(Str);
				} else{
					String Str1 =	v.getMultiplier().get(length-2);
					if(Str1 != ""  ) {
						symptomKey.setDescriptor(Str1);
					} else {
						symptomKey.setDescriptor(v.getMultiplier().get(length-3));
					}
				}
			} 
		}  else{
			logger.info(" Multiplier is null  for :: " + v.getSymptomID());
			DataframeBaddata invalidData = new DataframeBaddata();
			invalidData.setCode(v.getSymptomID());
		//	invalidData.setInvalidMultiplier(selecltedListValue);
		//	invalidData.setType(symptomTypes.getSymptomType());
			invalidData.setIcd10Code(v.getIcd10Code());
			invalidData.setError("Multiplier is null");
			invalidList.add(invalidData);
		}




	}
	
	
	private String getSubGroupName(DFSymptomTypes symptomTemplate) {
		String symptomGroupName = symptomTemplate.getGroupName();
		if(symptomGroupName.equalsIgnoreCase("Physical")  && symptomTemplate.getPartOfGroup()) {
			List<String> subGroups = symptomTemplate.getSubGroups();
			if(subGroups != null && ! subGroups.isEmpty()) {
				return subGroups.get(2);
			}
		} else if((symptomGroupName.equalsIgnoreCase("pain") || symptomGroupName.equalsIgnoreCase("swelling"))   && symptomTemplate.getPartOfGroup()) {
			return symptomTemplate.getCategoryID();
		}

		return null;
	}
	
	
	
	private String getSubGroupName(DFSymptomTypes symptomTemplate) {
		String groupName = symptomTemplate.getCode() + "_" + symptomTemplate.getCategoryID();
		//List<String> subGroups = symptomTemplate.getSubGroups();

		String symptomGroupName = symptomTemplate.getSymptomGroup();

		if(symptomGroupName != null) {
			return symptomGroupName + "_" +symptomTemplate.getCategoryID();
		} else {
			return groupName;
		}


	}


	
	
	private String getSubGroupName(DFSymptomTypes symptomTemplate) {
		String symptomID = symptomTemplate.getCode(); 
		//List<String> subGroups = symptomTemplate.getSubGroups();
		String symptomGroupName = symptomTemplate.getSymptomGroup();
		if(symptomGroupName != null) {
			return symptomGroupName;
		} else {
			return symptomID;
		}


	}

	
	
	
	private Boolean validateListSymptoms(DFSymptomTypes symptomTypes,
			List<DFSymptomTemplate> templates, DFSymptomData v,DFSymptomKeys symptomKey, List<String> usedMultipliers, List<String> timeFrames ) {
		Boolean ignore = false;
		//Float antithesis = 0.01f;
		DFSymptomTemplate template = null;
		String code = symptomTypes.getCode();
		List<String> selecltedListValue = v.getMultiplier();
	
		if(timeFrames != null  && ! timeFrames.isEmpty() && timeFrames.size() > 0) {
			logger.info(" ListType  ::   " +   code  + "  with time periods  " + timeFrames   +  " is  list  symptom (Invalid symptom)  and not includeded in DF ::   "+timeFrames.size());
			ignore  = true;
			
		}
		
		if(selecltedListValue != null &&  !selecltedListValue.isEmpty()  && selecltedListValue.size() > 0) {
			template = templates.parallelStream().filter(s->s.getSymptomID().equals(code) && s.getDsName() != null && selecltedListValue.contains(s.getDsName())).findAny().orElse(null);
			if(template != null ) {
			//	antithesis = template.getDsAntithesis();
			} else{
				if(selecltedListValue.isEmpty() ) {
					logger.info(" SymptomID  ::   " +   code  + ":::: "   +   selecltedListValue+ "  is invalid symptom for list time symptom   and not includeded in DF ::  "+selecltedListValue.size());
					ignore = true;
				}
				if(externalSymptoms.contains(code)) {
				//	logger.info(code  + ":::: "   +   selecltedListValue+ " :: External symptoms,  set  antithesis set to 0.01");
					ignore = false;
				}  else{
					ignore  = true;
					logger.info(" ListType  ::   " +   code  + ":::: "   +   selecltedListValue+ "  is  invalid symptom   and not includeded in DF ::  "+selecltedListValue.size());
					usedMultipliers.removeIf(x -> selecltedListValue.contains(x)); 
				}
			}
		} else{
			ignore  = true;
			logger.info("ListType  ::   " +   code  + ":::: "   +   selecltedListValue+ "    is  invalid symptom   and not includeded in DF :: "+ selecltedListValue.size());
		}
	//	symptomKey.setLikelihood(antithesis);
		
		if(ignore) {
			DataframeBaddata invalidData = new DataframeBaddata();
			invalidData.setCode(code);
			invalidData.setInvalidmultiplier(selecltedListValue);
			invalidData.setMultiplier(selecltedListValue);
			invalidData.setType(symptomTypes.getSymptomType());
			invalidData.setIcd10Code(v.getIcd10Code());
			invalidData.setError("Invalid multiplier");
			invalidList.add(invalidData);
		}
		
		
		return ignore;
	}

	
	
	
	private Boolean validateListTimeMultipliers(DFSymptomTypes symptomTypes,
			List<DFSymptomTemplate> templates, DFSymptomData v,DFSymptomKeys symptomKey, List<String> usedMultipliers ) {
		Boolean ignore = false;
		//Float antithesis = 0.01f;
		DFSymptomTemplate template = null;
		String code = symptomTypes.getCode();
		List<String> selecltedListValue = v.getMultiplier();

		
		if(selecltedListValue != null &&  !selecltedListValue.isEmpty()  && selecltedListValue.size() > 0) {
			template = templates.parallelStream().filter(s->s.getSymptomID().equals(code) && s.getDsName() != null && selecltedListValue.contains(s.getDsName())).findAny().orElse(null);
			if(template != null ) {
			//	antithesis = template.getDsAntithesis();
			} else{
				if(selecltedListValue.isEmpty() ) {
					logger.info(" SymptomID  ::   " +   code  + ":::: "   +   selecltedListValue+ "  is invalid symptom for list time symptom   and not includeded in DF ::  "+selecltedListValue.size());
					ignore = true;
					
				}
				if(externalSymptoms.contains(code)) {
				//	logger.info(code  + ":::: "   +   selecltedListValue+ " :: External symptoms,  set antithesis set to 0.01");
					ignore = false;
				}  else{
					ignore  = true;
					logger.info(" SymptomID  ::   " +   code  + ":::: "   +   selecltedListValue+ "  is invalid symptom for list time symptom   and not includeded in DF ::  "+selecltedListValue.size());
					usedMultipliers.removeIf(x -> selecltedListValue.contains(x)); 
				}
			}
		} else{
			ignore  = true;
			logger.info("SymptomID  ::   " +   code  + ":::: "   +   selecltedListValue+ "    is  invalid symptom for list time symptoms  and not includeded in DF :: "+ selecltedListValue.size());
		}
		
		if(ignore) {
			DataframeBaddata invalidData = new DataframeBaddata();
			invalidData.setCode(code);
			invalidData.setInvalidmultiplier(selecltedListValue);
			invalidData.setMultiplier(selecltedListValue);
			invalidData.setType(symptomTypes.getSymptomType());
			invalidData.setIcd10Code(v.getIcd10Code());
			invalidData.setError("Invalid multiplier");
			invalidList.add(invalidData);
		}
		
		
	//	symptomKey.setLikelihood(antithesis);
		return ignore;
	}

	

	private Boolean validateTimeFrames(DFSymptomTypes symptomTemplate,DFSymptomData v,	DFSymptomKeys symptomKey, List<String> timeFrames) {
		Boolean flag = false;
		if(availbleTimeBuckets != null  && ! availbleTimeBuckets.isEmpty() && availbleTimeBuckets.size() > 0) {
			if(timeFrames.isEmpty()) {
				logger.info("No time symptoms found for    :::::::::::.     "+ v.getSymptomID());
				flag = true;
			}
			
			if(v.getTimeFrame() != null  && timeFrames != null ) {
				if(availbleTimeBuckets.stream().anyMatch(v.getTimeFrame()::contains)) {	
					symptomKey.setTimeFrame(v.getTimeFrame());
					flag = false;
				} else {
					logger.info("Invalid Time periods  for the symptom    :::::::::::.     "+ v.getSymptomID() + "  " + v.getTimeFrame());
					flag = true;
					timeFrames.removeIf(x -> v.getTimeFrame().contains(x)); 
				}
				
				List<String> trimeFramesList = Stream.of(v.getTimeFrame().split(",")).map(String::trim)
						  .collect(Collectors.toList());
			//	logger.info("icd10Code : "  + v.getIcd10Code() + "SymptomID" + v.getSymptomID() + "Values : "+  trimeFramesList + "Size " + trimeFramesList.size());
				
				if(trimeFramesList.size() > 1) {
					flag = true;
					logger.info("Invalid Time periods  for the symptom    :::::::::::.     "+ v.getSymptomID() + "  " + v.getTimeFrame());
				}
				

			} else {				
				logger.info(v.icd10Code + "Time periods requried for the symptom    :::::::::::.     "+ v.getSymptomID());

				flag = true;
			}
			

		
			if(flag) {
				DataframeBaddata invalidData = new DataframeBaddata();
				invalidData.setCode(v.getSymptomID() );			
				invalidData.setIcd10Code(v.getIcd10Code());
				invalidData.setTimeFrame(v.getTimeFrame());
				invalidData.setType(symptomTemplate.getSymptomType());
				invalidData.setError("Time periods requried /Invalid");
				invalidData.setDsId(v.getDsId());
				invalidList.add(invalidData);
			}

		}
		return flag;
	}




	private Boolean valdateBodyParts(DFSymptomTypes symptomTemplate,
			DFSymptomData v, DFSymptomKeys symptomKey, List<String> locations) {
		Boolean flag = false;
		List<String> templateBodyLocations = symptomTemplate.getBodyLocations();
		if(templateBodyLocations != null  && ! templateBodyLocations.isEmpty()) {
			if(v.getBodyParts() != null && ! v.getBodyParts().isEmpty() ) {				
				if(v.getBodyParts().size() <=  templateBodyLocations.size()) {
					if(v.getBodyParts().stream().anyMatch(templateBodyLocations::contains)) {
						symptomKey.setSelectedBodyParts(v.getBodyParts());
					flag = false;
				} else {
					logger.info( templateBodyLocations +  " Invalid Body locations  for the symptom    :::::::::::.     "+ symptomTemplate.getCode() + " selected  " + v.getBodyParts());
					flag = true;
					locations.removeIf(x -> v.getBodyParts().contains(x)); 
				}
				
			}  else {
				logger.info( templateBodyLocations +  " Invalid Body locations  for the symptom    :::::::::::.     "+ symptomTemplate.getCode() + " selected  " + v.getBodyParts());
					flag = true;
		
			}
			} else {				
				logger.info("Body locations requried for the symptom    :::::::::::.     "+ symptomTemplate.getCode());
				flag = true;
			}

		} else { 
			//  there should not any body part selected for the illness for no bodyparts symptoms
			if(v.getBodyParts() != null && ! v.getBodyParts().isEmpty() ) {
				logger.info("Invalid Body locations  for the symptom    :::::::::::.     "+ symptomTemplate.getCode() + "  " + v.getBodyParts());
				flag = true;
				locations.removeIf(x -> v.getBodyParts().contains(x)); 
			}
			
		}
		
		if(flag) {
			DataframeBaddata invalidData = new DataframeBaddata();
			invalidData.setCode(v.getSymptomID() );			
			invalidData.setIcd10Code(v.getIcd10Code());
			invalidData.setError("Body locations requried /Invalid");
			invalidList.add(invalidData);
		}
		return flag;
	}


	private Boolean validateSimpleSymptoms(DFSymptomKeys symptomKey,List<String> multipliers,
			List<String> timeFrames) {
		 Boolean ignore = false;
		 

		 
		if(timeFrames != null  && ! timeFrames.isEmpty() && timeFrames.size() >0) {
			logger.info(" SimpleType   ::   " +   symptomKey.getSymptomID()  + "  with time periods  " + timeFrames  + " are not valid   and not includeded in DF :: "+timeFrames.size());
			
			ignore  = true;
			
		}
		
		if(multipliers != null  && ! multipliers.isEmpty() && multipliers.size() > 0) {
			logger.info(" SimpleType  ::   " +   symptomKey.getSymptomID()  + " with multipliers ::  " +  multipliers  + "are not valid   and not includeded in DF :: ::  "+multipliers.size());
			ignore  = true;
			
		}

		return ignore;
	}

	

	private Boolean validateSimpleTimeSymptoms(DFSymptomKeys symptomKey,List<String> multipliers) {
		 Boolean ignore = false;
		
		if(multipliers != null  && ! multipliers.isEmpty() && multipliers.size() >0) {
			logger.info(" Simple Time  ::   " +   symptomKey.getSymptomID()  + "  with multipliers  ::  " +  multipliers  + " are not valid   and not includeded in DF ::  "+multipliers.size());
			ignore  = true;
		}

		return ignore;
	}
	
	


private Boolean validateCategory(DFSymptomTypes symptomTemplate,
			DFSymptomData v) {
	Boolean flag = false;
	
	if (! symptomTemplate.getCategoryID().equalsIgnoreCase(v.getCategoryID())) {
		flag = true;
		logger.info("Invalid categories  for the symptom    :::::::::::.     "+ symptomTemplate.getCode() + "     " + v.getCategoryID());
	}
	return flag;
}


	private void processListTimeSymptoms(String icd10Code,
			List<DFSymptomKeys> dFSymptomKeys, List<DFSymptomData> rawSymptoms,boolean listTime) {
		//	updateTimePeriods(rawSymptoms);
		if(listTime) {
			Stream<DFSymptomData> symptomMultipliers = rawSymptoms.stream().filter(s->s.getMultiplier() !=null && !s.getMultiplier().isEmpty());
			if(symptomMultipliers != null ) {
				Map<List<String>, List<DFSymptomData>> groupByNoBodyPartWithMultiplier =
						symptomMultipliers.collect(Collectors.groupingBy(DFSymptomData::getMultiplier));
				groupByNoBodyPartWithMultiplier.forEach((k,illnessData)->{
					processTimeSymptoms(icd10Code,dFSymptomKeys,illnessData);
				});
			}
		} else {
			processTimeSymptoms(icd10Code,dFSymptomKeys,rawSymptoms);

		}

	}







	private void processTimeSymptoms(String icd10Code,
			List<DFSymptomKeys> dFSymptomKeys, List<DFSymptomData> rawSymptoms) {
		if(rawSymptoms != null) {
			for (Iterator<DFSymptomData> iterator = rawSymptoms.iterator(); iterator.hasNext();) {
				DFSymptomData dfSymptomData = (DFSymptomData) iterator.next();
				updateSimpleSymptomKeyLikelihoods(dfSymptomData,  dFSymptomKeys, icd10Code,null);

			}

		}
	}




	private Map<String,Float> getMaximumLikelihood(List<DFSymptomData> rawSymptoms,
			String symptomID, List<DFSymptomKeys> dFSymptomKeys, String icd10Code) {
		// get symptom from keys
		DFSymptomKeys symptom = dFSymptomKeys.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomID)).findAny().orElse(null);

		Map<String,Float> basicMaxLikelihood = new Hashtable<String, Float>();

		if(symptom == null ){
			logger.info(symptomID +  "::: No df keys found, Symptom is partially mapped for ::  "+ icd10Code );
		}
		if(symptom != null && symptom.getBasic()) {
			Set<String> basicGroupSymptoms = basicSymptomGroups.get(symptom.getGroupName());
			Optional<DFSymptomData>  maxLikelihoodSymptom = rawSymptoms.stream().filter(e -> basicGroupSymptoms.contains(e.getSymptomID()) && e.getLikelihood() != null).collect(Collectors.maxBy(Comparator.comparing(DFSymptomData::getLikelihood)));
			if(maxLikelihoodSymptom.isPresent()){
				DFSymptomData dFSymptomData =maxLikelihoodSymptom.get();
				if(dFSymptomData != null &&  dFSymptomData.getLikelihood() != null ) {
					basicMaxLikelihood.put(dFSymptomData.getSymptomID(), dFSymptomData.getLikelihood());
				} else{
					logger.info(dFSymptomData.getSymptomID() + "   ::  " + dFSymptomData.getLikelihood() + " mapped with Likelihood  null  for illness :  " + icd10Code );
				}

			}

		}
		return basicMaxLikelihood;
	}


	private void createLikelihoodsForSimpleAndList(String icd10Code,
			List<DFSymptomKeys> dFSymptomKeys, List<DFSymptomData> rawSymptoms, Map<String, Float>  basicSymptoms) {
		for (DFSymptomData dFSymptomData : rawSymptoms) {
			updateSimpleSymptomKeyLikelihoods(dFSymptomData,  dFSymptomKeys, icd10Code,basicSymptoms);
		}
	}



	private void	updateSimpleSymptomKeyLikelihoods(DFSymptomData dFSymptomData,List<DFSymptomKeys> dFSymptomKeys, String icd10Code, Map<String, Float> basicSymptoms) {
	
		//Integer hasCode = Objects.hash(dFSymptomData.getSymptomID(),dFSymptomData.getBodyParts().stream().sorted().collect(Collectors.toList()),dFSymptomData.getMultiplier(), dFSymptomData.getTimeFrame());
		
		Integer hasCode =	 Objects.hash(dFSymptomData.getSymptomID(),
				dFSymptomData.getBodyParts().stream().sorted().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList()),
				dFSymptomData.getMultiplier().stream().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList()), 
				dFSymptomData.getTimeFrame());
		
		
		Integer hasCode = getUniqueString(dFSymptomData) ;
		
		if(dFSymptomData.getSymptomID().equals("SYMPT0003412") && icd10Code.equals("H40.011") ) {
			logger.info("HashCode   "+ hasCode);
			DFSymptomKeys symptomKey =dFSymptomKeys.parallelStream().filter(s ->s.getKey().intValue()==hasCode.intValue()).findAny().orElse(null);
			logger.info("symptom keys ::::::::::::  "+ symptomKey);
		}
		
	//	if(hasCode != null ) {
			DFSymptomKeys symptomKey =dFSymptomKeys.parallelStream().filter(s ->s.getKey().intValue()==hasCode.intValue()).findAny().orElse(null);
		
			if(icd10Code.equals("H40.011") ) {
				logger.info("symptom keys ::::::::::::  "+ symptomKey);
				logger.info("HashCode   "+ hasCode);
				logger.info("HashCode1   "+ hasCode1);
			
			}
		
			if(symptomKey != null  ){
				Float likelihood = dFSymptomData.getLikelihood();
				if(icd10Code != null && likelihood != null) {
				// adjust basic symptom likelihood
				if(symptomKey.getBasic()) {
					if(basicSymptoms != null && basicSymptoms.size() > 0) {
						Float basicSymptomLiklihood = basicSymptoms.get(symptomKey.getSymptomID());
						likelihood =basicSymptomLiklihood;
					}

				}
			      SymptomSettings additionalInfo = new SymptomSettings();
				  additionalInfo.setValue(likelihood);
				  additionalInfo.setRelated_icd10(dFSymptomData.getRelatedIc10Codes());
				  additionalInfo.setBias(dFSymptomData.getBias());  
				  additionalInfo.setMdc(dFSymptomData.getMdc());
				  additionalInfo.setMust(dFSymptomData.getMust());
				  additionalInfo.setRuleOut(dFSymptomData.getRuleOut());
				
				updateIllnessLikelihoodMatrix(icd10Code,symptomKey, additionalInfo);
				}
				
			} else{
				logger.info(" Symptom attributes :: "+ icd10Code + " :: "   +dFSymptomData.getSymptomID()+ " ::: " +dFSymptomData.getBodyParts()+ " ::: " +dFSymptomData.getMultiplier()+ " ::: " + dFSymptomData.getTimeFrame()+ " ::: " + dFSymptomData.getLikelihood() );

			}
			
		} else{
			logger.info("Key not found  :: "+ hasCode + "for  " + icd10Code);
		}



	}




	private void updateIllnessLikelihoodMatrix(String icd10Code,DFSymptomKeys symptomKey, SymptomSettings additionalInfo ) {
			  Map<String, SymptomSettings> likelihoodMap = illnessLikelihoodMap.get(symptomKey.getKey());
			  if(likelihoodMap == null ) {
				  likelihoodMap = new Hashtable<String, SymptomSettings>();
				  likelihoodMap.put(icd10Code, additionalInfo); 				  
		 	    } else{
			  	  likelihoodMap.put(icd10Code, additionalInfo);
			    }
			  illnessLikelihoodMap.put(symptomKey.getKey(), likelihoodMap);
	}


	*//**
	 * Returns list of list time symptoms
	 *
	 * @param listTime
	 * @return
	 *//*

	private Map<Integer, DFSymptomData>  getAllIllnessSymptoms(
			List<DFSymptomData> listTime) {
		

		Map<Integer, DFSymptomData> listTimeSymptoms = new HashMap<Integer, DFSymptomData>();
		
		
		for (DFSymptomData dfSymptomData : listTime) {
			listTimeSymptoms.put(getUniqueString(dfSymptomData),				dfSymptomData);
		}
		
		
	
		
		
		
		
		//String timePeriod = null;
		Map<Integer, DFSymptomData> listTimeSymptoms = listTime.parallelStream()
				.collect(HashMap::new, (m, v) -> m.put(
						Objects.hash(v.getSymptomID(),v.getBodyParts().stream().sorted().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList()),v.getMultiplier().stream().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList()), v.getTimeFrame()), v),
						HashMap::putAll);

		
	//	dFSymptomData.getSymptomID(),dFSymptomData.getBodyParts().stream().sorted().collect(Collectors.toList()),dFSymptomData.getMultiplier(), dFSymptomData.getTimeFrame()
				
		return listTimeSymptoms;
	}

	



	private Float getSimpleDCLs(String code,
			List<DFSymptomTemplate> templateAnthithesis) {
		Float antithesis = 0.01f;
		DFSymptomTemplate template = templateAnthithesis.parallelStream().filter(s->s.getSymptomID().equals(code)).findFirst().orElse(null);
		if(template != null ) {
			antithesis = template.getSymptomAntithesis();
		} else{
			logger.info("Bad symptom :::::::::::::::::; "+ code);

		}
		return antithesis;

	}

	private void createDfKey(DFSymptomKeys symptomKey) {
		StringBuffer dfKey = new StringBuffer();
		dfKey.append("[");
		dfKey.append(symptomKey.getSymptomID());
		dfKey.append("]");
		dfKey.append("[");
		dfKey.append(symptomKey.getDescriptor());
		dfKey.append("]");
		dfKey.append("[");
		dfKey.append(symptomKey.getTimeFrame());
		dfKey.append("]");
		if(symptomKey.getSelectedBodyParts() !=  null  && ! symptomKey.getSelectedBodyParts().isEmpty()   ) {
			dfKey.append("[");
			dfKey.append(DF_MICAUtil.getCommaSeparatedString(symptomKey.getSelectedBodyParts()));
			dfKey.append("]");
			
		} else {
			dfKey.append("[");
			dfKey.append(symptomKey.getSelectedBodyParts());
			dfKey.append("]");
		}
		symptomKey.setDfKey(dfKey.toString());
	}

	private List<String> getExternalSymptoms() {
		List<String> listSymptoms = new ArrayList<String>();
		DataframeQueryResultEntity results = dataFrameRepository.getExternalSymptoms();
		Set<String> symptoms = results.getSymptoms();
		if(symptoms != null && !symptoms.isEmpty()) {
			listSymptoms = symptoms.stream().collect(Collectors.toList());
		}

		return listSymptoms;
	}

	private String populateDFIllnesses() {
		return dataFrameRepository.createDFIllnessesFromSources();
	}


	private List<DFSymptomTypes> getSymptomTemplates() {
		return dataFrameRepository.getSymptomTypes();
	}

	private List<DFSymptomTypes> getSymptomTemplates(Boolean active) {
		return dataFrameRepository.getSymptomTypes(active);
	}


	private List<DFSymptomTemplate> getSymptomAntithesis() {
		return dataFrameRepository.getSymptomAntithesis();
	}

	private List<DFSymptomTemplate> getSymptomAntithesis(Boolean active) {
		return dataFrameRepository.getSymptomAntithesis(active);
	}



	private List<DFSymptomData> getApprovedIllneness(Boolean active) {
		return dataFrameRepository.getApprovedIllneness(active);

	}


	private List<DFSymptomData> getApprovedIllneness() {
		return dataFrameRepository.getApprovedIllneness();

	}


	private List<String> getIgnoreSymptoms() {
		return dataFrameRepository.getIgnoreSymptoms();
	}


	private List<String> getTimeBuckets() {


		List<String> listBuckets = new ArrayList<String>();
		DataframeQueryResultEntity results = dataFrameRepository.getTimeBuckets();
		Set<String> periods = results.getTimePeriods();
		if(periods != null && !periods.isEmpty()) {
			listBuckets = periods.stream().collect(Collectors.toList());
		}
		return listBuckets;
	}

	private List<DFIllnessSymptom> getIllnessDataForGivenSymptoms(Boolean active ,List<String> includeSymptoms) {

		return	dataFrameRepository.getIcd10CodesWithSymptomsFromAllSources(active,includeSymptoms);

	}


	private List<DFIllnessSymptom> getIcd10CodesWithSymptomsFromAllSources() {

		return	dataFrameRepository.getIcd10CodesWithSymptomsFromAllSources();

	}


	public  Map<String, String> getIllnessGroupNames(	 List<DataframeQueryResultEntity>  masterIllnesses) {
		if(masterIllnesses != null) {
			return  masterIllnesses.stream().collect(Collectors.toMap(DataframeQueryResultEntity::getIcd10code, DataframeQueryResultEntity::getGroupName, (oldValue, newValue) -> newValue));
		}
		return null;

	}

	private List<DFIllness> getApprovedDiseases(Boolean active) {
		List<DFIllness> illness = dataFrameRepository.getApprovedDiseases(active);
		for (DFIllness dfIllness : illness) {
			dfIllness.setGroup(getGroupNames(dfIllness.getIcd10Code()).trim());
		}


		return illness;
	}



	public String getGroupNames(String icd10code){

		if(icd10code.contains(".")) {
			icd10code = icd10code.substring(0, icd10code.length()-1);
			if(icd10code.length() > 6) {
				icd10code = icd10code.substring(0, icd10code.length()-1);
			}
			if(icd10code.endsWith(".")){
				icd10code =icd10code.substring(0, icd10code.length()-1);
			}
		}

		return groupNames.get(icd10code);
	}


	private List<DFIllness> getApprovedDiseases() {

		List<DFIllness> illness = dataFrameRepository.getApprovedDiseases();
		for (DFIllness dfIllness : illness) {
			dfIllness.setGroup(getGroupNames(dfIllness.getIcd10Code()));
		}
		return illness;
	}

	private CodingRules getMICACodingRules(List<String> micadiseases) {

		CodingRules coding_rules = new CodingRules();

		List<DataframeQueryResultEntity> codeRulesIllness = masterIllnesses.stream().filter(s->s.getRule_id() != null && ! s.getRule_id().isEmpty()   && micadiseases.contains(s.getIcd10code()) ).collect(Collectors.toList());

		coding_rules.setNewborn(codeRulesIllness.stream().filter(s->s.getRule_id().contains(1)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));

		coding_rules.setPediatric(codeRulesIllness.stream().filter(s->s.getRule_id().contains(2)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));

		coding_rules.setMaternity(codeRulesIllness.stream().filter(s->s.getRule_id().contains(3)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));

		coding_rules.setAdult(codeRulesIllness.stream().filter(s->s.getRule_id().contains(4)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));

		coding_rules.setMale(codeRulesIllness.stream().filter(s->s.getRule_id().contains(6)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));

		coding_rules.setFemale(codeRulesIllness.stream().filter(s->s.getRule_id().contains(5)). map(DataframeQueryResultEntity::getIcd10code).collect(Collectors.toList()));
		return coding_rules;
	}


	private void createDFJson(DataFrameJsonModel dataFrameJsonModel) {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
			Path path = Paths.get(jsonLocation+ File.separator +  dataFrameRelPath  + File.separator +  DF_MICAUtil.getAdvinowDateFormat()+  File.separator + dataFrameName + ".json");
			Files.createDirectories(path.getParent());
			String json = gson.toJson(dataFrameJsonModel);
			byte[] utf8JsonString = json.getBytes(StandardCharsets.UTF_8);
			Files.write(path,utf8JsonString, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	*//**
	 * creates JSON file from the object and uploads to S3.
	 * S3 bucket name would be advinow-de-dataframes with location pipeline/date/dataframe.raw.json 
	 * 
	 * @param dataFrameJsonModel
	 *//*
	private String uploadDFJsonToS3(DataFrameJsonModel dataFrameJsonModel) {
		
		String stringObjKeyName  = null;
		return stringObjKeyName;
		
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
			String json = gson.toJson(dataFrameJsonModel);
			byte[] utf8JsonString = json.getBytes(StandardCharsets.UTF_8);
			 stringObjKeyName =  dataFrameRelPath + "/"+  DF_MICAUtil.getAdvinowDateFormat()+   "/" + dataFrameName + ".json";
			logger.info("S3 Path:: " + stringObjKeyName);
			AwsS3Config s3Config= new AwsS3Config();
			AmazonS3 s3Client =s3Config.s3client( awsId, awsKey, region);
			InputStream fileInputStream = new ByteArrayInputStream(utf8JsonString);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(CONTENT_TYPE);
			metadata.setContentLength(utf8JsonString.length);
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					bucketName, stringObjKeyName.trim(), fileInputStream, metadata);
			s3Client.putObject(putObjectRequest);

		} catch(AmazonServiceException e) {
			e.printStackTrace();
		}
		catch(SdkClientException e) {
			e.printStackTrace();
		}
		return stringObjKeyName;
	}

	
	public Integer getUniqueString(DFSymptomData dFSymptomData){
		StringBuffer str = new StringBuffer();
		str.append(dFSymptomData.getSymptomID());
		String StrNull = "NULL";
	
		List<String> bodyParts = dFSymptomData.getBodyParts().stream().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList());
		if( bodyParts != null) {
			str.append(bodyParts);
		} else{
			str.append(StrNull);
		}
    	
		List<String> multiplier = dFSymptomData.getMultiplier().stream().filter(Objects::nonNull).map(String :: trim).collect(Collectors.toList()) ;
	
	if( multiplier != null) {
		str.append(multiplier);
	} else{
		str.append(StrNull);
	}
	
	String time = dFSymptomData.getTimeFrame();
		if( time != null) {
		str.append(time);
	} else{
		str.append(StrNull);
	}
		return Objects.hash(str.toString());
	}
*/}
