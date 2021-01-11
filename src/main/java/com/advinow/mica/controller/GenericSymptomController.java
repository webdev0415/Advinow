/**
 * 
 */
package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.model.GenericSymptomPagination;
import com.advinow.mica.services.GenericRawSymptomService;
import com.advinow.mica.services.MICATemplateService;


/**
 * @author Govinda Reddy
 * 
 *
 */
@RestController
@RequestMapping("/sources")
@Api(value="/sources", description="Generic services for  third part sources.")
public class GenericSymptomController {
	

	@Autowired
	GenericRawSymptomService  genericRawSymptomService;
	
	@Autowired
	MICATemplateService mICATemplateService;
	
	@RequestMapping(value = "/symptoms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericSymptomPagination> getRawSymptomsWithPaging(
			@RequestParam(value = "source", required=true)  String  source,
			@RequestParam(value = "name", required=false)  String  name,
			@RequestParam(value = "page", required=false)  Integer  page,
			@RequestParam(value = "size", required=false)  Integer  size) {
		
		     GenericSymptomPagination symptoms = null;
		
		     if(source.equalsIgnoreCase("ECW")) {
		      symptoms = genericRawSymptomService.getRawSymptomsWithPaging(source,name,page, size); 
		     } else{
		      symptoms = mICATemplateService.getSymptomTemplatesWithPaging(source.toUpperCase(),name,page, size); 
		     }
		
		    return new ResponseEntity<>(symptoms, HttpStatus.OK);
	} 
	

}
