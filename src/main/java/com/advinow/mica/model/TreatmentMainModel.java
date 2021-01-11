package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentMainModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	   // @DateString("yy-MM-dd")  
	private Long modifiedDate;
		
	@JsonInclude(Include.NON_NULL)
	private Long createdDate; 
	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<TreatmentModel> treatments;

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

	public List<TreatmentModel> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<TreatmentModel> treatments) {
		this.treatments = treatments;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	
	
	
	
}
