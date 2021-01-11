/**
 * 
 */
package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class LogicalSymptomGroupsRef extends Neo4jEntity {

	
	@JsonInclude(Include.NON_NULL)
	private Integer groupID;

	@JsonInclude(Include.NON_NULL)
	private Integer parentID;
	
	@JsonInclude(Include.NON_NULL)
	private String type;
	
	@JsonInclude(Include.NON_NULL)
	private String logicalGroupName;

	
	/**
	 * @return the parentID
	 */
	public Integer getParentID() {
		return parentID;
	}

	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogicalGroupName() {
		return logicalGroupName;
	}

	public void setLogicalGroupName(String logicalGroupName) {
		this.logicalGroupName = logicalGroupName;
	}

		
	
}
