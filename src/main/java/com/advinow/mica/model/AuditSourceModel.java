package com.advinow.mica.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public @Data class AuditSourceModel {
		
	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
	
	// posible 
	@JsonInclude(Include.NON_NULL)
	private String action;
	


}
