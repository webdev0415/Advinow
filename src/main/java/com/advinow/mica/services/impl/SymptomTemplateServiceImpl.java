package com.advinow.mica.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.SymptomConfig;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.mapper.SymptomTemplateMapper;
import com.advinow.mica.model.SymptomType;
import com.advinow.mica.repositories.DataKeysRepository;
import com.advinow.mica.repositories.DataStoreRepository;
import com.advinow.mica.repositories.SymptomConfigRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.SymptomTemplateService;

@Service
@Retry(name = "neo4j")
public class SymptomTemplateServiceImpl implements SymptomTemplateService {
	
	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;
	
	SymptomTemplateMapper  symptomTemplateMapper = new SymptomTemplateMapper();
	
	@Autowired
	DataStoreRepository  dataStoreRepository;
	
	@Autowired
	DataKeysRepository   dataKeysRepository;
	
	@Autowired
	SymptomConfigRepository symptomConfigRepository;

	@Override
	public List<SymptomType> getSymptomsTemplatesByGroup(String groupId) {
	        	 Set<SymptomTemplate> symptomsTemplates = symptomTemplateRepository.getAllSymptomTemplatesForGroup(groupId);
	        	 if(symptomsTemplates.isEmpty()){
	        		 symptomsTemplates = symptomTemplateRepository.getAllSymptomTemplatesForPainGroup(groupId);
	        	 }
		         Iterable<DataKeys> dataKeys = dataKeysRepository.findAll(1);
		         List<SymptomType>  templates = symptomTemplateMapper.prepareSymptomTemplates(symptomsTemplates,dataKeys);
		
		return templates;
	}

	@Override
	public List<SymptomType> getPatientBioSymptoms() {
		   Iterable<DataKeys> dataKeys = dataKeysRepository.findAll(1);
		   SymptomConfig  symptomConfig=  symptomConfigRepository.getSymptomConfig("PATIENTBIO");
			   Set<SymptomTemplate> symptomsTemplates =   symptomTemplateRepository.getPatientBioSymptoms(symptomConfig.getIncludeSymptoms());
		    List<SymptomType>  templates = symptomTemplateMapper.prepareSymptomTemplates(symptomsTemplates,dataKeys);
		return templates;
	}
	
	
	@Override
	public List<SymptomType> getSymptomTemplates(String groupId) {
	        	 Set<SymptomTemplate> symptomsTemplates = symptomTemplateRepository.getAllSymptomTemplatesForGroup(groupId);
	        	  Iterable<DataKeys> dataKeys = dataKeysRepository.findAll(1);
		         List<SymptomType>  templates = symptomTemplateMapper.prepareSymptomTemplates(symptomsTemplates,dataKeys);
		
		return templates;
	}


	@Override
	public List<SymptomType> getFamilySocialHistorySmptoms() {
		   Iterable<DataKeys> dataKeys = dataKeysRepository.findAll(1);
		   SymptomConfig  symptomConfig=  symptomConfigRepository.getSymptomConfig("FSHISTORY");
		   Set<SymptomTemplate> symptomsTemplates =   symptomTemplateRepository.getFamilySocialHistorySmptoms(symptomConfig.getIncludeCategories(),symptomConfig.getIgnoreSymptoms());
		    List<SymptomType>  templates = symptomTemplateMapper.prepareSymptomTemplates(symptomsTemplates,dataKeys);
		return templates;
	}

}
