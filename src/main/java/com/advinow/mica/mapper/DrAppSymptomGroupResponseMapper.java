package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;
import com.advinow.mica.domain.Section;
import com.advinow.mica.domain.SnomedCodes;
import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomGroup;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.SectionModel;
import com.advinow.mica.model.SnomedCodesModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.Symptoms;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class DrAppSymptomGroupResponseMapper {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	Set<DataKeys> 	dataKeys= null;	

	Set<SymptomTemplate> 	snomedSymptomKeys  = null;
	
	List<GenericQueryResultEntity> logicalGroups= null; 
	
	List<GenericQueryResultEntity> deGroups = null;
	/**
	 * Prepares the response message for the given group name.
	 * 
	 * @param symptomGroup
	 * @param dataKeysRepository
	 * @param rCodeDataKeyRespository
	 * @param dataStoreRepository
	 * @return
	 */
	public SymptomGroups prepareSymptomsGroupByName(SymptomGroup symptomGroup, Set<DataKeys> 	dataKeys ,Set<SymptomTemplate> 	snomedSymptomKeys,List<GenericQueryResultEntity> logicalGroups, List<GenericQueryResultEntity> deGroups ) {
		this.dataKeys = dataKeys;
		this.snomedSymptomKeys = snomedSymptomKeys;
		this.logicalGroups =logicalGroups;
	    this.deGroups = deGroups;
		// TODO Auto-generated method stub
		Set<String> dataStoreRefList= new HashSet<String>();
		SymptomGroups group= new SymptomGroups();
		if(symptomGroup.getName() !=null ){
			group.setName(symptomGroup.getName());
		}
		group.setGroupID(symptomGroup.getGroupID());
		Set<SymptomCategory> resCategoryList = symptomGroup.getCategories();     	
		if ( resCategoryList != null && ! resCategoryList.isEmpty()  ){
			group.setCategories(createCategories(resCategoryList));
		}

		Set<Section> sections = symptomGroup.getSections();
		if(sections != null  &&  !sections.isEmpty() ){
			group.setSections(createSections(sections,dataStoreRefList));
		}
		group.setUpdatedDate(symptomGroup.getUpdatedDate());
		
		return group;
	}
	
	
	/*public SymptomGroups prepareSymptomsGroupByName(SymptomGroup symptomGroup, Set<DataKeys> 	dataKeys ) {
		this.dataKeys = dataKeys;
		

		// TODO Auto-generated method stub
		Set<String> dataStoreRefList= new HashSet<String>();
		SymptomGroups group= new SymptomGroups();
		if(symptomGroup.getName() !=null ){
			group.setName(symptomGroup.getName());
		}
		group.setGroupID(symptomGroup.getGroupID());
		Set<SymptomCategory> resCategoryList = symptomGroup.getCategories();     	
		if ( resCategoryList != null && ! resCategoryList.isEmpty()  ){
			group.setCategories(createCategories(resCategoryList));
		}

		Set<Section> sections = symptomGroup.getSections();
		if(sections != null  &&  !sections.isEmpty() ){
			group.setSections(createSections(sections,dataStoreRefList));
		}
		return group;
	}*/

	private List<SectionModel> createSections(Set<Section> sections,
			Set<String> dataStoreRefList) {
		List<SectionModel> modelSections = new ArrayList<SectionModel>();
		Iterator<Section> dbSectionItr = sections.iterator();
		while(dbSectionItr.hasNext()){
			Section dbSection = dbSectionItr.next();
			SectionModel sectionModel = new SectionModel();
			sectionModel.setSectionID(dbSection.getCode());
			sectionModel.setName(dbSection.getName());
			Set<SymptomCategory> sectionCategories = dbSection.getSymptomCategories();
			if(sectionCategories != null && ! sectionCategories.isEmpty() ) {
				sectionModel.setCategories(createCategories(sectionCategories));
			}

			modelSections.add(sectionModel);
		}
		return modelSections;
	}

	private List<CategoryModel> createCategories(Set<SymptomCategory> sysmptomCategoryList) {
		List<CategoryModel> resCategoryList= new ArrayList<CategoryModel>();
		Iterator<SymptomCategory> symtomIt = sysmptomCategoryList.iterator();
		while (symtomIt.hasNext()) {
			SymptomCategory symptomCategory = symtomIt.next();
			CategoryModel category = new CategoryModel();		
			category.setName(symptomCategory.getName());
			category.setCategoryID(symptomCategory.getCode());
			category.setSymptoms(createSymptomps(symptomCategory));
			resCategoryList.add(category);
		}
		return resCategoryList;
	}


	private List<Symptoms> createSymptomps(SymptomCategory symptomCategory) {
		List<Symptoms>  responseSymptoms = new ArrayList<Symptoms>();
		Set<SymptomTemplate> symptomTemplateList = symptomCategory.getSymptomTemplates();
		if(symptomTemplateList != null){
			Iterator<SymptomTemplate> symptomTmpItr = symptomTemplateList.iterator();
			while(symptomTmpItr.hasNext()){
				SymptomTemplate symptomTemplate =	symptomTmpItr.next();
				responseSymptoms.add(createSymtoms(symptomTemplate));
			}
		}
		return responseSymptoms;
	}


/*
	private Symptoms createSymtoms(SymptomTemplate symptomTemplate) {
		Symptoms symptoms = new Symptoms();
		symptoms.setName(symptomTemplate.getName());
		symptoms.setSymptomID(symptomTemplate.getCode());
		symptoms.setCriticality(symptomTemplate.getCriticality());		
		DataKeys datakey = dataKeys.stream().filter(s->s.getName().equalsIgnoreCase(symptomTemplate.getMultipleValues())).findAny().orElse(null);
		Set<DataStore> dataStores = null;
		if(datakey != null ) {
			dataStores = 	datakey.getDataStoreList();
		}
		SnomedSymptomMap snomedSymptomMap = snomedSymptomKeys.stream().filter(s->s.getCode().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);
		symptoms.setSnomedCodes(getSnomedCodes(snomedSymptomMap,dataStores));
		return symptoms;
	}
	*/
	private Symptoms createSymtoms(SymptomTemplate symptomTemplate) {
		Symptoms symptoms = new Symptoms();
		symptoms.setName(symptomTemplate.getName());
		symptoms.setSymptomID(symptomTemplate.getCode());
		symptoms.setCriticality(symptomTemplate.getCriticality());		
		symptoms.setDisplayDrApp(symptomTemplate.getDisplayDrApp());
		symptoms.setGenderGroup(symptomTemplate.getGenderGroup());
		symptoms.setLogicalGroupNames(getLogicalGroupNames(symptomTemplate));
		symptoms.setDeGroups(getDeGroupNames(symptomTemplate));
		DataKeys datakey = dataKeys.stream().filter(s->s.getName().equalsIgnoreCase(symptomTemplate.getMultipleValues())).findAny().orElse(null);
		Set<DataStore> dataStores = null;
		if(datakey != null ) {
			dataStores = 	datakey.getDataStoreList();
		}
		SymptomTemplate snomedTemplate = snomedSymptomKeys.stream().filter(s->s.getCode().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);
		symptoms.setSnomedCodes(getSnomedCodes(snomedTemplate,dataStores));
		return symptoms;
	}


	/*private Set<SnomedCodesModel> getSnomedCodes(SnomedSymptomMap snomedSymptomMap ,
			Set<DataStore> dataStores) {
		Set<SnomedCodesModel>  snomedModelCodes = new HashSet<SnomedCodesModel>();
		if(dataStores != null  &&  !dataStores.isEmpty()) {
			for (DataStore dataStore : dataStores) {	
				SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
		 		snomedCodesModel.setListValueCode(dataStore.getCode());
				snomedCodesModel.setListValue(dataStore.getName());
				snomedModelCodes.add(snomedCodesModel);
			}
			populateSnomedCodes(snomedModelCodes,snomedSymptomMap);
			
		} else if(snomedSymptomMap != null && snomedSymptomMap.getSnomedCodes() != null && ! snomedSymptomMap.getSnomedCodes().isEmpty()) {
			for (SnomedCodes dbSnomedCodes : snomedSymptomMap.getSnomedCodes()) {
				SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
				snomedCodesModel.setCode(dbSnomedCodes.getCode());
				snomedCodesModel.setName(dbSnomedCodes.getName());
				snomedModelCodes.add(snomedCodesModel);
			}
		}
		return snomedModelCodes;
	}*/
	
	
	private Set<SnomedCodesModel> getSnomedCodes(SymptomTemplate snomedSymTemplate ,
			Set<DataStore> dataStores) {
		Set<SnomedCodesModel>  snomedModelCodes = new HashSet<SnomedCodesModel>();
		if(dataStores != null  &&  !dataStores.isEmpty()) {
			for (DataStore dataStore : dataStores) {	
				SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
		 		snomedCodesModel.setListValueCode(dataStore.getCode());
				snomedCodesModel.setListValue(dataStore.getName());
				snomedModelCodes.add(snomedCodesModel);
			}
			populateSnomedCodes(snomedModelCodes,snomedSymTemplate);
			
		} else if(snomedSymTemplate != null && snomedSymTemplate.getSnomedCodes() != null && ! snomedSymTemplate.getSnomedCodes().isEmpty()) {
			for (SnomedCodes dbSnomedCodes : snomedSymTemplate.getSnomedCodes()) {
				SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
				snomedCodesModel.setCode(dbSnomedCodes.getCode());
				snomedCodesModel.setName(dbSnomedCodes.getName());
				snomedModelCodes.add(snomedCodesModel);
			}
		}
		return snomedModelCodes;
	}

/*	private void populateSnomedCodes(Set<SnomedCodesModel> snomedModelCodes,
			SnomedSymptomMap snomedSymptomMap) {
		Set<SnomedCodes> snomedCodes  = null;
		if(snomedSymptomMap !=null) {
             snomedCodes = snomedSymptomMap.getSnomedCodes();
		}
		if(snomedCodes != null  &&  !snomedCodes.isEmpty()) {
			for (SnomedCodes snomed : snomedCodes) {	
				  if(snomed.getListValueCode() != null)   {
					   SnomedCodesModel  snomedCodesModel =  snomedModelCodes.stream().filter(s->s.getListValueCode()!= null && s.getListValueCode().equalsIgnoreCase(snomed.getListValueCode())).findAny().orElse(null);
					  if(snomedCodesModel != null) {
						    snomedCodesModel.setCode(snomed.getCode());
							snomedCodesModel.setName(snomed.getName());  
					  }
				  } else{
					    SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
						snomedCodesModel.setCode(snomed.getCode());
						snomedCodesModel.setName(snomed.getName());
					  snomedModelCodes.add(snomedCodesModel);
				  }
				
			}
		}
		
	}*/
	
	
	
	private void populateSnomedCodes(Set<SnomedCodesModel> snomedModelCodes,
			SymptomTemplate symptomTemplate) {
		Set<SnomedCodes> snomedCodes  = null;
		if(symptomTemplate !=null) {
             snomedCodes = symptomTemplate.getSnomedCodes();
		}
		if(snomedCodes != null  &&  !snomedCodes.isEmpty()) {
			for (SnomedCodes snomed : snomedCodes) {	
				  if(snomed.getListValueCode() != null)   {
					   SnomedCodesModel  snomedCodesModel =  snomedModelCodes.stream().filter(s->s.getListValueCode()!= null && s.getListValueCode().equalsIgnoreCase(snomed.getListValueCode())).findAny().orElse(null);
					  if(snomedCodesModel != null) {
						    snomedCodesModel.setCode(snomed.getCode());
							snomedCodesModel.setName(snomed.getName());  
					  }
				  } else{
					    SnomedCodesModel snomedCodesModel = new SnomedCodesModel();
						snomedCodesModel.setCode(snomed.getCode());
						snomedCodesModel.setName(snomed.getName());
					  snomedModelCodes.add(snomedCodesModel);
				  }
				
			}
		}
		
	}
	
	private List<String>  getLogicalGroupNames(SymptomTemplate symptomTemplate) {	
		GenericQueryResultEntity groupSymptom = logicalGroups.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);
		if(groupSymptom != null) {
		return groupSymptom.getLogicalGroupNames();
		}
		
		return null;

	}
	
	private List<String>  getDeGroupNames(SymptomTemplate symptomTemplate) {	
		GenericQueryResultEntity groupSymptom = deGroups.stream().filter(s->s.getSymptomID().equalsIgnoreCase(symptomTemplate.getCode())).findAny().orElse(null);
		if(groupSymptom != null) {
		return groupSymptom.getDeGroupNames();
		}
		
		return null;

	}
}
