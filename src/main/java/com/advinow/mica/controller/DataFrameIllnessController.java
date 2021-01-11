/**
 * 
 */
package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.advinow.mica.exception.MICAApplicationException;
import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.dataframe.DataFrameModel;
import com.advinow.mica.services.DataFrameCreateService;
import com.advinow.mica.services.DataFrameIllnessService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * Controller for DataFrame related operations.
 * 
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("/dataframe")
@Api(value="/dataframe", description="DataFrame service")
public class DataFrameIllnessController {
	
	@Autowired
	DataFrameIllnessService  dataFrameIllnessService;
	
	@Autowired
	DataFrameCreateService dataFrameCreateService;
		
	/**
	 * 
	 * Updates true for illnesses/Symptoms inclusion in DF for all given illnesses/Symptoms.
	 * 
	 * 
	 * @param dataFrameModel
	 * @return ResponseEntity
	 * @throws MICAApplicationException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IllnessStatusModel> updateDataFrame(@RequestBody DataFrameModel dataFrameModel)  throws MICAApplicationException, JsonProcessingException  {
		IllnessStatusModel	dbIllness= 	dataFrameIllnessService.updateDataFrameInfo(dataFrameModel);
		return new ResponseEntity<>(dbIllness, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DataFrameModel> getDataFrameSymptomsIllnesses()    {
		DataFrameModel	symptomConfig= 	dataFrameIllnessService.getSymptomsIllnesses();
		return new ResponseEntity<>(symptomConfig, HttpStatus.OK);
	}

	/**
	 * 
	 * Retruns active illnesses for data frame.
	 * 
	 * 
	 * @return DataFrameModel
	 */
	@RequestMapping(value = "/illnesses/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DataFrameModel> getAllAciveIllnesses()    {
		DataFrameModel	symptomConfig= 	dataFrameIllnessService.getAllAciveIllnesses();
		return new ResponseEntity<>(symptomConfig, HttpStatus.OK);
	}
	/**
	 *  Retruns active symptoms for data frame.
	 * 
	 * @return DataFrameModel
	 */
	@RequestMapping(value = "/symptoms/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DataFrameModel> getAllAciveSymptoms()    {
		DataFrameModel	symptomConfig= 	dataFrameIllnessService.getAllAciveSymptoms();
		return new ResponseEntity<>(symptomConfig, HttpStatus.OK);
	}
	
	
	/**
	 *  Retruns active symptoms for data frame.
	 * 
	 * @return DataFrameModel
	 */
/*	@ApiIgnore
	@RequestMapping(value = "/generate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> generateDFJson()    {
		dataFrameCreateService.generateDFJson();
		return new ResponseEntity<>("Created...", HttpStatus.OK);
	}
	*/
}
