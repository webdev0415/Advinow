package com.advinow.mica.domain;


import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class Policy extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Integer policyID;

	public Integer getPolicyID() {
		return policyID;
	}

	public void setPolicyID(Integer policyID) {
		this.policyID = policyID;
	}

}
