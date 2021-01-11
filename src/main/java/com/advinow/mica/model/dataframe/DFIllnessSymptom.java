package com.advinow.mica.model.dataframe;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
@SuppressWarnings("serial")
@QueryResult
public @Data class DFIllnessSymptom implements Serializable {
	
	public String icd10Code;

	public Long version;

	public String state;

	public List<String> symptoms;
	
	@JsonInclude(Include.NON_NULL)
	private String dfstatus;
	
	public DFIllnessSymptom(String icd10Code, Long version,
			String state, List<String> symptoms) {
		super();
		this.icd10Code = icd10Code;
		this.version = version;
		this.state = state;
		this.symptoms = symptoms;
	
	}
	public DFIllnessSymptom() {

	}

	
	

}
