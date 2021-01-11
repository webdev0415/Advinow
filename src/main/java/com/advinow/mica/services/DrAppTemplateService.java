package com.advinow.mica.services;

import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.SymptomDictionary;

/**
 * 
 * @author Govinda Reddy
 *
 */
public interface DrAppTemplateService {
	
	public MICASymptomsGroup getSymtomGroups();

	public SymptomDictionary getSymptomDictionaries();
	

	
		
}
