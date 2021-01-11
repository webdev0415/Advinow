/**
 * 
 */
package com.advinow.mica.mapper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.Category;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomDataStore;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.util.MICAConstants;

/**
 * @author Govinda Reddy
 *
 */
public class IllnessVersionMergeMapper {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	
	SymptomTemplateRepository symptomTemplateRepository; 

	public void mergeIllnessData(Illness dbIllness, Illness illnessModel,	SymptomTemplateRepository symptomTemplateRepository) {
		  this.symptomTemplateRepository = symptomTemplateRepository;
	        dbIllness.setVersion(null);
	        dbIllness.setState("FINAL");
	        dbIllness.setMergeCount( dbIllness.getMergeCount()+1);	      
	        dbIllness.getMergedVersions().add(illnessModel.getVersion().intValue());
	        dbIllness.setGroupsComplete(mergeGroups( dbIllness,  illnessModel));  
	        dbIllness.setCategories(mergeCategories(dbIllness,illnessModel));
	        
	}


	/**
	 * 
	 * @param dbIllness
	 * @param illnessModel
	 * @return
	 */
	private Set<Category> mergeCategories(Illness dbIllness,
			Illness illnessModel) {	
 	     //  List<String> categoryCodes = dbIllness.getCategories().stream().map(e->e.getCode()).collect(Collectors.toList());
		Set<Category> finalCategories =  dbIllness.getCategories();
	     Set<Category> modelCategories = illnessModel.getCategories();		
	 	Iterator<Category> modelCategoryItr = modelCategories.iterator();
		while (modelCategoryItr.hasNext()) {
			Category modelCategory = modelCategoryItr.next();	
			if(modelCategory != null ) {
			Category dbCategory = finalCategories.stream().filter(s->s.getCode().equalsIgnoreCase(modelCategory.getCode())).findAny().orElse(null);
			// if category not in the database, add the new category to the existing list
			if(dbCategory == null ) {
				finalCategories.add(modelCategory);			
			} else{
				// if category exists then merge the symptoms
				mergeCategory(dbCategory,modelCategory);
				
			}
			
			}
							
		}
		
		
		return finalCategories;
	}

	private void mergeCategory(Category dbCategory, Category modelCategory) {		
		Set<Symptom> finalSymptoms = dbCategory.getSymptoms();		
		Set<Symptom> modelSymptoms = modelCategory.getSymptoms();
		  Iterator<Symptom> modelSymptomItr = modelSymptoms.iterator();
		while (modelSymptomItr.hasNext()) {
			   Symptom modelSymptom = modelSymptomItr.next();	
			   if(modelSymptom != null ) {				
					 Symptom dbSymptom = finalSymptoms.stream().filter(s->s.getCode().equalsIgnoreCase(modelSymptom.getCode())).findAny().orElse(null);
					 if(dbSymptom == null) {						 
						 finalSymptoms.add(modelSymptom);
					 } else {						 
						 mergeSymptoms(dbSymptom,modelSymptom);
					 }
					 
			}
		
		}
	}


	private void mergeSymptoms(Symptom dbSymptom, Symptom modelSymptom) {
		dbSymptom.setBodyParts(mergeBodyParts(dbSymptom,modelSymptom));
		SymptomTemplate SymptomTemplate = symptomTemplateRepository.findBySymptomCode(modelSymptom.getCode().toUpperCase());
		if(SymptomTemplate != null) {			
			String symptomType = getSymptomType(SymptomTemplate);			
			logger.info("Symptom type::  "+ symptomType);		
			if(symptomType.equalsIgnoreCase(MICAConstants.SIMPLE)) {
				 SymptomDataStore finalDataStores= dbSymptom.getSymptomDataStore().stream().findFirst().get();
				 SymptomDataStore  modelDataStore =   modelSymptom.getSymptomDataStore().stream().findFirst().get();
				 if(finalDataStores == null) {
				Set<SymptomDataStore> dbDataStores = dbSymptom.getSymptomDataStore();
				 dbDataStores.add(modelDataStore);
				 }else {
				 mergeSymptomDataStore(finalDataStores,modelDataStore);
				 }
			}
			if(symptomType.equalsIgnoreCase(MICAConstants.LIST)) {
				mergeListSymptom(dbSymptom,  modelSymptom);
			}
			
			if(symptomType.equalsIgnoreCase(MICAConstants.SIMPLE_TIME)) {
				mergeSimpleTime(dbSymptom,  modelSymptom);
			}
			
            if(symptomType.equalsIgnoreCase(MICAConstants.LIST_TIME)) {
				
			}
			
		}
		
		
	}

