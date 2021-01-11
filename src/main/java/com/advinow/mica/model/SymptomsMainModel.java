package com.advinow.mica.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomsMainModel {
	
	@JsonInclude(Include.NON_NULL)	
	List<SymptomTemplateDTO> symptoms = null;
	
	//@JsonInclude(Include.NON_NULL)
//	Map<String, Map<String, Object>>  symptomOptions = null;
	
	@JsonInclude(Include.NON_NULL)
	Map<String,	 Collection<Map<String, Object>>> symptomOptions = null;

	

	/**
	 * @return the symptoms
	 */
	public List<SymptomTemplateDTO> getSymptoms() {
		return symptoms;
	}


	/**
	 * @param symptoms the symptoms to set
	 */
	public void setSymptoms(List<SymptomTemplateDTO> symptoms) {
		this.symptoms = symptoms;
	}


	/**
	 * @return the symptomOptions
	 */
	public Map<String, Collection<Map<String, Object>>> getSymptomOptions() {
		return symptomOptions;
	}


	/**
	 * @param symptomOptions the symptomOptions to set
	 */
	public void setSymptomOptions(
			Map<String, Collection<Map<String, Object>>> symptomOptions) {
		this.symptomOptions = symptomOptions;
	}
	

}
