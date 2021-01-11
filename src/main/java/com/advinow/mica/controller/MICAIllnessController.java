package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.advinow.mica.domain.BadIllness;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.model.ICD10CodeModel;
import com.advinow.mica.model.ICD10CodesModel;
import com.advinow.mica.model.IllnessModel;
import com.advinow.mica.model.IllnessSourcesModel;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.IllnessUserData;
import com.advinow.mica.model.PaginationModel;
import com.advinow.mica.model.SymptomData;
import com.advinow.mica.model.UserICD10CodeModel;
import com.advinow.mica.services.MICAIllnessService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * Controller for illness CRUD operations.
 * 
 * @author Govinda ReddyR
 *
 */
@RestController
@RequestMapping("/mica")
@Api(value="/mica", description="Illness service")
public class MICAIllnessController {

	@Autowired
	MICAIllnessService mICARestService;
	
	
	protected Logger logger = LoggerFactory.getLogger(MICAIllnessController.class);

	/**
	 *  Takes user ID,groups completed,symptom data and creates the illness records and assign to given user.
	 * @param userData
	 * @return
	 * @throws MICAApplicationException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> addUserIllnessData(@RequestBody IllnessUserData userData)  throws MICAApplicationException, JsonProcessingException  {
		IllnessStatusModel	dbIllness= 	mICARestService.addIllnessUserData(userData);
		return new ResponseEntity<>(dbIllness, HttpStatus.CREATED);
	}

	/**
	 * Gives all the illness information with symptom data.
	 * 
	 * @return
	 * @throws JsonProcessingException
	 *//*
	@RequestMapping(value = "/api/illness",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData> getIllness() throws JsonProcessingException {
		IllnessUserData illnesses = otherSourceIllnessService.getIllnessesForGroups();
		return new ResponseEntity<>(illnesses, HttpStatus.OK);

	}
*/


	/**
	 * Takes list of icd10Codes and returns the Name and Criticality. if list is empty then returns all the illness with Name and Criticality.<br>
	 * <br> {
	 * <br>  "idIcd10Codes" :[] or   "idIcd10Codes" :["A001"] 
	 *  <br> }
	 * <br>
	 * @return ResponseEntity
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/icd10codes", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ICD10CodesModel> getIllnessFromICDCodes(@RequestBody IllnessStatusModel illnessStatusModel) throws JsonProcessingException {
		ICD10CodesModel illnesses = mICARestService.getIllnessFromICDCodes(illnessStatusModel);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	}
	

	/**
	 * Takes list of icd10Codes and returns approved records wih name and Criticality for given icd10Code.<br>
	 * <br> This api for doctor app
	 * <br>  api/illness/icd10codes/R11.11
	 *  <br>
	 * <br>
	 * @return ResponseEntity
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/icd10codes/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ICD10CodesModel> getIllnessforGivenICDCode(@PathVariable("icd10Code") String icd10Code) throws JsonProcessingException {
		IllnessStatusModel illnessStatusModel = new IllnessStatusModel();
		if(icd10Code != null) {
		illnessStatusModel.getIcd10Codes().add(icd10Code);
		}
		ICD10CodesModel illnesses = mICARestService.getIllnessFromICDCodes(illnessStatusModel);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);

	}



	/**
	 *  Updates the Criticality for all records in the DB and the symptom templates for given icd10code
	 *  
	 *  <br>   {
	 *  <br>  "criticality": 15,
	 *  <br>  "icd10Code": "G08"
	 *  <br>   }
	 *  
	 * @return ResponseEntity
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/icd10codes", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ICD10CodeModel> updateIllnessRecord(@RequestBody ICD10CodeModel iCD10CodeModel) throws JsonProcessingException {
		ICD10CodeModel illnesse = mICARestService.updateIllnessRecord(iCD10CodeModel);
		return new ResponseEntity<>(illnesse, HttpStatus.CREATED);

	}

	/**
	 * Returns the illnesses for the given icd10 code AND  state
	 * 
	 *   /api/illness/H01.026?state=APPROVED
	 *  
	 * @param icd10Code
	 * @return
	 */

