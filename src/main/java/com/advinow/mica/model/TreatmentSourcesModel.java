package com.advinow.mica.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public @Data class TreatmentSourcesModel {
	
	
	@JsonInclude(Include.NON_NULL)
	private Long sourceRefDate; 
	
	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
	
	@JsonInclude(Include.NON_NULL)
	private String addedBy;

/*	@JsonInclude(Include.NON_NULL)
	private Boolean enable;*/
		
	@JsonInclude(Include.NON_NULL)
	private String sourceInfo;	
	
	@JsonInclude(Include.NON_NULL)
	private Boolean verified = false;

}
