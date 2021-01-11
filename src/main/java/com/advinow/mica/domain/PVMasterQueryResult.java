package com.advinow.mica.domain;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@QueryResult
public @Data class PVMasterQueryResult {
	
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	Integer pvid;
	
	@JsonInclude(Include.NON_NULL)
	String description;

	@JsonInclude(Include.NON_NULL)
	String strength;
	
	@JsonInclude(Include.NON_NULL)
	String strength_units;
	
	@JsonInclude(Include.NON_NULL)
	String route;

	@JsonInclude(Include.NON_NULL)
	String dosage;
	
	@JsonInclude(Include.NON_NULL)
	String marketing_status;
	
	//@JsonInclude(Include.NON_NULL)
	Integer rxcui;
	
	@JsonIgnore
	Float score;
	
	@JsonInclude(Include.NON_NULL)
	List<Integer> genericRxcui = null;
	
}
