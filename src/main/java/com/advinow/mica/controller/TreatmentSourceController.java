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

import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.services.TreatmentSourceInfoService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/treatmentsources")
@Api(value="/treatmentsources", description="Maintains source information")
public class TreatmentSourceController {
	
	@Autowired
	TreatmentSourceInfoService treatmentSourceInfoService;
	
	/**
	 * Returs all the symptom sources.
	 * 
	 * @return List<TreatmentSourceInfo>
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentSourceInfo>> getSourceSymptoms() {
		List<TreatmentSourceInfo> sources = treatmentSourceInfoService.getSourceSymptoms();
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	/**
	 * Creates new source
	 * 
	 * @param symptomSource
	 * @return TreatmentSourceInfo
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentSourceInfo> getSourceSymptoms(@RequestBody TreatmentSourceInfo symptomSource ) {
		
		if(symptomSource !=  null && (symptomSource.getSource() == null ||  symptomSource.getSource().isEmpty())){
			throw new DataNotFoundException("Please include source in the payload.");
		}
		
		TreatmentSourceInfo sources = treatmentSourceInfoService.createSourceSymptom(symptomSource);
	   return new ResponseEntity<>(sources, HttpStatus.CREATED);
	}
	
	
	/**
	 * Returns all the sources for matching parameter
	 * 
	 * @param source
	 * @return <List<TreatmentSourceInfo>
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentSourceInfo>> searchSources(@RequestParam(value = "source", required=true)  String  source) {
		List<TreatmentSourceInfo> sources = treatmentSourceInfoService.searchSources(source);
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	

	/**
	 * 
	 * @param groupID
	 * @return String
	 */
	@RequestMapping(value = "/{sourceID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteGroup(@PathVariable("sourceID") Integer sourceID) {
		treatmentSourceInfoService.delete(sourceID);
	    String    str ="Source deleted Successfully";
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
	
}
