package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class RxNorms extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	Integer rxcui;
	
	@JsonInclude(Include.NON_NULL)
	String addedBy;

	public Integer getRxcui() {
		return rxcui;
	}

	public void setRxcui(Integer rxcui) {
		this.rxcui = rxcui;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

}