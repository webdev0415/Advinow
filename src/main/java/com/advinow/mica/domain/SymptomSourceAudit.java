package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class SymptomSourceAudit {

	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	/*@JsonInclude(Include.NON_NULL)
	private Integer version;*/
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	private String multiplier;
	
	/*@JsonInclude(Include.NON_NULL)
	private String state;*/
	
	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
		
	@JsonInclude(Include.NON_NULL)
	private String action;
	
	@JsonInclude(Include.NON_NULL)
	private Long actionDate;
	
	
}
