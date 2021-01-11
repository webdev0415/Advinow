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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.services.SymptomSourceInfoService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/symptomsources")
@Api(value="/symptomsources", description="Maintain source information")
public class SymptomSourceController {
	
	@Autowired
	SymptomSourceInfoService symptomSourceInfoService;
	
	/**
	 * Returs all the symptom sources.
	 * 
	 * @return List<SymptomSourceInfo>
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomSourceInfo>> getSourceSymptoms() {
		List<SymptomSourceInfo> sources = symptomSourceInfoService.getSourceSymptoms();
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	/**
	 * Creates new source
	 * 
	 * @param symptomSource
	 * @return SymptomSourceInfo
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomSourceInfo> getSourceSymptoms(@RequestBody SymptomSourceInfo symptomSource ) {
		SymptomSourceInfo sources = symptomSourceInfoService.createSourceSymptom(symptomSource);
	   return new ResponseEntity<>(sources, HttpStatus.CREATED);
	}
	
	
	/**
	 * Returns all the sources for matching parameter
	 * 
	 * @param source
	 * @return <List<SymptomSourceInfo>
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomSourceInfo>> searchSources(@RequestParam(value = "source", required=true)  String  source) {
		List<SymptomSourceInfo> sources = symptomSourceInfoService.searchSources(source);
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @param groupID
	 * @return String
	 */
	@RequestMapping(value = "/{sourceID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteGroup(@PathVariable("sourceID") Integer sourceID) {
	        symptomSourceInfoService.delete(sourceID);
	    String    str ="Source deleted Successfully";
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
	
}
