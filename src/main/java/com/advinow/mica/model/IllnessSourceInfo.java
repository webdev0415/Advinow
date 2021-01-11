package com.advinow.mica.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class IllnessSourceInfo {
	
	//@JsonInclude(Include.NON_NULL)
	public String icd10Code;

//	@JsonInclude(Include.NON_NULL)
	Set<SymptomSource>  symptoms= new HashSet<SymptomSource>();

	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	public Set<SymptomSource> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(Set<SymptomSource> symptoms) {
		this.symptoms = symptoms;
	}

}
