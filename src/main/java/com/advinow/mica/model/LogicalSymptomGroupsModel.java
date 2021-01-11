/**
 * 
 */
package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.advinow.mica.domain.queryresult.SymptomGroupResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * @author Govinda Reddy
 *
 */

public class LogicalSymptomGroupsModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Integer groupID;
	
	@JsonInclude(Include.NON_NULL)
	private Integer parentID;
	
	@JsonInclude(Include.NON_NULL)
	private String type;

	@JsonInclude(Include.NON_EMPTY)
	private List <LogicalSymptomGroupsModel> subGroups = new ArrayList<LogicalSymptomGroupsModel>();
	
	//@JsonInclude(Include.NON_EMPTY)
	//private List<String> symptoms = new ArrayList<String>();

	private List<SymptomGroupResult> symptoms = new ArrayList<SymptomGroupResult>();
	
	
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
	 * @return the subGroups
	 */
	public List<LogicalSymptomGroupsModel> getSubGroups() {
		return subGroups;
	}

	/**
	 * @param subGroups the subGroups to set
	 */
	public void setSubGroups(List<LogicalSymptomGroupsModel> subGroups) {
		this.subGroups = subGroups;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SymptomGroupResult> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List<SymptomGroupResult> symptoms) {
		this.symptoms = symptoms;
	} 

	
	
}
