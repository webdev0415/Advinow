package com.advinow.mica.domain;

import java.util.List;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class TreatmentPolicy {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private Integer policyID;
	
	@JsonInclude(Include.NON_NULL)
	private String action;
		
	// target: (ENUM) [ILLNESS, SYMPTOM, PROPERTY],
	@JsonInclude(Include.NON_NULL)
	private String target;
		
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> targetDetail;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> propertyName;
	
	// (ENUM) [AND, OR],	   
	@JsonInclude(Include.NON_NULL)
	private String targetOperator;
	
	//(ENUM) [NONE, EQUALS, LESSTHAN, LTEEQUAL, GTEEQUAL, GREATERTHAN]
	@JsonInclude(Include.NON_NULL)
	private String targetCompare;	
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "ALTERNATIVE_DRUG", direction = Relationship.OUTGOING)
	private AlternativeDrugRx alternativeDrug;
	   	  
	
	
}
