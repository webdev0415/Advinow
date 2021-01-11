/**
 * 
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataStoreSources;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomDataStore;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.domain.User;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;
import com.advinow.mica.domain.queryresult.IllnessSourcesResultEnitity;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.mapper.IllnessRequestMapper;
import com.advinow.mica.mapper.IllnessResponseMapper;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourceInfo;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.SymptmSourceInfoModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.SymptomSource;
import com.advinow.mica.repositories.DataStoreModifierRepository;
import com.advinow.mica.repositories.IllnessRepository;
import com.advinow.mica.repositories.OtherSourcesIllnessRepository;
import com.advinow.mica.repositories.SectionRepository;
import com.advinow.mica.repositories.SymptomGroupRepository;
import com.advinow.mica.repositories.UserRepository;
import com.advinow.mica.services.OtherSourceIllnessService;
import com.advinow.mica.util.MICAConstants;

/**
 * @author Govinda Reddy
 *
 */

@Service
@Retry(name = "neo4j")
public class OtherSourcesIllnessServiceImpl implements OtherSourceIllnessService {


	protected Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired	
	OtherSourcesIllnessRepository otherSourcesIllnessRepository;


	@Autowired
	UserRepository userRepository;

	IllnessRequestMapper   illnessRequestMapper =  new IllnessRequestMapper();


	@Autowired
	SymptomGroupRepository   symptomGroupRepository;

	IllnessResponseMapper  illnessResponseMapper = new IllnessResponseMapper();

	@Autowired
	DataStoreModifierRepository dataStoreModifierRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired	
	IllnessRepository illnessRepository;


	/**
	 * 
	 */
	@Override
	public PaginationModel getIllnessWithPaging(Integer page, Integer size,String source,String status,String illnessCode,String name) {
		
		if(name != null) {
			if(name.length() < 3) {
				throw new  DataInvalidException("Illness name should be greater than 3 characters.");
			}
		}
		
		
		if(illnessCode != null) {
			if(illnessCode.length() < 3) {
				throw new  DataInvalidException("Illness code should be greater than 3 characters.");
			}
		}
		
		
		PaginationModel  illnessPages = new PaginationModel();
		Page<Illness>  illnessDbPages = getPagenationContent(illnessPages, page,  size, source, status,illnessCode,name);
			 if(illnessDbPages != null) {
				List<Illness> slices = illnessDbPages.getContent();
				if(slices != null ){
					List <ICD10CodeModel> content = new ArrayList<ICD10CodeModel>();
					for (Illness slice : slices) {
						String icd10Code = slice.getIcd10Code();
						Integer  version =   slice.getVersion();
						ICD10CodeModel iCD10CodeModel = new ICD10CodeModel(); 
						iCD10CodeModel.setIcd10Code(icd10Code);
						iCD10CodeModel.setName(slice.getName());
						iCD10CodeModel.setStatus(slice.getState());
						iCD10CodeModel.setVersion(version);
						iCD10CodeModel.setActive(slice.getActive());
				         //iCD10CodeModel.setUserID(userRepository.getUserID(source, icd10Code, version));
						content.add(iCD10CodeModel);
					}
					illnessPages.setContent(content);
				}

				illnessPages.setFirst(illnessDbPages.isFirst());
				illnessPages.setLast(illnessDbPages.isLast());
				illnessPages.setPagenumber(illnessDbPages.getNumber());
				illnessPages.setElementsInPage(illnessDbPages.getContent().size());
				illnessPages.setPageSize(illnessDbPages.getSize());


			}
		


		return illnessPages;


	}


