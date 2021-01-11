package com.advinow.mica.model;



import lombok.Data;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public @Data class AlternativeDrugModel  {

	
	
	// active ingredient rxcui or generic rxcui
	@JsonInclude(Include.NON_NULL)
	private Integer ingredientRxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String ingredientRxcuiDesc;
	
	//@JsonInclude(Include.NON_NULL)
	private String drugName;

	//@JsonInclude(Include.NON_NULL)
	private DrugDosageModel dosageRecommendation = new DrugDosageModel();
		
}
