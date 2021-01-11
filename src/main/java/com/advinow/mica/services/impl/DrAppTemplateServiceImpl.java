package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.mapper.DrAppSymptomGroupResponseMapper;
import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.SymptomDictionary;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.cache.CacheGroupsTime;
import com.advinow.mica.repositories.DataKeysRepository;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.DrAppTemplateService;
import com.advinow.mica.services.MICACacheService;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class DrAppTemplateServiceImpl implements DrAppTemplateService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SymptomGroupRepository   symptomGroupRepository;

	@Autowired
	DataKeysRepository dataKeysRepository;

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository; 

	DrAppSymptomGroupResponseMapper drAppSymptomGroupResponseMapper = new DrAppSymptomGroupResponseMapper();

	@Autowired
	MICACacheService  mICACacheService;
	
	@Value("${server.cache}")
	Boolean serverCache;
	/*
	@Override
	public MICASymptomsGroup getSymtomGroups() {
		MICASymptomsGroup mICASymptomsGroup= new MICASymptomsGroup();
		List<SymptomGroups> modelGroups =  new ArrayList<SymptomGroups>();
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(2);

		Set<SymptomTemplate> snomedSymTemplates = symptomTemplateRepository.getSnomedSymptomTemplates();

		Iterable<DataKeys> iterableRcodes = dataKeysRepository.findAll(1);                                
		// Convert Iterable to Set 
		Set<DataKeys> 	dataKeys  =  (Set<DataKeys>) StreamSupport.stream(iterableRcodes.spliterator(), false).collect(
				Collectors.<DataKeys> toSet());
		Iterator<SymptomGroup> groupIter = groups.iterator();
		while(groupIter.hasNext()){
			String groupID = groupIter.next().getGroupID();
			SymptomGroup  symptomGroup =null;
			if(groupID.equals("pain")){
				symptomGroup=	symptomGroupRepository.findByGroupPainCode(groupID);
			}else{
				symptomGroup=	symptomGroupRepository.findByGroupCode(groupID);
			}
			SymptomGroups responseGroup = drAppSymptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,dataKeys,snomedSymTemplates);
			modelGroups.add(responseGroup);
			mICASymptomsGroup.setSymptomGroups(modelGroups);

		}
		return mICASymptomsGroup;
	}
*/
	
	
	@Override
	public MICASymptomsGroup getSymtomGroups() {
		MICASymptomsGroup mICASymptomsGroup= new MICASymptomsGroup();
		List<SymptomGroups> modelGroups =  new ArrayList<SymptomGroups>();
		Set<SymptomGroup> groups = symptomGroupRepository.findGroups();	
		Map<String, Long> dbGroupMap = groups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID, SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));
		
		/*dbGroupMap.forEach((groupID,timestamp)->{
			SymptomGroups symptomGroups =getDrAppSymptomsByGroupFromCache(groupID);
			modelGroups.add(symptomGroups);
		});
		*/
		
		
		List<Callable<SymptomGroups>> callableTasks = new ArrayList<Callable<SymptomGroups>>();
		ExecutorService exec = Executors.newFixedThreadPool(dbGroupMap.size());
		
		dbGroupMap.forEach((groupID,timestamp)->{	
		Callable<SymptomGroups> callable =( () -> {
		    // Perform some computation
			   return getDrAppSymptomsByGroupFromCache(groupID);
		});
		 
		callableTasks.add(callable);
		
		});
		try {
		 Collection<Future<SymptomGroups>> futures = exec.invokeAll(callableTasks);
		 
	        for (Future<SymptomGroups> f : futures) {
	        	if(f.isDone()) {
	        	modelGroups.add(f.get());
	        	}
	        }
	        exec.shutdown();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
		mICASymptomsGroup.setSymptomGroups(modelGroups);
		
	
		return mICASymptomsGroup;
	}
	
	
	
