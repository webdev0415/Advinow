package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomTemplateValidate extends JSonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	private Map<String, List<String>> multiplier;
	@JsonInclude(Include.NON_NULL)
	private List<String> dataInvalidAttributes;

	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	

	public List<String> getDataInvalidAttributes() {
		return dataInvalidAttributes;
	}

	public void setDataInvalidAttributes(List<String> dataInvalidAttributes) {
		this.dataInvalidAttributes = dataInvalidAttributes;
	}

	public Map<String, List<String>> getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Map<String, List<String>> multiplier) {
		this.multiplier = multiplier;
	}

	

}
