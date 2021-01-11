package com.advinow.mica.model;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.advinow.mica.model.enums.Units;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public @Data class DrugDosageModel  {
	private Integer rxcui;
	private String rxcuiDesc;
	private String form;
	private String route;
	private String frequency;
	private String dispenseForm;
	private String directions;
	private Integer quantity;
	private Boolean prn;
	private Boolean daw;
	private Integer strength;
	private String amount;
	private String measurement;
	private Units unit;
	
		
}
