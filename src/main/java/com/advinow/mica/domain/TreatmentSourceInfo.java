package com.advinow.mica.domain;



import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class TreatmentSourceInfo {
	
	@JsonIgnore
	@Id
	@GeneratedValue
	private Long id;
		
	@JsonInclude(Include.NON_NULL)
	private String source;


	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
	
	@JsonInclude(Include.NON_NULL)
	private String sourceType;
	
	@JsonInclude(Include.NON_NULL)
	private String sourceTitle;
		
	@JsonInclude(Include.NON_NULL)
	private Integer rank=1;
}
