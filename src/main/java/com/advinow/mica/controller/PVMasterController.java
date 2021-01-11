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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.domain.PVMasterQueryResult;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.services.PVMasterService;

/**
 * @author Developer
 *
 */

@RestController
@RequestMapping("/pv")
@Api(value="/pv", description="PV drug search")
public class PVMasterController {
	
	@Autowired
	PVMasterService pvMasterService;
	
	/**
	 *   This API allows to search drugs by name and includes fuzzy logic
	 * @param drugName
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/search/drugs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PVMasterQueryResult>> searchByPVDrugName(
	    	@RequestParam(value = "name", required=true)  String  drugName,
	    	@RequestParam(value = "status", required=false)  String  status) {
	List<PVMasterQueryResult> pvList = null;
		if(status != null) {
			pvList = pvMasterService.searchPvDruNameWithParameters(drugName,status.toUpperCase()); 
		} else {
			pvList = pvMasterService.searchByPVDrugName(drugName); 
		}
		   return new ResponseEntity<>(pvList, HttpStatus.OK);
	}

	
	
	
	
	
	/**
	 * This API allows to search drugs by pvid and status
	 * @param pvid
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/search/combinationdrugs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PVMasterQueryResult>> getCombinationDrug(
	    	@RequestParam(value = "pvid", required=true)  List<Long>  pvid,
	    	@RequestParam(value = "status", required=false)  String  status) {
		
		if( pvid != null && pvid.size() > 10) {
			throw new DataInvalidException("Request parameters should not exceed 10");
		}
		
		
	List<PVMasterQueryResult> pvList = null;
		if(status != null) {
			pvList = pvMasterService.getCombinationDrugWithParameters(pvid,status.toUpperCase()); 
		} else {
			pvList = pvMasterService.getCombinationDrugs(pvid); 
		}
		   return new ResponseEntity<>(pvList, HttpStatus.OK);
	}

	
}
