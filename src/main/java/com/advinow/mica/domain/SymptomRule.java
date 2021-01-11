package com.advinow.mica.domain;



import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NodeEntity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public @Data class SymptomRule {

	@Id
	@GeneratedValue
	private Long id;

	@JsonInclude(Include.NON_NULL)
	private String name;

	@JsonInclude(Include.NON_NULL)
	private String es_name;

	@JsonInclude(Include.NON_NULL)
	private Integer ruleID;

	/*@JsonInclude(Include.NON_NULL)
	private String tag;*/

	@JsonInclude(Include.NON_NULL)
	private String relation;

	@JsonInclude(Include.NON_NULL)
	private Long lastUpdated; 

	@JsonInclude(Include.NON_NULL)
	private String direction;

	@JsonInclude(Include.NON_NULL)
	private Long startID; 

	@JsonInclude(Include.NON_NULL)
	private Long endID; 

}
