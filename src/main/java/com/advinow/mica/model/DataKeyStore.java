package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DataKeyStore extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
    String title;
	
	@JsonInclude(Include.NON_NULL)
    String es_title;
	
	//@JsonInclude(Include.NON_EMPTY)
	List<ValueStore> values;// = new ArrayList<ValueStore>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ValueStore> getValues() {
		return values;
	}

	public void setValues(List<ValueStore> values) {
		this.values = values;
	}

	public String getEs_title() {
		return es_title;
	}

	public void setEs_title(String es_title) {
		this.es_title = es_title;
	}

}