/*	@Override
	public SymptomDictionary getSymptomDictionaries() {
		SymptomDictionary symptomDictionary = new SymptomDictionary();
		Map<String,Map<String, Object>> symptom_id_dict = new Hashtable<String,Map<String, Object>>();
		List<GenericQueryResultEntity>  painSwellingResults =	symptomTemplateRepository.findPainSwellingGroups();
		updateTemplateDictionary(painSwellingResults,symptom_id_dict);
		List<GenericQueryResultEntity>  groupsResults =	symptomTemplateRepository.findSymptomGroups();
		updateTemplateDictionary(groupsResults,symptom_id_dict);
		symptomDictionary.setGroupsUpdatedDate(getGroupsUpdatedDictionary());
		symptomDictionary.setSymptom_snomed_dict(getSnomedCodes());
		symptomDictionary.setSymptom_id_dict(symptom_id_dict);
		List<GenericQueryResultEntity> symptom_groups_dict =symptomTemplateRepository.getLogicalGroups(); 
		symptomDictionary.setSymptom_groups_dict(symptom_groups_dict);
		return symptomDictionary;
	}
*/



	
	@Override
	public SymptomDictionary getSymptomDictionaries() {
		
		SymptomDictionary symptomDictionary = new SymptomDictionary();
		Map<String,Map<String, Object>> symptom_id_dict = new Hashtable<String,Map<String, Object>>();
		List<GenericQueryResultEntity> symptom_groups_result =symptomTemplateRepository.getLogicalGroups(); 		
	//	List<GenericQueryResultEntity>  painSwellingResults =	symptomTemplateRepository.findPainSwellingGroups();
		//updateTemplateDictionary(painSwellingResults,symptom_id_dict, symptom_groups_result );
		List<GenericQueryResultEntity>  groupsResults =	symptomTemplateRepository.findSymptomGroups();
		updateTemplateDictionary(groupsResults,symptom_id_dict,symptom_groups_result);
		symptomDictionary.setGroupsUpdatedDate(getGroupsUpdatedDictionary());
		symptomDictionary.setSymptom_snomed_dict(getSnomedCodes());
		symptomDictionary.setSymptom_id_dict(symptom_id_dict);
	//	symptomDictionary.setSymptom_groups_dict(symptom_groups_dict);
		return symptomDictionary;
	}

	
	
	
