package com.advinow.mica.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public @Data class RxNormsModel {
	
	@JsonInclude(Include.NON_NULL)
	Integer rxcui;
	
	@JsonInclude(Include.NON_NULL)
	String addedBy;

}
