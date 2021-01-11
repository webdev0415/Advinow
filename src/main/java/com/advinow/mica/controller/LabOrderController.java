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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.LabOrdersRef;
import com.advinow.mica.services.SymptomLabsService;

/**
 * @author Developer
 *
 */
@RestController
@RequestMapping("/laborders")
@Api(value="/laborders", description="Maintain lab order information for symptoms")
public class LabOrderController {
	
	
	
	@Autowired
	SymptomLabsService symptomLabsService;
	
	/**
	 * Returns labOrders with symptoms.
	 * 
	 * @return List<SymptomsLabOrders>
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LabOrdersRef>> getAllLabOrders() {
		List<LabOrdersRef> labOrders = symptomLabsService.getAllLabOrders();
	   return new ResponseEntity<>(labOrders, HttpStatus.OK);
	}
	
	
	
	/*

	@Autowired
	SymptomLabsService symptomLabsService;
	
	*//**
	 * Returns labOrders with symptoms.
	 * 
	 * @return List<SymptomsLabOrders>
	 *//*
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SymptomsLabOrders>> getAllLabOrders() {
		List<SymptomsLabOrders> sourceSymptomsLabOrderss = symptomLabsService.getAllLabOrders();
	   return new ResponseEntity<>(sources, HttpStatus.OK);
	}
	
	*//**
	 * Added new labOrder
	 * 
	 * @param labOrders
	 * @return SymptomsLabOrders
	 *//*
	@RequestMapping(value = "/add", method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomsLabOrders> addOrUpdate(@RequestBody SymptomsLabOrders labOrders ) {
		SymptomsLabOrders dbLabOrders = symptomLabsService.addOrUpdate(labOrders);
	   return new ResponseEntity<>(dbLabOrders, HttpStatus.CREATED);
	}
	
	
	*//**
	 * Returns laborders for given symptom.
	 * 
	 * @param symptomID
	 * @return SymptomsLabOrders
	 *//*
	@RequestMapping(value = "/{symptomID}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SymptomsLabOrders> getLabOrderedBySymptom(@PathVariable("symptomID")  String  symptomID) {
		SymptomsLabOrders order= symptomLabsService.getLabsOrdered(symptomID);
	   return new ResponseEntity<>(order, HttpStatus.OK);
	}
	
	
	*//**
	 * Delete  laborder for given symptomID 
	 * 
	 * @param symptomID
	 * @return String
	 *//*
	@ApiIgnore
	@RequestMapping(value = "/{symptomID}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteLabOrder(@PathVariable("symptomID") String symptomID) {
	        symptomLabsService.deleteLabOrder(symptomID);
	    String    str ="Laborder deleted Successfully";
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
	
*/}