  private void mergeSimpleTime(Symptom dbSymptom, Symptom modelSymptom) {
		
	}


private void mergeListSymptom(Symptom dbSymptom, Symptom modelSymptom) {
	  
	 Set<SymptomDataStore> finalDataStores = dbSymptom.getSymptomDataStore();
	 Set<SymptomDataStore> modelDataStores = modelSymptom.getSymptomDataStore();
	 Iterator<SymptomDataStore> modelDsItr = modelDataStores.iterator();
		while (modelDsItr.hasNext()) {
			SymptomDataStore modelSymptomDataStore= modelDsItr.next();	
			if(modelSymptomDataStore != null ) {
				SymptomDataStore dbSymptomDataStore = finalDataStores.stream().filter(s->s.getMultiplier().containsAll(modelSymptomDataStore.getMultiplier())).findAny().orElse(null);
			if(dbSymptomDataStore == null ) {
				finalDataStores.add(modelSymptomDataStore);
			} else {
				mergeSymptomDataStore(dbSymptomDataStore,modelSymptomDataStore);
			}
			
			}
		}
	}


private void mergeSymptomDataStore(SymptomDataStore finalDataStores,
		SymptomDataStore modelDataStore) {
	 finalDataStores.setBias(modelDataStore.getBias());
//	 finalDataStores.setLikelihood(mergeSimpleTypeLikelihoods(finalDataStores.getLikelihood(),finalDataStores.getMergeCount(),modelDataStore.getLikelihood()));
	 finalDataStores.setMergeCount(finalDataStores.getMergeCount()+1);
}


private List<String> mergeBodyParts(Symptom dbSymptom, Symptom modelSymptom) { 
	 List<String> finalBodyParts = dbSymptom.getBodyParts() ;
	 List<String> modelBodyParts = modelSymptom.getBodyParts();
	 List<String> bodyparts = null; 
	
	 if(finalBodyParts != null && modelBodyParts != null ) {
	  bodyparts = Stream.concat(finalBodyParts.stream(), modelBodyParts.stream())
               .distinct()
               .collect(Collectors.toList()); 
	 } else{
		 if(finalBodyParts==null){
			 
			 bodyparts = modelBodyParts;
		 }
		 
          if(modelBodyParts==null){
			 
			 bodyparts = finalBodyParts;
		 }
		 
	 }
	  	return bodyparts;
	}




	private String getSymptomType(SymptomTemplate symptomTemplate) {
		// TODO Auto-generated method stub
		String type= null;
		
	     String	multiplierKey = symptomTemplate.getMultipleValues();
	  	 List<String> dataStoreValues = symptomTemplate.getDataStoreTemplates();
	  	 
	  	 
	  	if(multiplierKey != null ) {
     			if(dataStoreValues.contains(multiplierKey)  &&  dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE_TIME)){
     				type  =MICAConstants.LIST_TIME;
     			} else if(dataStoreValues.contains(multiplierKey)  &&  dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE)){
     				type = MICAConstants.LIST;
     			}
     		} else if(dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE_TIME)) {
     			type = MICAConstants.SIMPLE_TIME;
     			
     		} else{
     			type =MICAConstants.SIMPLE;
     		}
     	 
	     		
		return type;
	}


	/**
	 * Merge list of completed groups with Final illness
	 * 
	 * @param dbIllness
	 * @param illnessModel
	 * @return List<String>
	 */
	private List<String> mergeGroups(Illness dbIllness,
			Illness illnessModel) { 
			   List<String> groups = Stream.concat(dbIllness.getGroupsComplete().stream(), illnessModel.getGroupsComplete().stream())
                .distinct()
                .collect(Collectors.toList()); 
	  	return groups;
	}
	
}
