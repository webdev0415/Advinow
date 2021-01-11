package com.advinow.mica.model;

import java.util.List;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.advinow.mica.model.enums.PolicyAction;
import com.advinow.mica.model.enums.PolicyTarget;
import com.advinow.mica.model.enums.PolicyTargetCompare;
import com.advinow.mica.model.enums.PolicyTargetOperator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public @Data class TreatmentPolicyModel {
	
		
	
	private Integer policyID;
	
	// action : (ENUM) [INCLUDE, EXCLUDE,NONE],
	
	private PolicyAction action;
		
	// target: (ENUM) [ILLNESS, SYMPTOM, PROPERTY,NONE],
	
	private PolicyTarget target =PolicyTarget.NONE;
		
	
	private String name;
	
	
	private List<String> targetDetail;
	
	
	private List<String> propertyName;
	
	//targetOperator : (ENUM) [AND, OR,NONE]	   
	
	private PolicyTargetOperator targetOperator = PolicyTargetOperator.NONE;
	
	// targetCompare : (ENUM) [NONE, EQUALS, LESSTHAN, LTEEQUAL, GTEEQUAL, GREATERTHAN]
	
	private PolicyTargetCompare targetCompare = PolicyTargetCompare.NONE;
	
	//
	private AlternativeDrugModel alternative= new AlternativeDrugModel();
	
	
	
	
	   	  
	
	
}
