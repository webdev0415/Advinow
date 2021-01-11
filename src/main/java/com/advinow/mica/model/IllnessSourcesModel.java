package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public @Data class IllnessSourcesModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	private Integer version;
	
	@JsonInclude(Include.NON_NULL)
	private String state;
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<MultiplierSources> symptomSources;
	
	
}
