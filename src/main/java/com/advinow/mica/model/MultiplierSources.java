package com.advinow.mica.model;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public @Data class MultiplierSources {
	
	@JsonInclude(Include.NON_NULL)
	String multiplier;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<AuditSourceModel> sourceInfo;
	
	

}
