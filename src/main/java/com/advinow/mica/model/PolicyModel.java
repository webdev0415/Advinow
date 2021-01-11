/**
 * 
 */
package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * @author Govinda Reddy
 *
 */

public class PolicyModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Integer policyID;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> symptoms;

	public Integer getPolicyID() {
		return policyID;
	}

	public void setPolicyID(Integer policyID) {
		this.policyID = policyID;
	}

	public List<String> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List<String> symptoms) {
		this.symptoms = symptoms;
	}
	
	
	
}
