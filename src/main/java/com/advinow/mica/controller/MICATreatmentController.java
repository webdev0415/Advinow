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

import springfox.documentation.annotations.ApiIgnore;

import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.model.IllnessSourcesModel;
import com.advinow.mica.model.TreatmentAuditSourcesModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentStatusModel;
import com.advinow.mica.model.TreatmentTypeRefGroups;
import com.advinow.mica.model.TreatmentTypeRefModel;
import com.advinow.mica.services.RxTreatmentService;
import com.advinow.mica.services.TreatmentService;
import com.advinow.mica.services.TreatmentTypeService;

/**
 * 
 * Treatment main controller for the CRUD operations.
 * 
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("/treatment")
@Api(value="/treatment", description="Illness/Symptom  treatment services")
public class MICATreatmentController {

	@Autowired
	TreatmentTypeService treatmentTypeService;

	@Autowired 
	RxTreatmentService rxciTreatmentService;
	
	@Autowired
	TreatmentService treatmentService;
	
	
	/**
	 * This method returns the all treatment types and related treatment descriptions.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentTypeRefGroups>  getAllTreatmentTypes() {
		TreatmentTypeRefGroups  treatmentGroups =	treatmentTypeService.getAllTreatmentTypes();
		/*if(treatmentGroups !=  null && treatmentGroups.getTreatmentTypes() != null &&  treatmentGroups.getTreatmentTypes().isEmpty()){
			throw new DataNotFoundException("No data found in the database");
		}
*/
		return new ResponseEntity<>(treatmentGroups, HttpStatus.OK);
	}

	/**
	 * Returns the treatment and related treatment description for the given treatment type.
	 * Possible types are diet,activity,therapy,counseling,work,woundcare,imaging,labs,procedures,discharge,physicalexam,otcdrugs.
	 * 
	 * @param typeID
	 * @return ResponseEntity
	 */
	@RequestMapping(value = "/types/{typeID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentTypeRefModel>  getTreatmentByType(@PathVariable("typeID") Integer typeID) {
		TreatmentTypeRefModel  treatmentTypeModel =	treatmentTypeService.getTreatmentByType(typeID);
		/*if(treatmentTypeModel != null &&  treatmentTypeModel.getTreatmentTypeDesc().isEmpty()){
			throw new DataNotFoundException("No data found in the database");
		}*/
		return new ResponseEntity<>(treatmentTypeModel, HttpStatus.OK);
	}

	/**
	 * Creates the treatment type and typedetails
	 * 
	 * @param treatmentTypeModel
	 * @return
	 */
	@RequestMapping(value = "/types", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel>  creatreatTrementType(@RequestBody TreatmentTypeRefModel treatmentTypeModel) {
		TreatmentStatusModel  treatmentStatusModel =	treatmentTypeService.createtreatTrementType(treatmentTypeModel);
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	/**
	 * Creates the illness treatments for the given drug and nondrugs
	 * 
	 * @param treatmentMainModel
	 * @return TreatmentStatusModel
	 */
	@RequestMapping(value = "/illness", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel>  creatreatTrementsForIllness(@RequestBody TreatmentMainModel treatmentMainModel) {
		TreatmentStatusModel  treatmentStatusModel = treatmentService.creatreatTrementsForIllness(treatmentMainModel);
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	
	
	
	/**
	 * Creates the illness treatments for the given drug and nondrugs
	 * 
	 * @param treatmentMainModel
	 * @return TreatmentStatusModel
	 */
	@RequestMapping(value = "/v1/illness", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel>  createTreatmentsWithRxcuiForIllness(@RequestBody TreatmentMainModel treatmentMainModel) {
		TreatmentStatusModel  treatmentStatusModel = rxciTreatmentService.creatreatTrementsForIllness(treatmentMainModel);
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
	
	/**
	 * 
	 * Returns all the illness treamtments
	 * 
	 * @return List<TreatmentMainModel>
	 */
	@ApiIgnore
	@RequestMapping(value = "/illness", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentMainModel>>  getAllTreatments() {
		List<TreatmentMainModel>  treatmentStatusModel = treatmentService.getIllnessTreatments();
		/*if(treatmentStatusModel != null && treatmentStatusModel.isEmpty()) {
			throw new DataNotFoundException("No illness data found");
		}*/

		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	/**
	 * Returns all the illness treatments with the type description information for the given icd10Code
	 * 
	 * @param icd10Code
	 * @return TreatmentMainModel
	 */
	@RequestMapping(value = "/illness/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getTreatmentByIcd10Code(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainModel  treatmentStatusModel = treatmentService.getTreatmentByICD10Code(icd10Code);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
	


    /**
     * Returns all the illness treatments without treatment description for the given icd10Code
     * @param icd10Code
     * @return TreatmentMainModel
     */
	@RequestMapping(value = "/illness/short/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getTreatmentByIcd10CodeWithNoDesc(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainModel  treatmentStatusModel = treatmentService.getTreatmentByIcd10CodeWithNoDesc(icd10Code);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	

    /**
     * Returns all the illness treatments without treatment description for the given icd10Code
     * @param icd10Code
     * @return TreatmentMainModel
     */
	@RequestMapping(value = "/v1/illness/short/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getRxcuiTreatmentByIcd10CodeWithNoDesc(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainModel  treatmentStatusModel =  rxciTreatmentService.getAllIllnessTreatments(icd10Code);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
	
/*	
    *//**
     * Returns all the illness treatments without treatment description for the given icd10Code
     * @param icd10Code
     * @return TreatmentMainModel
     *//*
	@RequestMapping(value = "/v1/illness/short/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getRxcuiTreatmentByIcd10CodeWithNoDesc(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainModel  treatmentStatusModel = rxciTreatmentService.getTreatmentByIcd10CodeWithNoDesc(icd10Code);
		if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	*/
	
	
	

	
    /**
     * Returns all the illness treatments without treatment description for the given icd10Code
     * @param icd10Code
     * @return TreatmentMainModel
     */
	@RequestMapping(value = "/v1/illness/all/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getAllIllnessTreatments(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainModel  treatmentStatusModel = rxciTreatmentService.getAllIllnessTreatments(icd10Code);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	
	

    /**
     * Deletes the illness treatment for the given icd10Code.
     * @param icd10Code
     * @return
     */
	@ApiIgnore
	@RequestMapping(value = "illness/icd10Code/{icd10Code:.+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  deleteIllnessById(@PathVariable("icd10Code") String icd10Code) {
		treatmentService.deleteTreatmentIllnessByCode(icd10Code);
		return  new ResponseEntity<>("Deleted..", HttpStatus.OK);
		//	return null;
	}


	
	/**
	 * Creates the symptom treamtments.
	 * 
	 * @param treatmentMainModel
	 * @return TreatmentStatusModel
	 */
	@RequestMapping(value = "/symptom", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel>  creatreatTrementsForSymptom(@RequestBody TreatmentMainModel treatmentMainModel) {
		TreatmentStatusModel  treatmentStatusModel = treatmentService.creatreatTrementsForSymptom(treatmentMainModel);
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
	/**
	 * Creates the symptom treamtments.
	 * 
	 * @param treatmentMainModel
	 * @return TreatmentStatusModel
	 */
	@RequestMapping(value = "/v1/symptom", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel>  createTreatmentsWithRxcuiForSymptom(@RequestBody TreatmentMainModel treatmentMainModel) {
		TreatmentStatusModel  treatmentStatusModel = rxciTreatmentService.creatreatTrementsForSymptom(treatmentMainModel);
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	

	 /**
	  *  Returns all the symptom treamtments with the type description information.
	  * @return List<TreatmentMainModel>
	  */
	@ApiIgnore
	@RequestMapping(value = "/symptom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentMainModel>>  getSymptomsTreatments() {
		List<TreatmentMainModel>  treatmentStatusModel = treatmentService.getSymptomsTreatments();
	/*	if(treatmentStatusModel != null && treatmentStatusModel.isEmpty()) {
			throw new DataNotFoundException("No symptom data found");
		}*/

		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

     /**
      *  Returns all the symptom treatments with the type description information for the given symptomID
      *  
      * @param symptomID
      * @return TreatmentMainModel>
      */
	@RequestMapping(value = "/symptom/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getTreatmentBySymptomID(@PathVariable("symptomID") String symptomID) {
		TreatmentMainModel  treatmentStatusModel = treatmentService.getTreatmentBySymptomID(symptomID);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for SymptomID :"+ symptomID);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

     /**
      * Returns all the symptom treamtments without treatment description for the given symptomID
      * 
      * @param symptomID
      * @return TreatmentMainModel
      */
	@RequestMapping(value = "/symptom/short/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getTreatmentBySymptomIDWithNoDesc(@PathVariable("symptomID") String symptomID) {
		TreatmentMainModel  treatmentStatusModel = treatmentService.getTreatmentBySymptomIDWithNoDesc(symptomID);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for SymptomID :"+ symptomID);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
    /**
     * Returns all the symptom treamtments without treatment description for the given symptomID
     * 
     * @param symptomID
     * @return TreatmentMainModel
     */
	@RequestMapping(value = "/v1/symptom/short/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainModel>  getRxcuTreatmentBySymptomIDWithNoDesc(@PathVariable("symptomID") String symptomID) {
		TreatmentMainModel  treatmentStatusModel = rxciTreatmentService.getTreatmentBySymptomIDWithNoDesc(symptomID);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for SymptomID :"+ symptomID);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	
    /**
     * Returns all the symptom treamtments without treatment description for the given symptomID
     * 
     * @param symptomID
     * @return TreatmentMainModel
     */
	@RequestMapping(value = "/v1/symptom/all/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentMainModel>>  getAllSymptomTreatments(@PathVariable("symptomID") String symptomID) {
		List<TreatmentMainModel>  treatmentStatusModel = rxciTreatmentService.getAllSymptomTreatments(symptomID);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for SymptomID :"+ symptomID);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}

	
	

	/**
	 * Delete the symptom treatment for the given symptomID.
	 * @param symptomID
	 * @return String
	 */
	
	@ApiIgnore
	@RequestMapping(value = "symptom/symptomID/{symptomID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  symptom(@PathVariable("symptomID") String symptomID) {
		treatmentService.deleteSymptomBycode(symptomID);
		return  new ResponseEntity<>("Deleted..", HttpStatus.OK);
		//	return null;
	}
	
	/**
	 * Returns all the illness treamtments with the type description information for the given icd10Code. 
	 * This end point being used by doctor APP.
	 * 
	 * 
	 * @param icd10Code
	 * @return TreatmentMainDocModel
	 */
	@RequestMapping(value = "/illness/types/{icd10Code:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainDocModel>  getTreatmentTypesByIcd10Code(@PathVariable("icd10Code") String icd10Code) {
		TreatmentMainDocModel  treatmentStatusModel = treatmentService.getTreatmentTypesByIcd10Code(icd10Code);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for icd10Code :"+ icd10Code);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	
	 /**
     *  Returns all the symptom treamtments with the type and description  for the given symptomID.
     *  This end point being used by doctor APP.
     *  
     * @param symptomID
     * @return TreatmentMainModel>
     */
	@RequestMapping(value = "/symptom/types/{symptomID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentMainDocModel>  getTreatmentTypesBySymptomID(@PathVariable("symptomID") String symptomID) {
		TreatmentMainDocModel  treatmentStatusModel = treatmentService.getTreatmentTypesBySymptomID(symptomID);
		/*if(treatmentStatusModel == null) {
			throw new DataNotFoundException("No data found for SymptomID :"+ symptomID);
		}*/
		return new ResponseEntity<>(treatmentStatusModel, HttpStatus.OK);
	}
	
	/**
	 * This Endpoint returns all the source information for given icd10Code
	 *  T
	 * @param icd10code
	 * @param state
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/sources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TreatmentSourceInfo>> getIllnessTreatmentSources(@RequestParam(value = "icd10code", required=false ) String icd10code ,@RequestParam(value = "symptomID", required=false ) String symptomID ) {
	
		List<TreatmentSourceInfo> sources = null;
		if(icd10code != null) {
		sources = treatmentService.getTreatmentSourcesForGivenIllness(icd10code.toUpperCase());
		}
		
		if(symptomID != null) {
			 sources = treatmentService.getTreatmentSourcesForGivenSymptom(symptomID.toUpperCase());
			
		}
	   
		
		return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	

	
	
	
	
	
	@RequestMapping(value = "/illness/sources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreatmentStatusModel> updateTreatmentSources(@RequestBody TreatmentAuditSourcesModel treatmentSourcesModel) {
		String sources = treatmentService.updateTreatmentSources(treatmentSourcesModel);
		TreatmentStatusModel status = new TreatmentStatusModel();
		status.setStatus(sources);
	   return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	
}
