package com.advinow.mica.controller;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.model.BodyParts;
import com.advinow.mica.services.MICAGenericService;

/**
 * 
 * Generic controller provides the API for the data keys and datastore entities.
 * 
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("/generic/")
@Api(value="/generic", description="Generic service")
public class MICAGenericRestController {
	
	@Autowired
	MICAGenericService mICAGenericService;
    
		
	/**
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/datakeys/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DataKeys  getDataKeysByName(@PathVariable("name") String name) {
	return	mICAGenericService.getDataKeysByName(name);
	}
	
    /**
     * 
     * @return BodyParts
     */
	@RequestMapping(value = "/bodyparts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BodyParts  getAllBodyParts() {
	return	mICAGenericService.getBodyParts("en");
	}
	
	
	@RequestMapping(value = "/painswelling/bodyparts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BodyParts>  getPainSwellingBodyParts() {
	return	mICAGenericService.getPainSwellingBodyParts("en");
	}
	
	@RequestMapping(value = "/bodyparts/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BodyParts  getAllCategoriesBodyparts() {
	return	mICAGenericService.getAllCategoriesBodyparts("en");
	}
	
	
	
}
