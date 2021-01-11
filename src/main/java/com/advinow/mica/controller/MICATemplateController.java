/**
 * 
 */
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

import springfox.documentation.annotations.ApiIgnore;

import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.model.CategoryModel;
import com.advinow.mica.model.GenericSymptomGroups;
import com.advinow.mica.model.MICAResponse;
import com.advinow.mica.model.MICASymptomsGroup;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.LogicalSymptomGroupsModel;
import com.advinow.mica.model.SymptomGroups;
import com.advinow.mica.model.SymptomTemplateDTO;
import com.advinow.mica.model.SymptomTemplateModel;
import com.advinow.mica.model.SymptomTemplateModelV1;
import com.advinow.mica.model.SymptomTemplateValidate;
import com.advinow.mica.model.SymptomsMainModel;
import com.advinow.mica.model.PolicyModel;
import com.advinow.mica.services.MICATemplateService;
import com.advinow.mica.services.TreatmentPolicyService;

/**
 * 
 * 
 * @author Govinda Reddy
 *
 */

@RestController
@RequestMapping("/template")
@Api(value="/template", description="SymptomTemplate service")
public class MICATemplateController {
	
	@Autowired
	MICATemplateService mICATemplateService;
	
	@Autowired
	TreatmentPolicyService  treatmentPolicyService;
	
    /**
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/symptomgroups/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomGroups> getSymtomsByGroup(@PathVariable("groupId") String groupId) {
		SymptomGroups  responseSymptomGroup = mICATemplateService.getSymptomsByGroup(groupId,"en");	
	   return new ResponseEntity<>(responseSymptomGroup, HttpStatus.OK);
	}
	
	
	  /**
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/symptomsByCategory/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericSymptomGroups> getSymtomsByUser(@PathVariable("groupId") String groupId) {
		GenericSymptomGroups  responseSymptomGroup = mICATemplateService.symptomsByCategory(groupId,"en");	
	   return new ResponseEntity<>(responseSymptomGroup, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @param symptomsGroup 
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/symptomgroups/{groupId}", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomGroups> createsymptomsByGroup(@RequestBody SymptomGroups symptomsGroup,@PathVariable("groupId") String groupId) {
	SymptomGroups  responseSymptomGroup= 	mICATemplateService.createsymptomsByGroup(symptomsGroup, groupId);
	 return new ResponseEntity<>(responseSymptomGroup, HttpStatus.CREATED);
	
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/symptomgroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public MICASymptomsGroup  getSymtomGroups() {
	return	mICATemplateService.getSymtomGroups("en");
		
	}
		
	/**
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "/addSymptoms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<SymptomGroup> >  addSymptoms() {
	  Iterable<SymptomGroup> symptomGroup=	mICATemplateService.createSymtoms(SymptomUtil.createGroup());
		 return new ResponseEntity<>(symptomGroup, HttpStatus.CREATED);
		
	}*/
	
	/**
	 * 
	 * @param category
	 * @return
	 */
	
