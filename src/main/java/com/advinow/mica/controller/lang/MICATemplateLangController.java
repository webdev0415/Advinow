/**
 * 
 */
package com.advinow.mica.controller.lang;

import java.util.List;

//import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.model.GenericSymptomGroups;
import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.SymptomTemplateModel;
import com.advinow.mica.model.SymptomTemplateModelV1;
import com.advinow.mica.model.SymptomTemplateValidate;
import com.advinow.mica.services.MICATemplateService;
import com.advinow.mica.util.MICAConstants;

/**
 * 
 * 
 * @author Govinda Reddy
 *
 */

@RestController
@RequestMapping("{languagecode}/template")
//@Api(value="/template", description="SymptomTemplate service for  spanish translation")
public class MICATemplateLangController {

	@Autowired
	MICATemplateService mICATemplateService;

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/symptomgroups/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomGroups> getSymtomsByGroup(@PathVariable("groupId") String groupId,@PathVariable("languagecode") String languagecode) {

		if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
			throw new DataNotFoundException(languagecode + " Language not supported... ");
		}
		SymptomGroups  responseSymptomGroup = mICATemplateService.getSymptomsByGroup(groupId,languagecode);	
		return new ResponseEntity<>(responseSymptomGroup, HttpStatus.OK);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/symptomgroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MICASymptomsGroup  getSymtomGroups(@PathVariable("languagecode") String languagecode) {

		if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
			throw new DataNotFoundException(languagecode + " Language not supported... ");
		}
		return	mICATemplateService.getSymtomGroups(languagecode);

	}
	
	  /**
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/symptomsByCategory/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericSymptomGroups> getSymtomsByUser(@PathVariable("groupId") String groupId,@PathVariable("languagecode") String languagecode) {
		GenericSymptomGroups  responseSymptomGroup = mICATemplateService.symptomsByCategory(groupId,languagecode);	
	   return new ResponseEntity<>(responseSymptomGroup, HttpStatus.OK);
	}
	
	/**
	 *  
	 * Accepts a SymptomID and returns the following Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean). 
	 * 
	 * @param groupId
	 * @return
	 **/
	@RequestMapping(value = "/symptoms/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModel> getSymptomByID(@PathVariable("symptomID") String symptomID,@PathVariable("languagecode") String languagecode) {		
		if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
			throw new DataNotFoundException(languagecode + " Language not supported... ");
		}
		SymptomTemplateModel  symptom = mICATemplateService.getSymptomByID(symptomID,languagecode);	
		return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	/* 
     * Accepts a SymptomID and returns the following Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean). 
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/v1/symptoms/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModelV1> getSymptomByIDV1(@PathVariable("symptomID") String symptomID,@PathVariable("languagecode") String languagecode) {
		SymptomTemplateModelV1  symptom = mICATemplateService.getSymptomByIDV1(symptomID,languagecode);	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	/**
	 * Accepts a Symptom ID and updates the Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean) and ic10RCodes for illness data and symptoms templates.
	 * @param symptomTemplate
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/symptoms", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModel> updateSymptom(@RequestBody SymptomTemplateModel symptomTemplate,@PathVariable("languagecode") String languagecode) {
		SymptomTemplateModel  symptom = mICATemplateService.updateSymptom(symptomTemplate,languagecode);	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	/**
	 * Accepts a Symptom ID and updates the Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean) and ic10RCodes for illness data and symptoms templates.
	 * @param symptomTemplate
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/v1/symptoms", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModelV1> updateSymptomV1(@RequestBody SymptomTemplateModelV1 symptomTemplate,@PathVariable("languagecode") String languagecode) {
		SymptomTemplateModelV1  symptom = mICATemplateService.updateSymptomV1(symptomTemplate,languagecode);	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/symptoms/validate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomTemplateValidate>> validate(@PathVariable("languagecode") String languagecode) {
		List<SymptomTemplateValidate>  symptom = mICATemplateService.validateTemplates(languagecode);
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	

}
