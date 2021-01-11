package com.advinow.mica.controller.lang;

import java.util.List;

//import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.model.BodyParts;
import com.advinow.mica.services.MICAGenericService;
import com.advinow.mica.util.MICAConstants;

/**
 * 
 * Generic controller provides the API for the data keys and data stores entities.
 * 
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("{languagecode}/generic")
//@Api(value="/generic", description="Generic service for spanish translation.")
public class MICAGenericLangRestController {
	
	@Autowired
	MICAGenericService mICAGenericService;
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/datakeys/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DataKeys  getDataKeysByName(@PathVariable("name") String name,  @PathVariable("languagecode") String languagecode) {
	return	mICAGenericService.getDataKeysByName(name);
	}
	

	@RequestMapping(value = "/bodyparts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BodyParts  getAllBodyParts( @PathVariable("languagecode") String languagecode) {
		
	if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
		throw new DataNotFoundException(languagecode + " Language not supported... ");
	}
		
	return	mICAGenericService.getBodyParts(languagecode);
	}
	
	
	@RequestMapping(value = "/painswelling/bodyparts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BodyParts>  getPainSwellingBodyParts(@PathVariable("languagecode") String languagecode) {
		
	if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
		throw new DataNotFoundException(languagecode + " Language not supported... ");
	}
		
	return	mICAGenericService.getPainSwellingBodyParts(languagecode);
	}
	
	
	
	@RequestMapping(value = "/bodyparts/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BodyParts  getAllCategoriesBodyparts(@PathVariable("languagecode") String languagecode) {
		
		
		if (!MICAConstants.LANGUAGE_CODES.contains(languagecode)) {
			throw new DataNotFoundException(languagecode + " Language not supported... ");
		}		
		
	return	mICAGenericService.getAllCategoriesBodyparts(languagecode);
	}
}
