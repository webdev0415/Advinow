package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class TreatmentSourceAudit {

	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
		
	@JsonInclude(Include.NON_NULL)
	private Integer typeDescID;
	
	@JsonInclude(Include.NON_NULL)
	private String drugName;
				
	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
		
	@JsonInclude(Include.NON_NULL)
	private String action;
	
	@JsonInclude(Include.NON_NULL)
	private Long actionDate;
	
	
}
