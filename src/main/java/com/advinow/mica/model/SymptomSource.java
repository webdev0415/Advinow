package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomSource {
	
	String symptomID;
	String symptomName;
	
	@JsonInclude(Include.NON_NULL)
	List<SymptmSourceInfoModel> sourceInformation;


	/**
	 * @return the symptomID
	 */
	
	public String getSymptomID() {
		return symptomID;
	}

	/**
	 * @param symptomID the symptomID to set
	 */
	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	/**
	 * @return the sourceInformation
	 */
	public List<SymptmSourceInfoModel> getSourceInformation() {
		return sourceInformation;
	}

	/**
	 * @param sourceInformation the sourceInformation to set
	 */
	public void setSourceInformation(List<SymptmSourceInfoModel> sourceInformation) {
		this.sourceInformation = sourceInformation;
	}

	/**
	 * @return the symptomName
	 */
	public String getSymptomName() {
		return symptomName;
	}

	/**
	 * @param symptomName the symptomName to set
	 */
	public void setSymptomName(String symptomName) {
		this.symptomName = symptomName;
	}

	
	

}