	private Page<Illness> getPagenationContent(PaginationModel illnessPages,
			Integer page, Integer size, String source, String status,String icd10Code,String name) {
		    Page<Illness>  illnessDbPages = null;
		    Integer totalRecords = null;
	
		    if(page ==null ){
			   page=1; 
		   }
		   if(size == null) {
			   size = 20;
		   }
		   if(source != null) {
			   if(icd10Code != null && status != null && name != null){
				   PageRequest pageable =  PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");	
				      illnessDbPages = otherSourcesIllnessRepository.findBySourceAndIcd10CodeAndNameAndStatus(source.toUpperCase(),status.toUpperCase(),icd10Code.toUpperCase(),name.toUpperCase(), pageable);
				 	  totalRecords =  otherSourcesIllnessRepository.getIllnessCountBySourceAndIcd10CodeAndNameAndStatus(source.toUpperCase(),status.toUpperCase(),icd10Code.toUpperCase(),name.toUpperCase());
				   
			   } else if(icd10Code != null && status != null){
				   PageRequest pageable =  PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");	
				      illnessDbPages = otherSourcesIllnessRepository.findBySourceAndIcd10CodeAndStatus(source.toUpperCase(),status.toUpperCase(),icd10Code.toUpperCase(), pageable);
				   	  totalRecords =  otherSourcesIllnessRepository.getIllnessCountBySourceAndIcd10CodeAndStatus(source.toUpperCase(),status.toUpperCase(),icd10Code.toUpperCase());
				   
			   } else if(name != null && status != null){
				   PageRequest pageable =  PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");	
				      illnessDbPages = otherSourcesIllnessRepository.findBySourceAndNameAndStatus(source.toUpperCase(),status.toUpperCase(),name.toUpperCase(), pageable);
				   	  totalRecords =  otherSourcesIllnessRepository.getIllnessCountBySourceAndNameAndStatus(source.toUpperCase(),status.toUpperCase(),name.toUpperCase());
				   
			   } else if(status != null) {
				  PageRequest pageable =  PageRequest.of(page - 1, size, Sort.Direction.ASC, "illness.icd10Code");	
			        illnessDbPages = otherSourcesIllnessRepository.findBySourceAndStatus(source,status, pageable);
			    	  totalRecords =  otherSourcesIllnessRepository.getIllnessCountBySourceAndStatus(source.toUpperCase(),status.toUpperCase());
				} else{
					  PageRequest pageable =  PageRequest.of(page - 1, size, Sort.Direction.ASC, "icd10Code");	
						  illnessDbPages = otherSourcesIllnessRepository.findBySource(source, pageable);
						   totalRecords =  otherSourcesIllnessRepository.getIllnessCountBySource(source.toUpperCase());
							
				}
			   
		   }
			   
			  illnessPages.setTotalElements(totalRecords);
		return illnessDbPages;
	}

