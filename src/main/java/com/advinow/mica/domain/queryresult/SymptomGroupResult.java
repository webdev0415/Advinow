package com.advinow.mica.domain.queryresult;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@QueryResult
public @Data class SymptomGroupResult {
	
	@JsonInclude(Include.NON_NULL)
	String groupFlag;

	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
}
