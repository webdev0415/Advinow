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
public @Data class TimeSymptoms implements Serializable {


	@JsonInclude(Include.NON_NULL)
	public String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	public String icd10Name;
	
	
	@JsonInclude(Include.NON_NULL)
	public Integer version;
	
	
	@JsonInclude(Include.NON_NULL)
	public String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	public String symptomName;
	
	
	@JsonInclude(Include.NON_NULL)
	public List<String> multiplier;
	
	@JsonInclude(Include.NON_NULL)
	public String timeFrame;
	
	@JsonInclude(Include.NON_NULL)
	public Double likelihood;
	
	@JsonInclude(Include.NON_NULL)
	public Boolean mdc;
		
	@JsonInclude(Include.NON_NULL)
	public Long  dmId;

	@JsonInclude(Include.NON_NULL)
	public Long  dsId;
	
	
}
