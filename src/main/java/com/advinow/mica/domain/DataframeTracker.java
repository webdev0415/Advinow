package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class DataframeTracker {

	@Id
	@GeneratedValue
	private Long id;

	@JsonInclude(Include.NON_NULL)
	private String url;
	
	//@JsonInclude(Include.NON_EMPTY)
	private List<String> icd10Codes = new ArrayList<String>();
	
	
	
}
