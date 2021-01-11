package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomType extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	@JsonInclude(Include.NON_NULL)
	private String symptomType;
	@JsonInclude(Include.NON_NULL)
  	private String listName;
	@JsonInclude(Include.NON_NULL)
    private Long updatedDate;
	@JsonInclude(Include.NON_EMPTY)
	private List<String> listValues;
	@JsonInclude(Include.NON_NULL)
	private String image;
	@JsonInclude(Include.NON_EMPTY)
	private List<String> range; // measurements


	public String getSymptomType() {
		return symptomType;
	}

	public void setSymptomType(String symptomType) {
		this.symptomType = symptomType;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public List<String> getListValues() {
		return listValues;
	}

	public void setListValues(List<String> listValues) {
		this.listValues = listValues;
	}

	public List<String> getRange() {
		return range;
	}

	public void setRange(List<String> range) {
		this.range = range;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	
	
}
