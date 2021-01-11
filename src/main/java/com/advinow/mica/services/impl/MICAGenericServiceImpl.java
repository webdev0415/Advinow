package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.model.BodyParts;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.mita.MITAUser;
import com.advinow.mica.repositories.DataKeysRepository;
import com.advinow.mica.repositories.IllnessRepository;
import com.advinow.mica.repositories.SymptomCategoryRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.MICAGenericService;
import com.advinow.mica.services.MICAIllnessService;
import com.advinow.mica.util.MICAConstants;

@Service
@Retry(name = "neo4j")
public class MICAGenericServiceImpl implements MICAGenericService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	DataKeysRepository dataKeysRepository;

	@Autowired	
	IllnessRepository illnessRepository;
	
	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;
	
	@Autowired
	SymptomCategoryRepository  symptomCategoryRepository;
	
	@Autowired
	MICAIllnessService mICAIllnessService;

	

	@Value("${mita.rest.url}")
	String mitaBaseUrl;

	
	public DataKeys getDataKeysByName(String keyName) {
		return dataKeysRepository.findByName(keyName, 2);
	
	}/*
	
	@Override
	public BodyParts getBodyParts(String LanguageCode) {
		// TODO Auto-generated method stub
    	 SymptomCategory symptomCategory = symptomCategoryRepository.findByRootBodyPart(); 
    	 BodyParts  bodyParts = null;
    	 if(symptomCategory != null ) {
    		  bodyParts = new BodyParts();
    		  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
    			  bodyParts.setEs_name(symptomCategory.getEs_name());
    			  bodyParts.setEs_position(symptomCategory.getEs_bodyLocations());
    		 }
    		  bodyParts.setName(symptomCategory.getName());
    		  bodyParts.setBodypartID(symptomCategory.getCode());
    		  bodyParts.setPosition(symptomCategory.getBodyLocations());
    		  bodyParts.setSubParts(prepareSubParts(symptomCategory.getCode(),LanguageCode));
     	 }
     	return bodyParts;
	}

	private List<BodyParts> prepareSubParts(String parent,String LanguageCode) {		
		List<BodyParts> subParts = new ArrayList<BodyParts>();		
		List<SymptomCategory> listParts = symptomCategoryRepository.findBySubBodyPart(parent);
		if(listParts != null && ! listParts.isEmpty()){			
			for (int i = 0; i < listParts.size(); i++) {
				SymptomCategory st =	listParts.get(i);
				if(st != null ) {
					  BodyParts  bodyParts = new BodyParts();
					  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
		    			  bodyParts.setEs_name(st.getEs_name());
		    			  bodyParts.setEs_position(st.getEs_bodyLocations());
		    		 }
					  bodyParts.setName(st.getName());
		    		  bodyParts.setBodypartID(st.getCode());
		    		  bodyParts.setPosition(st.getBodyLocations());
		    		 
		    		  if(st.getParent() !=null) {
		    		  bodyParts.setSubParts(prepareSubParts(st.getCode(),LanguageCode));
		    		  }
		    		  subParts.add(bodyParts);
				}
				
			}
			
		}
			return subParts;
	}*/
	
	
	

	
	@Override
	public BodyParts getBodyParts(String LanguageCode) {
		// TODO Auto-generated method stub
		SymptomCategory symptomCategory = null;
		List<SymptomCategory> categories = symptomCategoryRepository.findAllBodyParts(); 
		if(categories != null ) {
			symptomCategory = categories.stream().filter(s->s.getParent()==null).findAny().orElse(null);
		}
		
    	 BodyParts  bodyParts = null;
    	 if(symptomCategory != null ) {
    		  bodyParts = new BodyParts();
    		  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
    			  bodyParts.setEs_name(symptomCategory.getEs_name());
    			  bodyParts.setEs_position(symptomCategory.getEs_bodyLocations());
    		 }
    		  bodyParts.setName(symptomCategory.getName());
    		  bodyParts.setBodypartID(symptomCategory.getCode());
    		  bodyParts.setPosition(symptomCategory.getBodyLocations());
    		  bodyParts.setSubParts(prepareSubParts(categories,symptomCategory.getCode(),LanguageCode));
     	 }
     	return bodyParts;
	}

	private List<BodyParts> prepareSubParts(List<SymptomCategory> categories,String parent,String LanguageCode) {		
		List<BodyParts> subParts = new ArrayList<BodyParts>();		
		List<SymptomCategory> listParts =	categories.stream().filter(s->s.getParent() != null && s.getParent().equals(parent)).collect(Collectors.toList());
		//List<SymptomCategory> listParts =	categories.stream().filter(s->s.getParent().equals(parent)).collect(Collectors.toList());
	//	List<SymptomCategory> listParts = symptomCategoryRepository.findBySubBodyPart(parent);
		if(listParts != null && ! listParts.isEmpty()){			
			for (int i = 0; i < listParts.size(); i++) {
				SymptomCategory st =	listParts.get(i);
				if(st != null ) {
					  BodyParts  bodyParts = new BodyParts();
					  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
		    			  bodyParts.setEs_name(st.getEs_name());
		    			  bodyParts.setEs_position(st.getEs_bodyLocations());
		    		 }
					  bodyParts.setName(st.getName());
		    		  bodyParts.setBodypartID(st.getCode());
		    		  bodyParts.setPosition(st.getBodyLocations());
		    		 
		    		  if(st.getParent() !=null) {
		    		  bodyParts.setSubParts(prepareSubParts(categories,st.getCode(),LanguageCode));
		    		  }
		    		  subParts.add(bodyParts);
				}
				
			}
			
		}
			return subParts;
	}
	
	@Override
	public String resetCollectorIllnessData(Integer userId) {		
	
		MITAUser userOb = new MITAUser();
		userOb.setUser_id(userId);
		Boolean mitaStatus = mitaResetIllnessData(userOb);
		if(mitaStatus != null &&  mitaStatus) {
			List<Long> illnesses = illnessRepository.getAllTestIllnessIDs(userId);
			if(illnesses != null && ! illnesses.isEmpty()) {
				logger.info(" resetCollectorIllnessData Output::  " + illnesses);
				IllnessStatusModel illnessStatusModel = new IllnessStatusModel();
				illnessStatusModel.setIds(illnesses);
				mICAIllnessService.deleteIllnessesByIDS(illnessStatusModel);
			}
		
			return "success";
		} 
	
		return "failure";
	}

	
	
	@Override
	public String resetReviewerIllnessData(Integer userId) {		

		MITAUser userOb = new MITAUser();
		userOb.setUser_id(userId);
		Boolean mitaStatus = mitaResetIllnessData(userOb);
		if(mitaStatus != null &&  mitaStatus) {
			List<Long> illnesses = illnessRepository.updateIllness(userId);
			if(illnesses != null && ! illnesses.isEmpty()) {
		
				logger.info(" resetReviewerIllnessData Output::  " + illnesses);

			
		
			}
			
			return "success";
			} 
	
		return "failure";
	}
	
	
	private Boolean mitaResetIllnessData(MITAUser userOb){
		RestTemplate restTemplate = new RestTemplate();
		Boolean response = null;
		StringBuffer base_url =  new StringBuffer(mitaBaseUrl).append("/mita-rest/user/resetUserIllnesses");	
		logger.info("Mita URL :: " + base_url);
		if(userOb.getUser_id() != null) {
			try {
				response = restTemplate.postForObject(base_url.toString(),userOb, Boolean.class);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		
		}
		
		logger.info("Mita Response :: " + response);
		return response;

		
	
		
	}
	@Override
	public List<BodyParts> getPainSwellingBodyParts(String LanguageCode) {
		// TODO Auto-generated method stub
   	 List<GenericQueryResultEntity> sCategories = symptomCategoryRepository.getAllPainSwellingCategories(); 
  	List<BodyParts> painSwellingBodyParts = new ArrayList<BodyParts>();
   	 for (GenericQueryResultEntity sCategory : sCategories) {
   		 BodyParts  bodyParts = null;
   		SymptomCategory symptomCategory = sCategory.getsCategory();
   		 
   	   	 if(symptomCategory != null ) {
   	   		  bodyParts = new BodyParts();
   	          bodyParts.setGroupName(sCategory.getGroupName());
   	   		  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
   	   			  bodyParts.setEs_name(symptomCategory.getEs_name());
   	   			  bodyParts.setEs_position(symptomCategory.getEs_bodyLocations());
   	   		 }
   	   		  bodyParts.setName(symptomCategory.getName());
   	   		  bodyParts.setBodypartID(symptomCategory.getCode());
   	   		  bodyParts.setPosition(symptomCategory.getBodyLocations());
   	   		  List<String> sectionIDs = new ArrayList<String>();
   	   	       sectionIDs.add(sCategory.getSectionID());
   	          List<SymptomCategory> subBodyParts = symptomCategoryRepository.findPainSwellingBodyParts(sectionIDs);
              bodyParts.setSubParts(prepareSubParts(subBodyParts,symptomCategory.getCode(),LanguageCode));
   	    	 }
     	  painSwellingBodyParts.add(bodyParts);
	}
   	 
  
   	
   	
   	 
   	 
   	 
    	return painSwellingBodyParts;
	}
	@Override
	public BodyParts getAllCategoriesBodyparts(String LanguageCode) {
		// TODO Auto-generated method stub
		SymptomCategory symptomCategory= null;
		List<SymptomCategory> physicalCategories = symptomCategoryRepository.findAllBodyParts(); 
		if(physicalCategories != null ) {
			symptomCategory = physicalCategories.stream().filter(s->s.getParent()==null).findAny().orElse(null);
		}
		
    	 BodyParts  bodyParts = null;
    	 if(symptomCategory != null ) {
    		  bodyParts = new BodyParts();
    		  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
    			  bodyParts.setEs_name(symptomCategory.getEs_name());
    			  bodyParts.setEs_position(symptomCategory.getEs_bodyLocations());
    		 }
    		  bodyParts.setName(symptomCategory.getName());
    			  bodyParts.setId(symptomCategory.getOrderID());
    			  bodyParts.setParentID(symptomCategory.getOrderID());
    		  bodyParts.setPosition(symptomCategory.getBodyLocations());    		  
    		  List<String> sectionIDs = new ArrayList<String>();
  	   	       sectionIDs.add("SC001");
  	   	       sectionIDs.add("SC002");  	   	  
  	          List<SymptomCategory> painSwellingCategories = symptomCategoryRepository.findPainSwellingBodyParts(sectionIDs);
  	          String bodyPartCode = symptomCategory.getCode();
  	          List<String> bodypartMap = painSwellingCategories.stream().filter(s->s.getBodypartCode().equalsIgnoreCase(bodyPartCode)).map(SymptomCategory::getCode).collect(Collectors.toList());
  	          bodypartMap.add(bodyPartCode);
  	         bodyParts.setBodyPartsCodes(bodypartMap);
  	         bodyParts.setSubParts(mapAllSubParts(physicalCategories,painSwellingCategories,bodyPartCode,LanguageCode,symptomCategory.getOrderID()));
     	 }
     	return bodyParts;
	}
	
	private List<BodyParts> mapAllSubParts(List<SymptomCategory> physicalCategories,   List<SymptomCategory> painSwellingCategories , String parent,String LanguageCode, Integer parentId) {		
		List<BodyParts> subParts = new ArrayList<BodyParts>();		
		List<SymptomCategory> listParts =	physicalCategories.stream().filter(s->s.getParent() != null && s.getParent().equals(parent)).collect(Collectors.toList());
		if(listParts != null && ! listParts.isEmpty()){			
			for (int i = 0; i < listParts.size(); i++) {
				SymptomCategory st =	listParts.get(i);
				if(st != null ) {
					  BodyParts  bodyParts = new BodyParts();
					  if(MICAConstants.SPANISH.equals(LanguageCode)) {    			 
		    			  bodyParts.setEs_name(st.getEs_name());
		    			  bodyParts.setEs_position(st.getEs_bodyLocations());
		    		 }
					  bodyParts.setName(st.getName());
		    		 // bodyParts.setBodypartID(st.getCode());
		    		  bodyParts.setPosition(st.getBodyLocations());
		    		
		    		  String bodyPartCode = st.getCode();
		    		  List<String> bodypartMap = painSwellingCategories.stream().filter(s->s.getBodypartCode().equalsIgnoreCase(bodyPartCode)).map(SymptomCategory::getCode).collect(Collectors.toList());
		  	          bodypartMap.add(bodyPartCode);
		  	          bodyParts.setBodyPartsCodes(bodypartMap);
		  	          bodyParts.setId(st.getOrderID());
		  	          bodyParts.setParentID(parentId);
		    		  if(st.getParent() !=null) {
		    		  bodyParts.setSubParts(mapAllSubParts(physicalCategories,painSwellingCategories,bodyPartCode,LanguageCode,st.getOrderID()));
		    		  }
		    		  subParts.add(bodyParts);
				}
				
			}
			
		}
	
		return subParts;
	}
}
