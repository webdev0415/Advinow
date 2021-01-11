package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentMainDocModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<TreatmentDocModel> treatments;

	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	public List<TreatmentDocModel> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<TreatmentDocModel> treatments) {
		this.treatments = treatments;
	}

	
	
	
}