	@RequestMapping(value = "/api/illness/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData>  getIllnessByIcd10Code(@PathVariable("icd10Code") String icd10Code,@RequestParam(value = "state", required=false)  String  state) {
		IllnessUserData illnesses = mICARestService.getIllnessByIcd10Code(icd10Code,state);
		if(illnesses == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	} 


	/**
	 * 
	 * Gives all the illness information with symptom data for given user.
	 * @param userID
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/user/{userID}",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData> getIllness(@PathVariable("userID") Integer userID) throws JsonProcessingException {
		IllnessUserData illnesses = mICARestService.getIllnessesForGroupsyByUser(userID);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);

	}
	
	
	/**
	 * 
	 * Returns basic illness information for given user and source.
	 * 
	 * /api/illness/info?userID=1&source=MICA
	 * 
	 * @param userID
	 * @param source
	 * @return
	 */
	
	@RequestMapping(value = "/api/illness/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserICD10CodeModel>>  getIllnessInformation(@RequestParam(value = "source", required=true)  String  source,@RequestParam(value = "userID", required=false) Integer userID) {
		List<UserICD10CodeModel> illnesses = mICARestService.getIllnessInformation(userID,source);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	} 
	

	/**
	 * Returns all the illness with symptom data for given user and status.
	 * <br> {
	 * <br> "userID" : 1,
	 * <br> "state" :["PENDING"]
	 * <br> }
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws JsonProcessingException
	 */

	
	@RequestMapping(value = "/api/illness/user",  method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessUserData> getIllness(@RequestBody IllnessStatusModel illnessStatusModel) throws JsonProcessingException {
		IllnessUserData illnesses = mICARestService.findByIllnessByUserAndStatus(illnessStatusModel);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	}

	
	/**
	 * Returns the icd10Codes with versions for the given user and status.
	 * 
	 * @param userID
	 * @param state
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/status",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, List<Integer>>> getUserIcd10Codes(
			@RequestParam(value = "userID", required=true)  Integer  userID,
			@RequestParam(value = "state", required=true)  String  state) throws JsonProcessingException {
		Map<String, List<Integer>> icd10Codes = mICARestService.findByicd10CodeByUserAndStatus(userID,state);
		return new ResponseEntity<>(icd10Codes, HttpStatus.OK);
	}
	

	/**
	 * Deletes all the illness related records for the given illness id.
	 * 
	 * @param id
	 * @return String
	 */
	@ApiIgnore
	@RequestMapping(value = "/api/illness/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  getIllnessesDeleteBycode(@PathVariable("id") Long id) {
		mICARestService.getIllnessesDeleteBycode(id);
		return  new ResponseEntity<>("Deleted..", HttpStatus.OK);
		}

	/**
	 * Deletes all the symptoms related records for the given symptom id.
	 * 
	 * @param id
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/api/illness/symptom/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  deleteSymptomByID(@PathVariable("id") Long id) {
		mICARestService.deleteSymptomByID(id);
		return  new ResponseEntity<>("Deleted..", HttpStatus.OK);
	}


	/**
	 * Deletes all the symptoms related records for the given symptom id.
	 * 
	 * @param id
	 * @return
	 */
	@ApiIgnore 
	@RequestMapping(value = "/api/illness/symptoms/{symptomID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  deleteBySymptomID(@PathVariable("symptomID") String symptomID) {
		mICARestService.deleteBySymptomID(symptomID);
		return  new ResponseEntity<>("Deleted..", HttpStatus.OK);
	}


	/**
	 * Returns all the illness related data for given symptoms IDs.
	 * 
	 * 
	 * @param symptomData
	 * @return ResponseEntity
	 * @throws MICAApplicationException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/api/illness/symptomsbyID", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IllnessModel>> getIllnessesBySymptomsIDs(@RequestBody SymptomData symptomData)  throws MICAApplicationException, JsonProcessingException  {
		List<IllnessModel>	dbIllness= 	mICARestService.getIllnessesBySymptomsIDs(symptomData.getSymptoms());
		return new ResponseEntity<>(dbIllness, HttpStatus.CREATED);
	}
/*
	/**
	 * Updates all the illness with to state and reassign all the illnesses for the given user.
	 * 
	 *  <br> { <br>
	 *  "userID": 109, <br>
	 *   "fromState" : "PENDING", <br>
	 *   "toState" : "COMPLETE",  <p>
	 *   "idIcd10Codes" : ["H35.733","H35.722"]<br>  
	 *   } <br>
	 *  @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	/*@RequestMapping(value = "/api/illness/update/user", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateIllnessesReAssignUser(@RequestBody IllnessStatusModel illnessStatusModel)  throws MICAApplicationException  {
		IllnessStatusModel resonseModel =	otherSourceIllnessService.illnessStatusUpdate(illnessStatusModel);
		if(resonseModel.getCount()==0) {
			return new ResponseEntity<>(resonseModel,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(resonseModel,HttpStatus.CREATED);

	}*/

	/**<BR>
	 * [{
	 * "userID": 109,
     * "fromState" : "FF",
	 * "toState" : "APPROVED",
	 * "icd10Code": "A00.0",
     * "version": 1
     * },
     * {
     *  "userID": 109,
     *  "fromState" : "FF",
     *  "toState" : "APPROVED",
	 *  "icd10Code": "A00.0",
     *  "version": 2
     * }
	 *  ] </BR>
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	@RequestMapping(value = "/api/illness/update/user", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateIllnessesReAssignUser(@RequestBody List<IllnessStatusModel> illnessStatusModel)  throws MICAApplicationException  {
		IllnessStatusModel resonseModel =	mICARestService.illnessStatusUpdateWithVersion(illnessStatusModel);
	/*	if(resonseModel.getCount()==0) {
			return new ResponseEntity<>(resonseModel,HttpStatus.BAD_REQUEST);
		}*/
		return new ResponseEntity<>(resonseModel,HttpStatus.OK);

	}
	
	
	/*@RequestMapping(value = "/api/illness/rollback/user", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateRollebackUser(@RequestBody List<IllnessStatusModel> illnessStatusModel)  throws MICAApplicationException  {
		IllnessStatusModel resonseModel =	otherSourceIllnessService.illnessStatusUpdateWithVersion(illnessStatusModel);
		if(resonseModel.getCount()==0) {
			return new ResponseEntity<>(resonseModel,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(resonseModel,HttpStatus.CREATED);

	}*/
	
	
	
	/**
	 * Deletes all the illness records for given ids
	 * 	 <br> {
	 *   <br>  "ids":[2190,2208]
	 *   <br> }
	 * 
	 * @param illnessStatusModel
	 * @return ResponseEntity
	 * @throws MICAApplicationException
	 */
	@ApiIgnore 
	@RequestMapping(value = "/api/illness", method = RequestMethod.DELETE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> deleteIllnessesByIDS(@RequestBody IllnessStatusModel illnessStatusModel)  throws MICAApplicationException  {
		IllnessStatusModel resonseModel =	mICARestService.deleteIllnessesByIDS(illnessStatusModel);
		return new ResponseEntity<>(resonseModel,HttpStatus.OK);
	}

	/**
	 * Deletes all the symptoms related records for the given symptom id.
	 * 
	 * @param id
	 * @return ResponseEntity
	 */
	@ApiIgnore 
	@RequestMapping(value = "/api/illness/symptoms",  method = RequestMethod.DELETE, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel>  deleteSymptomsByIDS(@RequestBody IllnessStatusModel illnessStatusModel) {
		// TODO Auto-generated method stub
		IllnessStatusModel responseModel = new IllnessStatusModel();
		List<Long> ids = illnessStatusModel.getIds();
		for (int i = 0; i < ids.size(); i++) {
			mICARestService.deleteSymptomByID(ids.get(i));
			responseModel.getIds().add(ids.get(i));
		}
		return new ResponseEntity<>(responseModel,HttpStatus.OK);
	}

 	/**
	 * Clones all the illnessess for list of illness ids, changes the illness state to given toState and reassign illness to the given user. 
	 * <br>     {
	 * <br>  "userID" : 111,
	 * <br> "ids":[3280],
	 * <br>   "toState" : "PROTECTED"
	 * <br>   }
	 *  
	 * @param illnessStatusModel
	 * @return
	 * @throws MICAApplicationException
	 */
	@RequestMapping(value = "/api/illness/clone", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> cloneIllness(@RequestBody IllnessStatusModel illnessStatusModel)  throws MICAApplicationException  {
		IllnessStatusModel resonseModel =	mICARestService.cloneIllness(illnessStatusModel);
		return new ResponseEntity<>(resonseModel,HttpStatus.OK);


	}

	
	@RequestMapping(value = "/api/illness/invalid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<BadIllness> >  getInvalidIllnesses() {
		Iterable<BadIllness>  illnesses = mICARestService.getInvalidIllnesses();
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	} 
	
	

	/**
	 * Accepts an ICD10 code (that is Ready for Review state) and compares it to all of the latest APPROVED Illnesses.
	 * An optional value may be passed to include or not include Time settings in the comparison.
	 * The endpoint should return a list of ICDs that "Match" exactly, or an empty list if there are no matches. 
	 * Note that if an Illness is a subset it does NOT count as a match. Meaning if Illness 1 has 4 symptoms and Illness 2 has 6 symptoms and 4 are exactly like Illness 1.
	 *  That is not an exact match
	 * 
	 * @param icd10Code
	 * @param time
	 * @return IllnessStatusModel
	 */
	@RequestMapping(value = "/illness/uniqueness/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>>  getUniqueIllnesses(@PathVariable("icd10Code") String icd10Code,@RequestParam(value = "inlcudetime", required=false)  Boolean  inlcudetime,@RequestParam(value = "version", required=false)  Integer  version) {
		List<String>  illnesses = mICARestService.getUniqueIllnesses(icd10Code,inlcudetime,version);
		return new ResponseEntity<>(illnesses, HttpStatus.OK);
	}
	
	
	/**
	 * If the diagnostic engine is using the chief complaint to find or filter diseases, all diseases should have at least one chief complaint. 
     *There are a few exclusions (R codes for lab findings etc, probably some Z codes as well)
     * At the final approval of a disease and at time of uniqueness check also check to make sure disease has at least 1 display symptom mapped.
	 * @param icd10Code
	 * @param inlcudetime
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/illness/chiefcomplaintcheck/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean>  chiefcomplaintcheck(@PathVariable("icd10Code") String icd10Code, Boolean  inlcudetime,@RequestParam(value = "version", required=true)  Integer  version) {
		Boolean  chiefComplaint = mICARestService.chiefcomplaintcheck(icd10Code,version);
		return new ResponseEntity<>(chiefComplaint, HttpStatus.OK);
	}
	
	
	
	
	/**
	 * 
	 * @param source
	 * @param symptomID
	 * @param symptomName
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/approved/illnesses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaginationModel> getAppovedIllnessesWithPagingForSymptom(
			@RequestParam(value = "source", required=true) String source,
			@RequestParam(value = "symptomID", required=false)  String  symptomID,
			@RequestParam(value = "symptomName", required=false)  String  symptomName,
			@RequestParam(value = "page", required=false)  Integer  page,		
			@RequestParam(value = "size", required=false)  Integer  size) {
		    PaginationModel illness = mICARestService.getIllnessWithPagingForGivenSymptom(page, size, source.toUpperCase(),symptomID,symptomName); 
		    return new ResponseEntity<>(illness, HttpStatus.OK);
	} 

	
    /**
     * API returns all symptoms for given list of illnesses with minimal Diagnostic Criteria set to true/false
     * 
     * @param icd10code
     * @return Map<String,List<String>>
     */
	@RequestMapping(value = "/mdc/illness", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,List<String>>>  getMDCSymptomsByIllnesses(@RequestParam(value = "icd10code", required=true ) List<String> icd10code, @RequestParam(value = "mdc", required=false, defaultValue="true" ) boolean mdc ) {
		List<String> icd10Codes = new ArrayList<String>();
		if(! icd10code.isEmpty()) {
			icd10Codes = icd10code.stream().map(String::toUpperCase).collect(Collectors.toList());
		}
		 Map<String,List<String>>  mdcSymptoms   = mICARestService.getMDCSymptomsByIllnesses(icd10Codes,mdc);
		return new ResponseEntity<>(mdcSymptoms, HttpStatus.OK);
	} 
	
	 /**
     * API returns all symptoms for given list of illnesses with Bias set to true/false
     * 
     * @param icd10code
     * @return Map<String,List<String>>
     */
	@RequestMapping(value = "/bias/illness", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,List<String>>>  getBiasSymptomsByIllnesses(@RequestParam(value = "icd10code", required=true ) List<String> icd10code, @RequestParam(value = "bias", required=false, defaultValue="false" ) boolean bias ) {
		List<String> icd10Codes = new ArrayList<String>();
		if(! icd10code.isEmpty()) {
			icd10Codes = icd10code.stream().map(String::toUpperCase).collect(Collectors.toList());
		}
		 Map<String,List<String>>  biasSymptoms   = mICARestService.getBiasSymptomsByIllnesses(icd10Codes,bias);
		return new ResponseEntity<>(biasSymptoms, HttpStatus.OK);
	} 
	
	
	@RequestMapping(value = "/illness/sources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomSourceInfo>> getIllnessSymptomsSources(@RequestParam(value = "icd10code", required=true ) String icd10code, @RequestParam(value = "state", required=true, defaultValue="APPROVED" ) String state ,@RequestParam(value = "version", required=true ) Integer version ) {
		List<SymptomSourceInfo> sources = mICARestService.getIllnessSymptomsSources(icd10code.toUpperCase(),state.toUpperCase(),version);
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	

	/**
	 * Returns all the illness with source data for given user and status.
	 * <br> {
	 * <br> "userID" : 1,
	 * <br> "state" :["PENDING"]
	 * <br> }
	 * 
	 * @param illnessStatusModel
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/illness/user/sources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomSourceInfo>> getIllnessSymptomsSourcesForGivenUser(@RequestBody IllnessStatusModel illnessStatusModel) {
		List<SymptomSourceInfo> sources = mICARestService.getIllnessSymptomsSourcesForGivenUser(illnessStatusModel.getUserID(), illnessStatusModel.getState());
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/illness/sources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateIllnessSources(@RequestBody IllnessSourcesModel illnessSourcesModel) {
		String sources = mICARestService.updateIllnessSources(illnessSourcesModel);
		IllnessStatusModel status = new IllnessStatusModel();
		status.setStatus(sources);
	   return new ResponseEntity<>(status, HttpStatus.OK);
	}

}
