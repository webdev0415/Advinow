package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import java.util.List;
import java.util.Set;

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

import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourceInfo;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.SymptomSource;
import com.advinow.mica.services.OtherSourceIllnessService;
import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
@RequestMapping("/{source}")
@Api(value="{source}", description="Other source illness service...")
public class OtherSourceIllnessController {
	
	@Autowired
	OtherSourceIllnessService  otherSourceIllnessService;
	  
	
	/**
	 * Returns the illnesses for the given icd10 code AND  state
	 * 
	 *   /api/illness?icd10code=govind?state=APPROVED
	 *  
	 * @param icd10Code
	 * @return
	 */

	@RequestMapping(value = "/api/illness", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginationModel> getIllnessWithPaging(
			@PathVariable("source") String source,
			@RequestParam(value = "icd10Code", required=false)  String  icd10Code,
			@RequestParam(value = "name", required=false)  String  name,
			@RequestParam(value = "status", required=false)  String  status,
			@RequestParam(value = "page", required=false)  Integer  page,		
			@RequestParam(value = "size", required=false)  Integer  size) {
		    PaginationModel illness = otherSourceIllnessService.getIllnessWithPaging(page, size, source.toUpperCase(),status,icd10Code,name); 
		    return new ResponseEntity<>(illness, HttpStatus.OK);
	} 

	/**
	 * Returns all the illness with symptom data for given user and status.
	 * <br> {
	 * <br> /api/illness/user?userID=1
	  * <br> }
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws JsonProcessingException
	 */
	
	@RequestMapping(value = "/api/illness/user",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData> getIllnessByUserAndSource(@RequestParam(value = "userID", required=true)  Integer  userID, @PathVariable("source") String source) throws JsonProcessingException {
		IllnessUserData illnesses = otherSourceIllnessService.findByIllnessByUserAndSource(userID,source.toUpperCase());
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	}
	/**
	 *  Updates all the illness with to state and reassign all the illnesses for the given user.
	 * 
	 *   <br> { <br>
	 *   "userID": 109, <br>
	 *   "fromState" : "LOADDED", <br>
	 *   "toState" : "REVIEWED",  <p>
	 *   "icd10Code" : "H35.733"<br>  
	 *   } <br>
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/user",  method = RequestMethod.PUT, consumes=  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateStatusReAssignUser(@RequestBody IllnessStatusModel illnessStatusModel,@PathVariable("source") String source) throws JsonProcessingException {
		String status = otherSourceIllnessService.updateStatusReAssignUser(illnessStatusModel,source.toUpperCase());
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	
	/**
	 *  Takes user ID,groups,symptom data and creates the illness records and assign to given user.
	 * @param userData
	 * @return
	 * @throws MICAApplicationException
	 * @throws JsonProcessingException
	 */

	@RequestMapping(value = "/api/illness", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateECWIllnessData(@RequestBody IllnessModel illnessModel,@PathVariable("source") String source)  throws MICAApplicationException, JsonProcessingException  {
		IllnessStatusModel	dbIllness= 	otherSourceIllnessService.saveIllnessData(illnessModel);
		return new ResponseEntity<>(dbIllness, HttpStatus.CREATED);
	}

	/**
	 * /2070Services/ecw/api/illness/H00.013?state=LOADED
	 * 
	 * @param icd10Code
	 * Returns sources with illness data
	 * 
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/api/illness/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData>  getIllnessByIcd10Code(@PathVariable("icd10Code") String icd10Code,@RequestParam(value = "state", required=false)  String  state,@PathVariable("source") String source) {
		IllnessUserData illnesses = otherSourceIllnessService.getIllnessByIcd10Code(icd10Code,state,source.toUpperCase());
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	} 

	
	/**
	 *
	 *This endpoint is created for doctor app to get top 5 sources for a symptom by verified flag.
	 * 
	 * @param icd10Code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/api/sourceinfo/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<SymptomSource>>  getSymptomsWithSource(@PathVariable("icd10Code") String icd10Code, @RequestParam(value = "state", required=true) String  state,@PathVariable("source") String source) {
		Set<SymptomSource> sourceInfoSymptoms = otherSourceIllnessService.getSymptomsWithSource(icd10Code,state,source.toUpperCase());
		return new ResponseEntity<>(sourceInfoSymptoms, HttpStatus.OK);
	} 
	

	
	/**
	 *
	 *This API is created for doctor app to get top 5 sources for a symptom by verified flag for a given list of illnesses.
	 * 
	 * @param icd10Code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/api/sourceinfo/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IllnessSourceInfo>>  getSymptomsSourcesFromIllnesses(@RequestParam("icd10Code") List<String> icd10Code, @RequestParam(value = "state", required=true) String  state,@PathVariable("source") String source) {
		List<IllnessSourceInfo> sourceInfoSymptoms = otherSourceIllnessService.getSymptomsSourcesFromIllnesses(icd10Code,state,source.toUpperCase());
		return new ResponseEntity<>(sourceInfoSymptoms, HttpStatus.OK);
	}
	
	
	
	/**
	 * /
	 * 
	 * @param icd10Code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/api/illnesses/approved", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IllnessDataQueryResultEnitity>>  getApprovedIllnesses(@PathVariable("source") String source) {
		List<IllnessDataQueryResultEnitity> illnesses = otherSourceIllnessService.getApprovedIllnesses(source.toUpperCase());
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	}
	

}
