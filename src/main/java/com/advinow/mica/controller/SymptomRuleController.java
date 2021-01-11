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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.SymptomRule;
import com.advinow.mica.domain.queryresult.SymptomRuleModel;
import com.advinow.mica.model.MICAResponse;
import com.advinow.mica.services.SymptomRuleService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/rules")
@Api(value="/rules", description="Symptom linking managment")
public class SymptomRuleController {
	
	@Autowired
	SymptomRuleService symptomRuleService;
	
	/**
	 * Returs all the rules.
	 * 
	 * @return List<SymptomRuleModel>
	 */
	@RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomRuleModel>> getAllRules() {
		List<SymptomRuleModel> sources = symptomRuleService.getAllRules();
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	/**
	 * Creates new rule
	 * 
	 * @param SymptomRuleModel
	 * @return SymptomRuleModel
	 */
	@RequestMapping(method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MICAResponse> createSymptomRules(@RequestBody SymptomRuleModel symptomRuleModel ) {
		 SymptomRule rule = symptomRuleService.createSymptomRules(symptomRuleModel);
		 
		 MICAResponse res = new MICAResponse();
		 if(rule != null ) {
		 res.setStatus("Rule created Successfully");
		 }
	   return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param ruleID
	 * @return String
	 */
	@RequestMapping(value = "/{ruleID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteRule(@PathVariable("ruleID") Integer ruleID) {
	        symptomRuleService.delete(ruleID);
	    String    str ="Rule deleted Successfully";
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{ruleID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomRuleModel> getRoleByID(@PathVariable("ruleID") Integer ruleID) {
		SymptomRuleModel rule = symptomRuleService.getRoleByID(ruleID);
	   return new ResponseEntity<>(rule, HttpStatus.OK);
	}
	
}
