package com.advinow.mica.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * @author Govinda Reddy
 *
 */
public class SnomedCodesModel {

	@JsonInclude(Include.NON_NULL)
	private String code;

	@JsonInclude(Include.NON_NULL)
	private String name;

	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	private String conceptID;
	
	@JsonInclude(Include.NON_NULL)
	private String expression;

	private String listValueCode;


	private String listValue;


	public String getConceptID() {
		return conceptID;
	}

	public void setConceptID(String conceptID) {
		this.conceptID = conceptID;
	}

	public String getListValueCode() {
		return listValueCode;
	}

	public void setListValueCode(String listValueCode) {
		this.listValueCode = listValueCode;
	}

	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}


}
