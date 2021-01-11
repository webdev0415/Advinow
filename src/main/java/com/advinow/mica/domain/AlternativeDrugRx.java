package com.advinow.mica.domain;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class AlternativeDrugRx  {

	@Id
	@GeneratedValue
	private Long id;
	
	// active ingredient rxcui or generic rxcui
	@JsonInclude(Include.NON_NULL)
	private Integer ingredientRxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String ingredientRxcuiDesc;
	
	@JsonInclude(Include.NON_NULL)
	private String name;
			
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "ALTDRUG_HAS_DOSAGE", direction = Relationship.OUTGOING)
	private DrugDosage dosage;
		
}
