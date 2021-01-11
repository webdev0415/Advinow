package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomConfig extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private String type;  

	@JsonInclude(Include.NON_EMPTY)
	private List<String> includeSymptoms= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> includeCategories= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> ignoreSymptoms  = new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> includeIllnesses  = new ArrayList<String>();

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the includeSymptoms
	 */
	public List<String> getIncludeSymptoms() {
		return includeSymptoms;
	}

	/**
	 * @param includeSymptoms the includeSymptoms to set
	 */
	public void setIncludeSymptoms(List<String> includeSymptoms) {
		this.includeSymptoms = includeSymptoms;
	}

	/**
	 * @return the includeCategories
	 */
	public List<String> getIncludeCategories() {
		return includeCategories;
	}

	/**
	 * @param includeCategories the includeCategories to set
	 */
	public void setIncludeCategories(List<String> includeCategories) {
		this.includeCategories = includeCategories;
	}

	/**
	 * @return the ignoreSymptoms
	 */
	public List<String> getIgnoreSymptoms() {
		return ignoreSymptoms;
	}

	/**
	 * @param ignoreSymptoms the ignoreSymptoms to set
	 */
	public void setIgnoreSymptoms(List<String> ignoreSymptoms) {
		this.ignoreSymptoms = ignoreSymptoms;
	}

	/**
	 * @return the includeIllnesses
	 */
	public List<String> getIncludeIllnesses() {
		return includeIllnesses;
	}

	/**
	 * @param includeIllnesses the includeIllnesses to set
	 */
	public void setIncludeIllnesses(List<String> includeIllnesses) {
		this.includeIllnesses = includeIllnesses;
	}

	
	
}
