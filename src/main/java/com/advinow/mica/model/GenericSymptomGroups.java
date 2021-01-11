package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



public class GenericSymptomGroups extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupID;
	
	@JsonInclude(Include.NON_NULL)
	private Long updatedDate;

	@JsonInclude(Include.NON_NULL)
	List<GenericCategoryModel> categories; 
	

	public List<GenericCategoryModel> getCategories() {
		return categories;
	}



	public void setCategories(List<GenericCategoryModel> categories) {
		this.categories = categories;
	}



	public String getGroupID() {
		return groupID;
	}



	public Long getUpdatedDate() {
		return updatedDate;
	}



	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}



	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

}
