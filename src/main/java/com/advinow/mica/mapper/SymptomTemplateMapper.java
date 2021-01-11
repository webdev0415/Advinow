package com.advinow.mica.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.model.SymptomType;
import com.advinow.mica.util.MICAConstants;
import com.advinow.mica.util.MICAUtil;

public class SymptomTemplateMapper {

	//public  List<SymptomType> prepareSymptomTemplates(Set<SymptomTemplate> symptomsTemplates,DataStoreRepository  dataStoreRepository) {
	public  List<SymptomType> prepareSymptomTemplates(Set<SymptomTemplate> symptomsTemplates,  Iterable<DataKeys> dataKeys) {
		List<SymptomType> symptomTemplates = new ArrayList<SymptomType>();
		
		 Map<String, Map<String, List<String>>> dataStoreMap = MICAUtil.populateDataStore(dataKeys);
		
		if(symptomsTemplates != null && ! symptomsTemplates.isEmpty()) {
			Iterator<SymptomTemplate> templateItr = symptomsTemplates.iterator();
			while(templateItr.hasNext()) {
			SymptomTemplate dbTemplate = templateItr.next();
			SymptomType symptomModel = new SymptomType();
			symptomModel.setSymptomID(dbTemplate.getCode());
			symptomModel.setName(dbTemplate.getName());
			symptomModel.setImage(dbTemplate.getDescriptorFile());
			symptomModel.setRange(dbTemplate.getRangeValues());
			symptomModel.setUpdatedDate(dbTemplate.getUpdatedDate());
		     String	multiplierKey = dbTemplate.getMultipleValues();
			if(!StringUtils.isEmpty(multiplierKey)){
	  		Map<String, List<String>> multiplierMap = dataStoreMap.get(multiplierKey);
	       if(multiplierMap != null) {
	  		    String key = multiplierMap.keySet().iterator().next();
	  			symptomModel.setListName(key);
				symptomModel.setListValues(multiplierMap.get(key));		
	  		  }}
				List<String> dataStoreValues = dbTemplate.getDataStoreTemplates();
	        	 if(dataStoreValues != null && ! dataStoreValues.isEmpty()){
	        		if(multiplierKey != null ) {
	        			if(dataStoreValues.contains(multiplierKey)  &&  dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE_TIME)){
	        				symptomModel.setSymptomType(MICAConstants.LIST_TIME);
	        			} else if(dataStoreValues.contains(multiplierKey)  &&  dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE)){
	        				symptomModel.setSymptomType(MICAConstants.LIST);
	        			}
	        		} else if(dataStoreValues.containsAll(MICAConstants.DATA_STORE_SIMPLE_TIME)) {
	        			symptomModel.setSymptomType(MICAConstants.SIMPLE_TIME);
	        			
	        		} else{
	        			symptomModel.setSymptomType(MICAConstants.SIMPLE);
	        		}
	        	 }
	     
			symptomTemplates.add(symptomModel);
			}
		}
		return symptomTemplates;
	}
	
	
	

}