	/* (non-Javadoc)
	 * @see com.advinow.mica.services.OtherSourceIllnessService#findByIllnessByUserAndSource(java.lang.Integer, java.lang.String)
	 */
	@Override
	public IllnessUserData findByIllnessByUserAndSource(Integer userID,
			String source) {/*
		IllnessUserData   userData = new IllnessUserData();
		Iterable<Illness> dbIllnesses  =	ecwIllnessRepository.getIllnessDataByUserIDAndSource(userID,source);
		List<IllnessModel> illnesses= new ArrayList<IllnessModel>();
		if(dbIllnesses != null  ){
			Iterator<Illness> dbIllnessItr = dbIllnesses.iterator();
			while(dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();	
				if(dbIllness !=null) {
					IllnessModel modelIllness = illnessResponseMapper.createIllnessModel(dbIllness);
					modelIllness.setUserID(userID);
					illnesses.add(modelIllness);
				}
			}
		}

		userData.setUserData(illnesses);

		return userData;
			 */



		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		List<IllnessModel> illnesses= new ArrayList<IllnessModel>();
		IllnessUserData   userData = new IllnessUserData();
		Iterable<Illness> dbIllnesse  =	otherSourcesIllnessRepository.getIllnessDataByUserIDAndSource(userID,source);
		Set<Section> dbSection =	sectionRepository.getPainSwellingSection();
		
		if(dbIllnesse != null  ){
			Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
			while(dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();	
				if(dbIllness !=null) {
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness,groups,dbSection);
					modeIillness.setUserID(userID);
					illnesses.add(modeIillness);
				}
			}

		}
		userData.setUserData(illnesses);
		return userData;
	}

	/* (non-Javadoc)
	 * @see com.advinow.mica.services.OtherSourceIllnessService#updateStatusReAssignUser(com.advinow.mica.model.IllnessStatusModel)
	 */
	@Override
	public String updateStatusReAssignUser(IllnessStatusModel illnessStatusModel,String source) {
		Boolean flag =false;
		// TODO Auto-generated method stub
		Illness  dbIllness =   otherSourcesIllnessRepository.findIllnessByStatusAndICD10CodeAndVersionAndSource(illnessStatusModel.getFromState(),illnessStatusModel.getIcd10Code(),illnessStatusModel.getVersion(),source);
		if(dbIllness==null) {			
			throw new DataNotFoundException("No Illness data found with "+  source + "  source");
		} 

		dbIllness.setState(illnessStatusModel.getToState());
		User dbUser = userRepository.findByUserID(illnessStatusModel.getUserID(),illnessStatusModel.getVersion()); {
			if(dbUser == null ){
				dbUser = new User();
				dbUser.setUserID(illnessStatusModel.getUserID());
				dbUser.getIllnesses().add(dbIllness);
				flag = true;
			}else{
				if(dbUser.getIllnesses() !=null  && ! dbUser.getIllnesses().isEmpty()){
					dbUser.getIllnesses().add(dbIllness);
					flag=true;
				}else{
					dbUser.getIllnesses().add(dbIllness);
					flag =true;
				}
			}
		}
		if(flag) {
			userRepository.save(dbUser);	
			userRepository.deleteUser(); // Delete disconnected users from the database.
		}

		return "Illness updated and assigned user.....";
	}

	@Override
	@Transactional
	public IllnessStatusModel  saveIllnessData(IllnessModel illnessModel) {
		IllnessStatusModel illnessStatusModel = new IllnessStatusModel();
		Integer userId=	 illnessModel.getUserID();
		Set<Illness>  illnesses = new HashSet<Illness>();
		User user  = null;
		Integer version = illnessModel.getVersion();
		if(version == null) {
			version =1;
		}

		List<Long> dbIllness = otherSourcesIllnessRepository.findIllnessByICD10Code(illnessModel.getIdIcd10Code(),version,illnessModel.getSource());
		if(dbIllness != null ){
			deleteIllnessData(dbIllness);
		} else{
			throw new DataNotFoundException("No Illness data found with " + illnessModel.getSource() + "source");
		}
		Illness  illness =	illnessRequestMapper.prepareIllness(illnessModel);


		if(userId != null) {
			user = userRepository.findByUserID(userId,1);
			illnesses.add(illness);
			if(user ==null ){
				user = new User();
			}
			user.setUserID(userId);
			user.getIllnesses().addAll(illnesses);
			User savedUser = userRepository.save(user);
			if(savedUser != null )  {
				illnessStatusModel.setCount(1);
				illnessStatusModel.setIcd10Code(illnessModel.getIdIcd10Code());
				illnessStatusModel.setStatus("Illnessess created successfully..");
			}

		} else{
			Illness illness_saved =	otherSourcesIllnessRepository.save(illness);
			if(illness_saved != null )  {
				illnessStatusModel.setCount(1);
				illnessStatusModel.setIcd10Code(illnessModel.getIdIcd10Code());
				illnessStatusModel.setStatus("Illnessess created successfully..");

			}
		}

		return illnessStatusModel;
	}







	/*
	@Override
	public IllnessUserData findByIllnessByUserAndSource(Integer userID,
			String source) {
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		List<IllnessModel> illnesses= new ArrayList<IllnessModel>();
		IllnessUserData   userData = new IllnessUserData();
		Iterable<Illness> dbIllnesse  =	ecwIllnessRepository.getIllnessDataByUserIDAndSource(userID,source);

		if(dbIllnesse != null  ){
			Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
			while(dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();	
				if(dbIllness !=null) {
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness,groups,sectionRepository,dataStoreModifierRepository);
					modeIillness.setUserID(userID);
					illnesses.add(modeIillness);
				}
			}

		}
		userData.setUserData(illnesses);
		return userData;
	}*/


	/*
	@Override
	public IllnessUserData findByIllnessByUserAndSource(Integer userID, String source) {
		IllnessUserData   userData = new IllnessUserData();
		Iterable<Illness> dbIllnesses  =	ecwIllnessRepository.getIllnessDataByUserIDAndSource(userID,source);
		List<IllnessModel> illnesses= new ArrayList<IllnessModel>();
		if(dbIllnesses != null  ){
			Iterator<Illness> dbIllnessItr = dbIllnesses.iterator();
			while(dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();	
				if(dbIllness !=null) {
					IllnessModel modelIllness = illnessResponseMapper.createIllnessModel(dbIllness);
					modelIllness.setUserID(userID);
					illnesses.add(modelIllness);
				}
			}
		}

		userData.setUserData(illnesses);

		return userData;
	}
	 */


	private void deleteIllnessData(List<Long> ids) {

		if(ids != null){
			illnessRepository.deleteIllnessDataModifier(ids);
			illnessRepository.deleteIllnessToDataStore(ids);
			illnessRepository.deleteIllnessToSymptom(ids);
			illnessRepository.deleteIllnessToCategory(ids);
			illnessRepository.deleteIllness(ids);
		}

	}

	@Override
	public IllnessUserData getIllnessByIcd10Code(String icd10Code, String state,String source) {
		Iterable<SymptomGroup> groups = symptomGroupRepository.findAll(1);
		List<IllnessModel> illnesses= new ArrayList<IllnessModel>();
		IllnessUserData   userData = new IllnessUserData();
	//	List<String> states = new ArrayList<String>();
		Set<Section> dbSection =	sectionRepository.getPainSwellingSection();
		
		Iterable<Illness> dbIllnesse = null;
		if(state != null ) {
			//states.add(StringUtils.upperCase(state));
			dbIllnesse  = otherSourcesIllnessRepository.getIllnessDataByIcd10CodeAndState(StringUtils.upperCase(icd10Code),state,source);

		}  else 
		{
			dbIllnesse  = otherSourcesIllnessRepository.getIllnessDataByIcd10CodeAndNoState(StringUtils.upperCase(icd10Code),source);
		}
		if(dbIllnesse != null  ){
			Iterator<Illness> dbIllnessItr = dbIllnesse.iterator();
			while(dbIllnessItr.hasNext()) {
				Illness dbIllness = dbIllnessItr.next();	
				if(dbIllness !=null) {
					IllnessModel modeIillness = illnessResponseMapper.createIllnessModelByGroup(dbIllness,groups,dbSection);
					illnesses.add(modeIillness);
				}
			}

		}
		userData.setUserData(illnesses);
		return userData;
	}
	
	
	
