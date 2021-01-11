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

import com.advinow.mica.domain.PolicyRef;
import com.advinow.mica.services.TreatmentPolicyService;

/**
 * @author Govinda Reddy
 *
 */
@RestController
@RequestMapping("/policies")
@Api(value="/policies", description="CRUD for Treatment Policies")
public class PolicyController {
	
	@Autowired
	TreatmentPolicyService treatmentPolicyService;
	
	/**
	 * Returs all the treatment policies.
	 * 
	 * @return List<TreatmentPolicyRef>
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PolicyRef>> getPolicies() {
		List<PolicyRef> groups = treatmentPolicyService.getPolicies();
	   return new ResponseEntity<>(groups, HttpStatus.OK);
	}
	
	/**
	 * Creates new policy
	 * 
	 * @param PolicyRef
	 * @return TreatmentPolicyRef
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PolicyRef> createPolicy(@RequestBody PolicyRef tretmentPolicy ) {
		PolicyRef policy = treatmentPolicyService.createPolicy(tretmentPolicy);
	   return new ResponseEntity<>(policy, HttpStatus.CREATED);
	}
	
	
	
	
	/**
	 * 
	 * @param policyID
	 * @return String
	 */
	@RequestMapping(value = "/{policyID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deletePolicy(@PathVariable("policyID") Integer policyID) {
	String str = treatmentPolicyService.delete(policyID);
	   return new ResponseEntity<>(str, HttpStatus.OK);
	}
}