/*	*//**
	 * Prepare symptom template dictionary
	 * 
	 * @param groupsTemplateResults
	 * @param symptom_id_dict
	 *//*
	private void updateTemplateDictionary(
			List<GenericQueryResultEntity> groupsTemplateResults,
			Map<String, Map<String, Object>> symptom_id_dict) {
		for (GenericQueryResultEntity genericQueryResultEntity : groupsTemplateResults) {
			symptom_id_dict.put(genericQueryResultEntity.getSymptomID(), genericQueryResultEntity.getLiteralMap().iterator().next());
		}

	}
	
	
*/
	/**
	 * Prepare symptom template dictionary
	 * 
	 * @param groupsTemplateResults
	 * @param symptom_id_dict
	 * @param symptom_groups_result 
	 */

	private void updateTemplateDictionary(
			List<GenericQueryResultEntity> groupsTemplateResults,
			Map<String, Map<String, Object>> symptom_id_dict, List<GenericQueryResultEntity> symptom_groups_result) {
		for (GenericQueryResultEntity genericQueryResultEntity : groupsTemplateResults) {
			Map<String, Object> finalMap = new Hashtable<String, Object>();
			Map<String, Object> symptomMap = genericQueryResultEntity.getLiteralMap().iterator().next();
			Map<String,Object> groupMap= new Hashtable<String, Object>();
			GenericQueryResultEntity groupSymptom = symptom_groups_result.stream().filter(s->s.getSymptomID().equalsIgnoreCase(genericQueryResultEntity.getSymptomID())).findAny().orElse(null);
		//	if(groupSymptom != null & groupSymptom.getLogicalGroupNames() != null && ! groupSymptom.getLogicalGroupNames().isEmpty() ){
			if(groupSymptom != null ) {	
			groupMap.put("logicalGroupNames", groupSymptom.getLogicalGroupNames());
			}

			finalMap =	Stream.concat(symptomMap.entrySet().stream(), groupMap.entrySet().stream())
					.collect(Collectors.toMap(
							entry -> entry.getKey(), // The key
							entry -> entry.getValue() // The value
							)
							);


			symptom_id_dict.put(genericQueryResultEntity.getSymptomID(), finalMap);
		}

	}

	
	
	
	
	
	
	
	/**
	 * Prepare date updated dictionary for all the groups.
	 * 
	 * @return
	 */
	private Map<String, Object> getGroupsUpdatedDictionary() {
		GenericQueryResultEntity groupsUpdatedDates= 	symptomTemplateRepository.findUpdatedTimeForGroups();
		Map<String, Object> groupsUpdatedDate  = null;
		if(groupsUpdatedDates != null) {
		groupsUpdatedDate = groupsUpdatedDates.getLiteralMap().stream().collect(Collectors.toMap(s -> (String) s.get("groupName"), s ->  s.get("updatedDate")));
		
				}
		return groupsUpdatedDate;
	}

	
	
	/**
	 * 
	 * Prepare snomed code dictionary for all the templates with descriptions.
	 * 
	 * @return
	 *//*
	private Map<Pair<String, String>, Object> getSnomedCodes() {
		List<GenericQueryResultEntity>  snomedCodeResults =	symptomTemplateRepository.findSymptomSnomedGroups();
		Map<Pair<String, String> , Object> symptom_snomed_dict = new HashMap<Pair<String, String>, Object>();
		for (GenericQueryResultEntity neo4jQueryResultEntity : snomedCodeResults) {
			String value =null;
			if(neo4jQueryResultEntity.getListValue() !=null ) {
				value ="'"+ neo4jQueryResultEntity.getListValue()+"'";
			}
			Pair<String, String> key = Pair.of("'"+neo4jQueryResultEntity.getSymptomID()+"'", value);
			symptom_snomed_dict.put(key, neo4jQueryResultEntity.getSnomedName());
		}
		return symptom_snomed_dict;
	}
*/

	
	/**
	 * 
	 * Prepare snomed code dictionary for all the templates with descriptions.
	 * 
	 * @return
	 */
	private List<GenericQueryResultEntity>  getSnomedCodes() {
		List<GenericQueryResultEntity>  snomedCodeResults =	symptomTemplateRepository.findSymptomSnomedGroups();
	/*	Map<Pair<String, String> , Object> symptom_snomed_dict = new HashMap<Pair<String, String>, Object>();
		for (GenericQueryResultEntity neo4jQueryResultEntity : snomedCodeResults) {
			String value =null;
			if(neo4jQueryResultEntity.getListValue() !=null ) {
				value ="'"+ neo4jQueryResultEntity.getListValue()+"'";
			}
			Pair<String, String> key = Pair.of("'"+neo4jQueryResultEntity.getSymptomID()+"'", value);
			symptom_snomed_dict.put(key, neo4jQueryResultEntity.getSnomedName());
		}*/
		return snomedCodeResults;
	}
	

	private SymptomGroups getDrAppSymptomsByGroupFromCache(String groupID) {
		
		
		
		
	/*	Set<SymptomTemplate> snomedSymTemplates = symptomTemplateRepository.getSnomedSymptomTemplates();

		Iterable<DataKeys> iterableRcodes = dataKeysRepository.findAll(1);                                
		// Convert Iterable to Set 
		Set<DataKeys> 	dataKeys  =  (Set<DataKeys>) StreamSupport.stream(iterableRcodes.spliterator(), false).collect(
				Collectors.<DataKeys> toSet());
		Iterator<SymptomGroup> groupIter = groups.iterator();
		while(groupIter.hasNext()){
			String groupID = groupIter.next().getGroupID();
			SymptomGroup  symptomGroup =null;
			if(groupID.equals("pain")){
				symptomGroup=	symptomGroupRepository.findByGroupPainCode(groupID);
			}else{
				symptomGroup=	symptomGroupRepository.findByGroupCode(groupID);
			}
			SymptomGroups responseGroup = drAppSymptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,dataKeys,snomedSymTemplates);
			modelGroups.add(responseGroup);
			mICASymptomsGroup.setSymptomGroups(modelGroups);

		}
		
		*/
		
		
		
		
		
		SymptomGroups symptomGroups = null;
		boolean loadFromCache = true;
		Set<SymptomGroup> dbGroups = symptomGroupRepository.findGroups();	
		if(serverCache) {
			Map<String, Long> dbGroupMap = dbGroups.stream().collect(Collectors.toMap(SymptomGroup::getGroupID, SymptomGroup::getUpdatedDate, (oldValue, newValue) -> newValue));
			Map<String, Long> 	cachedGroups = 	mICACacheService.getDrAppChachedGroups("drapp");
			// check for cached groups from the file, if no groups found create with incoming group id
			if(cachedGroups != null && !cachedGroups.isEmpty()) {
				  Long dbDate=	dbGroupMap.get(groupID);
				  Long  cachedDate=	cachedGroups.get(groupID);
				  if(cachedDate==null){
					  loadFromCache =false;
				  } else{
						Boolean dateCompare =Objects.equals(dbDate, cachedDate);
						if(dateCompare) {
							loadFromCache=true;
						} else{
							loadFromCache = false;
						}
				  }
				
			} else{
			   cachedGroups = new Hashtable<String, Long>();
			   loadFromCache = false;
			}
		
			if(loadFromCache){
				symptomGroups= mICACacheService.loadDrAppSymptomsFromCache(groupID,"drapp");
			} else{
				symptomGroups = getSymptomGroupsData(groupID); 
				Long updatedDate=	dbGroupMap.get(groupID);
				cachedGroups.put(groupID, updatedDate);
			}
			
			if(symptomGroups ==null) {
				Long updatedDate=	dbGroupMap.get(groupID);
				cachedGroups.put(groupID, updatedDate);
				symptomGroups = getSymptomGroupsData(groupID); 
				loadFromCache = false;
			}

			if(!loadFromCache) {
				CacheGroupsTime  dbGroupsTime =  new CacheGroupsTime();
				dbGroupsTime.setGroupsTimeMap(cachedGroups);
				mICACacheService.createDrAppGroupTimeMap(dbGroupsTime,"drapp");
				mICACacheService.createDrAppSymptomCache(symptomGroups, groupID,"drapp");

			}

		} 
		else{
			symptomGroups = getSymptomGroupsData(groupID);
		}

		return symptomGroups;
	}



	private SymptomGroups getSymptomGroupsData(String groupID) {
		Set<SymptomTemplate> snomedSymTemplates = symptomTemplateRepository.getSnomedSymptomTemplates();
		Iterable<DataKeys> iterableRcodes = dataKeysRepository.findAll(1);     
		List<GenericQueryResultEntity> logicalGroups = symptomTemplateRepository.getLogicalGroups();
		List<GenericQueryResultEntity> deGroups = symptomTemplateRepository.getDEGroups();
		// Convert Iterable to Set 
		Set<DataKeys> 	dataKeys  =  (Set<DataKeys>) StreamSupport.stream(iterableRcodes.spliterator(), false).collect(
				Collectors.<DataKeys> toSet());
		SymptomGroup  symptomGroup =null;
		if(groupID.equals("pain")){
			symptomGroup=	symptomGroupRepository.findByGroupPainCode(groupID);
		}else{
			symptomGroup=	symptomGroupRepository.findByGroupCode(groupID);
		}
		return drAppSymptomGroupResponseMapper.prepareSymptomsGroupByName(symptomGroup,dataKeys,snomedSymTemplates,logicalGroups, deGroups);
		
	}



}
