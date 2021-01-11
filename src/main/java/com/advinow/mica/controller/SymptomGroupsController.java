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

import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.services.SymptomGroupService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/groups")
@Api(value="/groups", description="CRUD for symptoms groups")
public class SymptomGroupsController {
	
	@Autowired
	SymptomGroupService symptomGroupService;
	
	/**
	 * Returs all the symptom groups.
	 * 
	 * @return List<LogicalSymptomGroupsModel>
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LogicalSymptomGroupsModel>> getSymptomROSGroups() {
		List<LogicalSymptomGroupsModel> groups = symptomGroupService.getSymptomGroups("En");
	   return new ResponseEntity<>(groups, HttpStatus.OK);
	}
	
	/**
	 * Creates new group
	 * 
	 * @param LogicalSymptomGroupsModel
	 * @return LogicalSymptomGroupsModel
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LogicalSymptomGroupsModel> createROSSymptomGroup(@RequestBody LogicalSymptomGroupsModel symptomGroup ) {
		LogicalSymptomGroupsModel group = symptomGroupService.createSymptomGroup(symptomGroup);
	   return new ResponseEntity<>(group, HttpStatus.CREATED);
	}
	
	
	/**
	 * Returns all the group names for matching parameter
	 * 
	 * @param source
	 * @return <List<LogicalSymptomGroupsModel>
	 */
/*	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LogicalSymptomGroupsModel>> searchSources(@RequestParam(value = "name", required=true)  String  name) {
		List<LogicalSymptomGroupsModel> sources = symptomGroupService.searchGroupsByName(name);
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	*/
	
	/**
	 * 
	 * @param groupID
	 * @return String
	 */
	@RequestMapping(value = "/{groupID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteGroup(@PathVariable("groupID") Integer groupID) {
	String str = symptomGroupService.delete(groupID);
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
}
