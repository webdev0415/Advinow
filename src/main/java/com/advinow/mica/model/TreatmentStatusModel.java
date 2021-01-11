package com.advinow.mica.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class TreatmentStatusModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
