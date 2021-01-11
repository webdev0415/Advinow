package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class LogicalSymptomGroups extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Integer groupID;
	
	@JsonInclude(Include.NON_NULL)
	String groupFlag="C";

	/**
	 * @return the groupID
	 */
	public Integer getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public String getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}
	

}
