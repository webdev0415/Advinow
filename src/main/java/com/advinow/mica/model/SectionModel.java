package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SectionModel extends JSonModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sectionID;

	@JsonInclude(Include.NON_EMPTY)
	List<CategoryModel> categories;

	public String getSectionID() {
		return sectionID;
	}

	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}

	public List<CategoryModel> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryModel> categories) {
		this.categories = categories;
	} 

}
