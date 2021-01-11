package com.advinow.mica.controller;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.advinow.mica.services.MICAGenericService;

@RestController
@RequestMapping("/mita/")
@Api(value="/mita", description=" MICA and MITA integration services")
public class MicaMitaIntegrationController {

	@Autowired
	MICAGenericService mICAGenericService;

	@ApiIgnore
	@RequestMapping(value = "/resetCollectorData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  resetCollectorIllnessData() {
	Integer userId = 312;
	String status =	mICAGenericService.resetCollectorIllnessData(userId);
	return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@ApiIgnore
	@RequestMapping(value = "/resetReviewerData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>  resetReviewerIllnessData() {
	Integer userId = 313;
	String status =	mICAGenericService.resetReviewerIllnessData(userId);
	return new ResponseEntity<>(status, HttpStatus.OK);
	}

	/**

	   @ApiIgnore
		@RequestMapping(value = "/resetCollectorData/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String>  resetCollectorIllnessData(@PathVariable("userId") Integer userId) {
		String status =	mICAGenericService.resetCollectorIllnessData(userId);
		return new ResponseEntity<>(status, HttpStatus.OK);
		}

		@ApiIgnore
		@RequestMapping(value = "/resetReviewerData/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String>  resetReviewerIllnessData(@PathVariable("userId") Integer userId) {
		String status =	mICAGenericService.resetReviewerIllnessData(userId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}


	**/

}
