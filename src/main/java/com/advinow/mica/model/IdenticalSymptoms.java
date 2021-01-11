package com.advinow.mica.model;

import java.util.List;

public class IdenticalSymptoms {

	public IdenticalSymptoms(List<String> icd10Code) {

		super();
		this.icd10Code=icd10Code;
	}
     
	public	IdenticalSymptoms(){
		super();
	}


	public List<String> icd10Code;


	public List<String> getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(List<String> icd10Code) {
		this.icd10Code = icd10Code;
	}




}
