package com.advinow.mica.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CategoryModel extends JSonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1208797818872006701L;
	
	@JsonInclude(Include.NON_EMPTY)
	private String categoryID;

	//@JsonInclude(Include.NON_EMPTY)
	List<Symptoms> symptoms = new ArrayList<Symptoms>();
	
	public List<Symptoms> getSymptoms() {
		return symptoms;
	}


	public void setSymptoms(List<Symptoms> symptoms) {
		this.symptoms = symptoms;
	}


	public String getCategoryID() {
		return categoryID;
	}


	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

}
