package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class BadSymptoms  extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private String category;
	
	@JsonInclude(Include.NON_NULL)
	private String invalidTypeData;
	
	@JsonInclude(Include.NON_NULL)
	private String noTypeData;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> invalidBodyLocations= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> invalidListValues= new ArrayList<String>();

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getInvalidTypeData() {
		return invalidTypeData;
	}

	public void setInvalidTypeData(String invalidTypeData) {
		this.invalidTypeData = invalidTypeData;
	}

	public String getNoTypeData() {
		return noTypeData;
	}

	public void setNoTypeData(String noTypeData) {
		this.noTypeData = noTypeData;
	}

	public List<String> getInvalidBodyLocations() {
		return invalidBodyLocations;
	}

	public void setInvalidBodyLocations(List<String> invalidBodyLocations) {
		this.invalidBodyLocations = invalidBodyLocations;
	}

	public List<String> getInvalidListValues() {
		return invalidListValues;
	}

	public void setInvalidListValues(List<String> invalidListValues) {
		this.invalidListValues = invalidListValues;
	}
	
}
