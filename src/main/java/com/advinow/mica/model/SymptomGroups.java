package com.advinow.mica.model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



public class SymptomGroups extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupID;
	
	@JsonInclude(Include.NON_NULL)
	private Long updatedDate;

	@JsonInclude(Include.NON_NULL)
	List<CategoryModel> categories; 
	
	@JsonInclude(Include.NON_EMPTY)
	List<SectionModel> sections; 

	// for Response
	@JsonInclude(Include.NON_EMPTY)
	private Hashtable<String, DataKeyStore> dataStoreRefTypes= new Hashtable<String, DataKeyStore>();

	public List<CategoryModel> getCategories() {
		return categories;
	}



	public void setCategories(List<CategoryModel> categories) {
		this.categories = categories;
	}



	public String getGroupID() {
		return groupID;
	}


	public Hashtable<String, DataKeyStore> getDataStoreRefTypes() {
		return dataStoreRefTypes;
	}


	public void setDataStoreRefTypes(
			Hashtable<String, DataKeyStore> dataStoreRefTypes) {
		this.dataStoreRefTypes = dataStoreRefTypes;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}



	public List<SectionModel> getSections() {
		return sections;
	}



	public void setSections(List<SectionModel> sections) {
		this.sections = sections;
	}



	public Long getUpdatedDate() {
		return updatedDate;
	}



	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

}
