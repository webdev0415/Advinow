package com.advinow.mica.domain.queryresult;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@QueryResult
public class SymptomValidatorResultEnitity {
	
	@JsonInclude(Include.NON_EMPTY)
	private String group;
	
	@JsonInclude(Include.NON_EMPTY)
	private String categoryID;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> bodyPartLocations;
	
	@JsonInclude(Include.NON_EMPTY)
	private String code;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> descriptors;
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomType;
	
}
