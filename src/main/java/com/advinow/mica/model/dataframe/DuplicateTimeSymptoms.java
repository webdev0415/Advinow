package com.advinow.mica.model.dataframe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

public @Data class DuplicateTimeSymptoms {
	

	@JsonInclude(Include.NON_NULL)
	public String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	public Integer version;
				
	@JsonInclude(Include.NON_NULL)
	public String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	public List<String> multiplier;
	
	@JsonInclude(Include.NON_NULL)
	public String timeFrame;
	

	public Double likelihood;
	public Long  dmId;
	Boolean delete = true;
	public String key; 
	public Long  dsd;
	
	
}
