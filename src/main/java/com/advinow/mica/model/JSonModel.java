package com.advinow.mica.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
public class JSonModel {
	

	@JsonInclude(Include.NON_NULL)
	private Integer id;
	
	
	@JsonInclude(Include.NON_NULL)
	private String code;
	

	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private String es_name;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEs_name() {
		return es_name;
	}

	public void setEs_name(String es_name) {
		this.es_name = es_name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