/*

	@Override
	public Set<SymptomSource> getSymptomsWithSource(String icd10Code,
			String state, String source) {
		List<Symptom> dbSourceInfo =  null;
		Set<SymptomSource> symptomSourceInfo = new HashSet<SymptomSource>(); 
		if(state != null) {
			dbSourceInfo =  otherSourcesIllnessRepository.getSymptomsWithSourceAndState(icd10Code,state,source);
		} else{
			dbSourceInfo =  otherSourcesIllnessRepository.getSymptomsWithSource(icd10Code,source);  
		}

		if(dbSourceInfo != null &&  !dbSourceInfo.isEmpty() ) {
			
		List<String> symptomIDs = dbSourceInfo.stream().map(Symptom::getCode).collect(Collectors.toList());
		
	    List<GenericQueryResultEntity> symptomTemplates = otherSourcesIllnessRepository.getSymptomNames(symptomIDs);
		
	   Map<String, String> codesMap = symptomTemplates.stream().collect(Collectors.toMap(GenericQueryResultEntity::getSymptomID, GenericQueryResultEntity::getSymptomName, (oldValue, newValue) -> newValue));
			
			for (Symptom symptom : dbSourceInfo) {				
				Set<SymptmSourceInfoModel> sources = getSourceInfo(symptom.getSymptomDataStore());
				if(sources != null  &&  !sources.isEmpty())  {				
				SymptomSource modelSymptomInfo = new SymptomSource();
				modelSymptomInfo.setSymptomID(symptom.getCode());
				modelSymptomInfo.setSymptomName(codesMap.get(symptom.getCode()));
				modelSymptomInfo.setSourceInformation(sources);
				symptomSourceInfo.add(modelSymptomInfo);
				}
			}
		}
		return symptomSourceInfo;
	}

	private Set<SymptmSourceInfoModel> getSourceInfo(Set<SymptomDataStore> symptomDataStore) {
		Set<SymptmSourceInfoModel> sources = new HashSet<SymptmSourceInfoModel>();
		if(symptomDataStore != null ) {
			Iterator<SymptomDataStore> dsItr = symptomDataStore.iterator();
			while(dsItr.hasNext()) {
				SymptomDataStore ds = dsItr.next();
				if( ds != null && ds.getSources() != null &&  !ds.getSources().isEmpty()) { 
					   List<SymptomSourceInfo> dbSources = otherSourcesIllnessRepository.getSymptomSourceInfo(ds.getSources());
					   if(dbSources != null ) {
						   for (SymptomSourceInfo symptomSourceInfo : dbSources) {
							   SymptmSourceInfoModel symptmSourceInfoModel = new SymptmSourceInfoModel();
							   symptmSourceInfoModel.setSourceRefDate(ds.getSourceRefDate());
								symptmSourceInfoModel.setSourceType(symptomSourceInfo.getSourceType());
								symptmSourceInfoModel.setSource(symptomSourceInfo.getSource());
								symptmSourceInfoModel.setSourceId(symptomSourceInfo.getSourceID());
								sources.add(symptmSourceInfoModel);
								if(ds.getMultiplier() != null && !ds.getMultiplier().isEmpty()) {
								symptmSourceInfoModel.setMultiplier(ds.getMultiplier().get(0));
								}
							
						}
						   
					   }
					  
				}
		
			}
		}
		return sources;
	}
	*/
	

	
	// new source start here
	
	
	
	@Override
	public Set<SymptomSource> getSymptomsWithSource(String icd10Code,
			String state, String source) {
		List<Symptom> dbSourceInfo =  null;
		Set<SymptomSource> symptomSourceInfo = new HashSet<SymptomSource>(); 
		
				dbSourceInfo =  otherSourcesIllnessRepository.getSymptomsWithSourceAndState(icd10Code,state.toUpperCase(),source);
	
		if(dbSourceInfo != null &&  !dbSourceInfo.isEmpty() ) {
			
		    Set<String> icd10Codes= new HashSet<String>();
		    icd10Codes.add(icd10Code);
			List<GenericQueryResultEntity> symptomTemplates =otherSourcesIllnessRepository.getAllSymptoms(icd10Codes,state.toUpperCase(),source);
			
		   Map<String, String> codesMap = symptomTemplates.stream().collect(Collectors.toMap(GenericQueryResultEntity::getSymptomID, GenericQueryResultEntity::getSymptomName, (oldValue, newValue) -> newValue));
			
		
			for (Symptom symptom : dbSourceInfo) {				
				List<SymptmSourceInfoModel> sources = getSourceInformation(symptom.getSymptomDataStore());
				if(sources != null  &&  !sources.isEmpty())  {				
				SymptomSource modelSymptomInfo = new SymptomSource();
				modelSymptomInfo.setSymptomID(symptom.getCode());
				modelSymptomInfo.setSymptomName(codesMap.get(symptom.getCode()));
				modelSymptomInfo.setSourceInformation(sources);
				symptomSourceInfo.add(modelSymptomInfo);
				}
			}
		}
		return symptomSourceInfo;
	}



	
	private List<SymptmSourceInfoModel> getSourceInformation(Set<SymptomDataStore> symptomDataStore) {
		List<SymptmSourceInfoModel> sources = new ArrayList<SymptmSourceInfoModel>();
		if(symptomDataStore != null ) {
			Iterator<SymptomDataStore> dsItr = symptomDataStore.iterator();
			while(dsItr.hasNext()) {
				SymptomDataStore ds = dsItr.next();
			//	Set<DataStoreSources> symptomdBSources = ds.getSourceInfo();
				List<DataStoreSources> symptomSources = adjustSources(ds.getSourceInfo());
				
				if( ds != null && symptomSources != null &&  ! symptomSources.isEmpty()) { 
				List<Integer> sourceIDs = symptomSources.stream().map(DataStoreSources::getSourceID).collect(Collectors.toList());
				   List<SymptomSourceInfo> dbSources = otherSourcesIllnessRepository.getSymptomSourceInfo(sourceIDs);
			
					   if(dbSources != null ) {
						   for (SymptomSourceInfo symptomSourceInfo : dbSources) {
							   SymptmSourceInfoModel symptmSourceInfoModel = new SymptmSourceInfoModel();
							    symptmSourceInfoModel.setSourceRefDate(symptomSources.stream().findFirst().get().getSourceRefDate());
								symptmSourceInfoModel.setSourceType(symptomSourceInfo.getSourceType());
								symptmSourceInfoModel.setSource(symptomSourceInfo.getSource());
								symptmSourceInfoModel.setSourceId(symptomSourceInfo.getSourceID());
								sources.add(symptmSourceInfoModel);
								if(ds.getMultiplier() != null && !ds.getMultiplier().isEmpty()) {
								symptmSourceInfoModel.setMultiplier(ds.getMultiplier().get(0));
								}
							
						}
						   
					   }
					  
				}
		
			}
		}
		return sources;
	}
	
	
	
	
	private List<SymptmSourceInfoModel> getSourceInfo(Set<SymptomDataStore> symptomDataStore,List<SymptomSourceInfo> dbMasterSources) {
		List<SymptmSourceInfoModel> sources = new ArrayList<SymptmSourceInfoModel>();
		if(symptomDataStore != null ) {
			Iterator<SymptomDataStore> dsItr = symptomDataStore.iterator();
			while(dsItr.hasNext()) {
				SymptomDataStore ds = dsItr.next();
			//	Set<DataStoreSources> symptomdBSources = ds.getSourceInfo();
				List<DataStoreSources> symptomSources = adjustSources(ds.getSourceInfo());
				
				if( ds != null && symptomSources != null &&  ! symptomSources.isEmpty()) { 
				List<Integer> sourceIDs = symptomSources.stream().map(DataStoreSources::getSourceID).collect(Collectors.toList());
					//   List<SymptomSourceInfo> dbSources = otherSourcesIllnessRepository.getSymptomSourceInfo(sourceIDs);
				List<SymptomSourceInfo> dbSources =
						dbMasterSources.stream()
					            .filter(e -> sourceIDs.contains(e.getSourceID()))
					            .collect(Collectors.toList());
					   if(dbSources != null ) {
						   for (SymptomSourceInfo symptomSourceInfo : dbSources) {
							   SymptmSourceInfoModel symptmSourceInfoModel = new SymptmSourceInfoModel();
							    symptmSourceInfoModel.setSourceRefDate(symptomSources.stream().findFirst().get().getSourceRefDate());
								symptmSourceInfoModel.setSourceType(symptomSourceInfo.getSourceType());
								symptmSourceInfoModel.setSource(symptomSourceInfo.getSource());
								symptmSourceInfoModel.setSourceId(symptomSourceInfo.getSourceID());
								sources.add(symptmSourceInfoModel);
								if(ds.getMultiplier() != null && !ds.getMultiplier().isEmpty()) {
								symptmSourceInfoModel.setMultiplier(ds.getMultiplier().get(0));
								}
							
						}
						   
					   }
					  
				}
		
			}
		}
		return sources;
	}
	
	
	// new source end here
	

	@Override
	public List<IllnessDataQueryResultEnitity> getApprovedIllnesses(
			String source) {
		// TODO Auto-generated method stub
		return illnessRepository.getApprovedIllnessesBySource(source);
	}

	
	
