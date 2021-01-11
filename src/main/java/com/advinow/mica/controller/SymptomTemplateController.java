/**
 * 
 */
package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.model.SymptomType;
import com.advinow.mica.services.SymptomTemplateService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/symptomtemplates")
@Api(value="/symptomtemplates", description="SymptomTemplate services for  third part clients")
public class SymptomTemplateController {
	
	
	@Autowired
	SymptomTemplateService symptomTemplateService;
	
    /**
     * Returns symptom type, list name and list values for the given group id
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomType>> getSymptomsTemplatesByGroup(@PathVariable("groupId") String groupId) {
		List<SymptomType> symptomtemplates = symptomTemplateService.getSymptomsTemplatesByGroup(groupId);
	   return new ResponseEntity<>(symptomtemplates, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/patient", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomType>> getPatientBioSymptoms() {
		List<SymptomType> symptomtemplates = symptomTemplateService.getPatientBioSymptoms();
	   return new ResponseEntity<>(symptomtemplates, HttpStatus.OK);
	}
	/**
	 * 
	 * @return
	 */
    @RequestMapping(value = "/measurements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomType>> getMeasurementsSymptoms() {
		List<SymptomType> symptomtemplates = symptomTemplateService.getSymptomTemplates("measurements");
	   return new ResponseEntity<>(symptomtemplates, HttpStatus.OK);
	}
    
    /**
     * 
     * @return
     */
	@RequestMapping(value = "/personalfamily", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomType>> getFamilySocialHistorySmptoms() {
		List<SymptomType> symptomtemplates = symptomTemplateService.getFamilySocialHistorySmptoms();
	   return new ResponseEntity<>(symptomtemplates, HttpStatus.OK);
	}

}
