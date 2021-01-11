package com.advinow.mica.domain;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class DrugDosage  {

	@Id
	@GeneratedValue
	private Long id;
	
	// this is actual rxcui codes
	@JsonInclude(Include.NON_NULL)
	private Integer rxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String rxcuiDesc;
			
	@JsonInclude(Include.NON_NULL)
	private String form;
	
	@JsonInclude(Include.NON_NULL)
	private String route;
	
	@JsonInclude(Include.NON_NULL)
	private String frequency;

	@JsonInclude(Include.NON_NULL)
	private String dispenseForm;

	@JsonInclude(Include.NON_NULL)
	private String directions;

	@JsonInclude(Include.NON_NULL)
	private Integer quantity;

	@JsonInclude(Include.NON_NULL)
	private Boolean prn;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean daw;
	
	@JsonInclude(Include.NON_NULL)
	private Integer strength;

	@JsonInclude(Include.NON_NULL)
	private String amount;
	

	@JsonInclude(Include.NON_NULL)
	private String measurement;
	
	@JsonInclude(Include.NON_NULL)
	private String units;
}
