package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.advinow.mica.model.JSonModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentDocModel  extends JSonModel {
	
	String type;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<DrugDocModel> details = new ArrayList<DrugDocModel>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DrugDocModel> getDetails() {
		return details;
	}

	public void setDetails(List<DrugDocModel> details) {
		this.details = details;
	}

}