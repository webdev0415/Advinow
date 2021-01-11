package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings("serial")
public @Data class TreatmentAuditSourcesModel implements Serializable {

	@JsonInclude(Include.NON_NULL)
	private String icd10Code;

	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<AuditTreatmentTypeModel> treatments;
		
	
	
}
