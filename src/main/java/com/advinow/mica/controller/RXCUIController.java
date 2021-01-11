package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.advinow.mica.services.NdcRXCUIService;
import com.advinow.mica.services.RXCUIService;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * Download all the rxcui common codes.
 * 
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("/util")
@Api(value="/util")
public class RXCUIController {
	
	
	@Autowired
	RXCUIService  rxcuiService;
	
	@Autowired
	NdcRXCUIService ndcRXCUIService;
	
	/**
	 *  Download ingredient rxcui codes for given rxcui
	 * 
	 * @return String
	 */
	@ApiIgnore
	@RequestMapping(value = "/download/ingredient", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downloadrx()    {
		rxcuiService.downloadrx();
		return new ResponseEntity<>("Created...", HttpStatus.OK);
	}

	
	
	
	/**
	 *  
	 *  Download rxcui codes for given ndc.
	 * 
	 * @return String
	 */
	@ApiIgnore
	@RequestMapping(value = "/download/rxcui", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downloadrxforndc()    {
		ndcRXCUIService.downloadrxforndc();
		return new ResponseEntity<>("Created...", HttpStatus.OK);
	}
	
	/**
	 *  
	 *  Download ndc codes for given rxcui.
	 * 
	 * @return String
	 */
	@ApiIgnore
	@RequestMapping(value = "/download/ndc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> downloadNDCforRxcui()    {
		rxcuiService.downloadNDCforRxcui();
		return new ResponseEntity<>("Created...", HttpStatus.OK);
	}
	
}