	@RequestMapping(value = "/category/symptom", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomCategory> createsymptomsByCategory(@RequestBody CategoryModel category) {
	SymptomCategory symptomCategory = mICATemplateService.createSymptomsByCategory(category);
	 return new ResponseEntity<>(symptomCategory, HttpStatus.CREATED);
	
	}
	
	/**
	 * 
	 * Removes the symptom from the templates and illness data as well.
	 * 
	 * @param symptompID
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/category/symptoms/{symptomID}", method = RequestMethod.DELETE)
	public ResponseEntity<String> createsymptomsByCategory(@PathVariable("symptomID") String symptomID) {
	String status = mICATemplateService.deleteSymptom(symptomID);
	 return new ResponseEntity<>(status, HttpStatus.OK);
	
	}
	
	/* 
     * Accepts a SymptomID and returns the following Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean). 
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/symptoms/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModel> getSymptomByID(@PathVariable("symptomID") String symptomID) {
		SymptomTemplateModel  symptom = mICATemplateService.getSymptomByID(symptomID,"en");	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	/* 
     * Accepts a SymptomID and returns the following Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean). 
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/v1/symptoms/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModelV1> getSymptomByIDV1(@PathVariable("symptomID") String symptomID) {
		SymptomTemplateModelV1  symptom = mICATemplateService.getSymptomByIDV1(symptomID,"en");	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	/* 
     * Returns all the symptom definitions
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/symptoms/definitions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomTemplateModel>> getAllSymptomDefinitions() {
		List<SymptomTemplateModel>  symptomDefinitions = mICATemplateService.getAllSymptomDefinitions();	
	   return new ResponseEntity<>(symptomDefinitions, HttpStatus.OK);
	}
	
		
	/*@RequestMapping(value = "/symptoms/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Symptoms> getSymptomByID(@PathVariable("symptomID") String symptomID) {
		Symptoms  symptom = mICATemplateService.getSymptomByCode(symptomID);	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}*/
	
	
	/**
	 * Accepts a Symptom ID and updates the Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean) and ic10RCodes for illness data and symptoms templates.
	 * @param symptomTemplate
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/symptoms", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModel> updateSymptom(@RequestBody SymptomTemplateModel symptomTemplate) {
		SymptomTemplateModel  symptom = mICATemplateService.updateSymptom(symptomTemplate,"en");	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	/**
	 * Accepts a Symptom ID and updates the Name, Criticality, Question,Prior, Antithesis, Treatable (new boolean) and ic10RCodes for illness data and symptoms templates.
	 * @param symptomTemplate
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/v1/symptoms", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateModelV1> updateSymptomV1(@RequestBody SymptomTemplateModelV1 symptomTemplate) {
		SymptomTemplateModelV1  symptom = mICATemplateService.updateSymptomV1(symptomTemplate,"en");	
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/symptoms/validate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomTemplateValidate>> validate() {
		List<SymptomTemplateValidate>  symptom = mICATemplateService.validateTemplates("en");
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}
		
	@RequestMapping(value = "/symptoms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<SymptomTemplate>> getAllSymptomTemplates() {
		Set<SymptomTemplate>  symptom = mICATemplateService.getAllSymptomTemplates();
	   return new ResponseEntity<>(symptom, HttpStatus.OK);
	}

	/**
	 * Returns the symptoms for the given symptomID and name
	 * 
	 *   /api/illness?icd10code=govind?state=APPROVED
	 *  
	 * @param icd10Code
	 * @return
	 */


	@RequestMapping(value = "/{source}/symptoms/paging", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginationModel> getSymptomsWithPaging(
		/*	@RequestParam(value = "source", required=true)  String  source,*/
			@PathVariable("source") String source,
			@RequestParam(value = "symptomID", required=false)  String  symptomID,
			@RequestParam(value = "symptomName", required=false)  String  symptomName,
			@RequestParam(value = "groupName", required=false)  String  groupName,
			@RequestParam(value = "page", required=false)  Integer  page,		
			@RequestParam(value = "size", required=false)  Integer  size) {
		    PaginationModel illness = mICATemplateService.getSymptomsWithPaging(page, size, source.toUpperCase(),symptomID,symptomName,groupName); 
		    return new ResponseEntity<>(illness, HttpStatus.OK);
	} 

	
	@RequestMapping(value = "/basicinfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomsMainModel> getSymptomBasicInfo() {
		SymptomsMainModel  symptoms = mICATemplateService.getSymptomBasicInfo();
	   return new ResponseEntity<>(symptoms, HttpStatus.OK);
	}
	
	
	/* 
     * Returns all the symptom question text 
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomTemplateDTO>> getAllQuestions() {
		List<SymptomTemplateDTO>  questions = mICATemplateService.getAllSymptomQuestions();	
	   return new ResponseEntity<>(questions, HttpStatus.OK);
	}
	
	
	/* 
     * Returns question text for given symptom ID
     * 
     * @param groupId
     * @return
     */
	@RequestMapping(value = "/questions/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomTemplateDTO> getSymptomQuestion(@PathVariable("symptomID") String symptomID) {
		
		if(symptomID==null) {
			
			throw new DataInvalidException("Symptom ID should not be null");
		}
		
		SymptomTemplateDTO  symptomTemplateModel = mICATemplateService.getSymptomQuestion(symptomID);	
	   return new ResponseEntity<>(symptomTemplateModel, HttpStatus.OK);
	}
	
/*	*//**
	 * This API allows to search symptoms by name, excludes NLP symptoms
	 * Seach is extended to inclue snomed name search and  handled misspelling
	 * 
	 * @param symptomName
	 * @param page
	 * @param size
	 * @return
	 *//*
	@RequestMapping(value = "/search/symptoms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginationModel> searchSymptomsByName(
	    	@RequestParam(value = "name", required=true)  String  symptomName,
			@RequestParam(value = "page", required=false)  Integer  page,		
			@RequestParam(value = "size", required=false)  Integer  size) {
		    PaginationModel illness = mICATemplateService.searchSymptomsByName(page, size, symptomName); 
		    return new ResponseEntity<>(illness, HttpStatus.OK);
	} 
	*/
	
	/**
	 * This API allows to search symptoms by name, excludes NLP symptoms
	 * Search is extended to include snomed name search and  misspelling for the names
	 * 
	 * @param symptomName
	 * @param page
	 * @param size
	 * @return GenericQueryResultEntity
	 */
	@RequestMapping(value = "/search/symptoms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity< List<GenericQueryResultEntity>> searchSymptomsByName(
	    	@RequestParam(value = "name", required=true)  String  symptomName) {
		    List<GenericQueryResultEntity> symptoms = mICATemplateService.searchSymptomsByName(symptomName); 
		    return new ResponseEntity<>(symptoms, HttpStatus.OK);
	} 
	
	
	
	/**
	 * This API  essentially adds/updates/removes symptoms from a logical group
	 * 
	 * @param logicalSymptomGroupsModel
	 * @return String
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> SaveOrUpdateSymptomGroups(@RequestBody LogicalSymptomGroupsModel logicalSymptomGroupsModel) {
		String  status = mICATemplateService.SaveOrUpdateSymptomGroups(logicalSymptomGroupsModel);	
	   return new ResponseEntity<>(status, HttpStatus.CREATED);
	}
	
	
	/**
	 * 
	 * 
	 * @param groupID
	 * @return LogicalSymptomGroupsModel
	 */ 
	@RequestMapping(value = "/groups/{groupID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LogicalSymptomGroupsModel> getSymptomsForGivenGroup(@PathVariable("groupID") Integer groupID) {
		LogicalSymptomGroupsModel  symptoms = mICATemplateService.getSymptomsForGivenGroup(groupID);	
	   return new ResponseEntity<>(symptoms, HttpStatus.OK);
	}
	
	
	
	

	/**
	 * This API  essentially adds/updates/removes symptoms from treatemnt policies
	 * 
	 * @param PolicyModel
	 * @return String
	 */
	@RequestMapping(value = "/policies", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MICAResponse> updateSymptomPolicies(@RequestBody PolicyModel treatmentPolicyModel) {
		String  status = treatmentPolicyService.updateSymptomPolicies(treatmentPolicyModel);	
		MICAResponse response = new MICAResponse();
		response.setStatus(status);
		
	   return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	/**
	 * 
	 * 
	 * @param policyID
	 * @return TreatmentPolicyModel
	 */ 
	@RequestMapping(value = "/policies/{policyID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PolicyModel> getSymptomsPolicies(@PathVariable("policyID") Integer policyID) {
		PolicyModel  symptoms = treatmentPolicyService.getSymptomsPolicies(policyID);	
	   return new ResponseEntity<>(symptoms, HttpStatus.OK);
	}
	
	
	
	
}
