package com.advinow.mica.domain.queryresult;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@QueryResult
public @Data class SymptomsLabOrders {
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomID;
	
	//@JsonInclude(Include.NON_EMPTY)
	private List<String> labsOrdered = new ArrayList<String>();

}
