package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.SymptomDictionary;
import com.advinow.mica.services.DrAppTemplateService;


@RestController
@RequestMapping("/drapp/template")
@Api(value="/drapp", description="API for android and dr applications")
public class DRAppTemplateController {
	
	@Autowired
	DrAppTemplateService drAppTemplateService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/symptomgroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MICASymptomsGroup  getSymtomGroups() {
	return	drAppTemplateService.getSymtomGroups();
		
	}
	
	/**
	 * Returns the symptom template dicitonary with all descriptors and  snomed code dictionary for templates with descriptors.
	 * 
	 * 
	 * @return SymptomDictionary
	 */
	@RequestMapping(value = "/symptomdictionaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SymptomDictionary  getSymptomDictionaries() {
	return	drAppTemplateService.getSymptomDictionaries();
		
	}
	

		

}