private List<DataStoreSources> adjustSources(Set<DataStoreSources> dbSources) {
	// TODO Auto-generated method stub
		List<DataStoreSources> sources =new ArrayList<DataStoreSources>();
		
		if(dbSources != null && ! dbSources.isEmpty()) {
	
			sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && s.getVerified()).limit(5).collect(Collectors.toList()));
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && s.getVerified()).limit(5).collect(Collectors.toList()));
			}
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				 sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("Doctor") && !s.getVerified()).limit(5).collect(Collectors.toList()));
				} 
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll(dbSources.stream().filter(s->s.getAddedBy().equalsIgnoreCase("NLP") && !s.getVerified()).limit(5).collect(Collectors.toList()));
			} 
			
			
		
			if(sources.isEmpty() || sources.size() < MICAConstants.sourceSize ) {
				sources.addAll( dbSources.stream().limit(5).collect(Collectors.toList()));
			}
	
			if(sources.size() > MICAConstants.sourceSize) {
				sources  = sources.stream().limit(5).collect(Collectors.toList());
			}
			
		}
		return sources;
	
}


@Override
public List<IllnessSourceInfo> getSymptomsSourcesFromIllnesses(List<String> icd10Codes, String state, String source) {

	List<IllnessSourceInfo> illnessSources = new ArrayList<IllnessSourceInfo>();

	Set<String> finalIcd10Codes = icd10Codes.stream().collect(Collectors.toSet());

	List<GenericQueryResultEntity> symptomTemplates = null;

	try {
		symptomTemplates = otherSourcesIllnessRepository.getAllSymptoms(finalIcd10Codes, state.toUpperCase(), source);
	} catch (Exception e) {

		System.out.println("getSymptomsSourcesFromIllnesses Exception");
		finalIcd10Codes.forEach((value) -> System.out.println("icd10Code:" + value));

		e.printStackTrace();
		// return new ArrayList<IllnessSourceInfo>();

		// quick fix
		symptomTemplates = new ArrayList<GenericQueryResultEntity>();
	}

	System.out.println("symptomTemplates " + symptomTemplates.size() + " symptomTemplates.toString()"
			+ symptomTemplates.toString());

	Map<String, String> codesMap = symptomTemplates.stream()
			.collect(Collectors.toMap(GenericQueryResultEntity::getSymptomID, GenericQueryResultEntity::getSymptomName,
					(oldValue, newValue) -> newValue));

	System.out.println("codesMap " + codesMap);

	List<SymptomSourceInfo> dbMasterSources = new ArrayList<SymptomSourceInfo>();

	List<SymptomSourceInfo> result = otherSourcesIllnessRepository.getSymptomMasterSourceInfo(finalIcd10Codes,
			state.toUpperCase(), source);

	System.out.println("result " + result);

	// List<SymptomSourceInfo> dbMasterSources = null;

	List<Callable<IllnessSourceInfo>> callableTasks = new ArrayList<Callable<IllnessSourceInfo>>();

	ExecutorService exec = Executors.newFixedThreadPool(finalIcd10Codes.size());

	for (String icd10Code : finalIcd10Codes) {
		Callable<IllnessSourceInfo> callable = (() -> {
			// Perform some computation
			// Thread.sleep(2000);
			return getIllnessSourceInfo(icd10Code, dbMasterSources, state, source, codesMap);
		});

		callableTasks.add(callable);
	}

	try {
		Collection<Future<IllnessSourceInfo>> futures = exec.invokeAll(callableTasks);

		for (Future<IllnessSourceInfo> f : futures) {
			if (f.isDone()) {
				illnessSources.add(f.get());
			}
		}
		exec.shutdown();
	} catch (Exception ex) {
		ex.printStackTrace();
	}

//	
//	for (String icd10Code: finalIcd10Codes) {
//		
//		
//	
//		IllnessSourceInfo finalIllnesSourceInfo = getIllnessSourceInfo(icd10Code,dbMasterSources,state,source,codesMap);
//		
//		
//		
//		//illnessSources.add(finalIllnesSourceInfo);
//	}

	return illnessSources;
}


