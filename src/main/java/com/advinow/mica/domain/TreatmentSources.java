package com.advinow.mica.domain;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class TreatmentSources  {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private Long sourceRefDate; 
	
	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
	
	@JsonInclude(Include.NON_NULL)
	private String addedBy="Doctor";

	/*@JsonInclude(Include.NON_NULL)
	private Boolean enable;*/
		
	@JsonInclude(Include.NON_NULL)
	private String sourceInfo;	
	
	private Boolean verified = false;
	
	@JsonInclude(Include.NON_NULL)
	private Integer rank;	


}
