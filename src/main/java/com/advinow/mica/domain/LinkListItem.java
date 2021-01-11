package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class LinkListItem {
	
	@Id
	@GeneratedValue
	//@JsonIgnore
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private String code;	
	
}