private IllnessSourceInfo getIllnessSourceInfo(String icd10Code,List<SymptomSourceInfo> dbMasterSources,String state, String source,Map<String, String> codesMap) {	

	IllnessSourceInfo illnessSourceInfo = new IllnessSourceInfo();
	List<Symptom> dbSourceInfo  =  otherSourcesIllnessRepository.getSymptomsWithSourceAndState(icd10Code,state.toUpperCase(),source);
	Set<SymptomSource> symptomSourceInfo = new HashSet<SymptomSource>(); 
	if(dbSourceInfo != null &&  !dbSourceInfo.isEmpty() ) {
		for (Symptom symptom : dbSourceInfo) {				
			List<SymptmSourceInfoModel> sources = getSourceInfo(symptom.getSymptomDataStore(),dbMasterSources);
			if(sources != null  &&  !sources.isEmpty())  {				
				SymptomSource modelSymptomInfo = new SymptomSource();
				modelSymptomInfo.setSymptomID(symptom.getCode());
				modelSymptomInfo.setSymptomName(codesMap.get(symptom.getCode()));
				modelSymptomInfo.setSourceInformation(sources);
				symptomSourceInfo.add(modelSymptomInfo);
			}
		}

	}
	illnessSourceInfo.setIcd10Code(icd10Code);
	illnessSourceInfo.setSymptoms(symptomSourceInfo);
	return illnessSourceInfo;


}


}
