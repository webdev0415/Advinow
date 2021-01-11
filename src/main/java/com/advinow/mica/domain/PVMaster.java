package com.advinow.mica.domain;

import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.Relationship;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



public @Data class PVMaster {
	
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	Integer pvid;
	
	@JsonInclude(Include.NON_NULL)
	String description;

	@JsonInclude(Include.NON_NULL)
	String strength;
	
	@JsonInclude(Include.NON_NULL)
	String strength_units;
	
	@JsonInclude(Include.NON_NULL)
	String route;
	
	@JsonInclude(Include.NON_NULL)
	String status;

	@JsonInclude(Include.NON_NULL)
	String dosage;
	
	//@JsonInclude(Include.NON_NULL)
	Integer rxcui;
		
	
	@JsonInclude(Include.NON_NULL)
	String marketing_status;
	
/*	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GENERIC_INGREDIENTS", direction = Relationship.OUTGOING)
	private Set<PVGenericRx> genericRxNorms;*/
	
}
