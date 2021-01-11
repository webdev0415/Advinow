package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class BodyParts extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
 	private String bodypartID;
	
	@JsonInclude(Include.NON_NULL)
	private String groupName;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> position= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> es_position= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	private List <BodyParts> subParts = new ArrayList<BodyParts>();
		
	@JsonInclude(Include.NON_EMPTY)
	private List<String> bodyPartsCodes= new ArrayList<String>();
	
	@JsonInclude(Include.NON_NULL)
	private Integer parentID;

	public String getBodypartID() {
		return bodypartID;
	}

	public void setBodypartID(String bodypartID) {
		this.bodypartID = bodypartID;
	}

	public List<String> getPosition() {
		return position;
	}

	public void setPosition(List<String> position) {
		this.position = position;
	}

	public List<BodyParts> getSubParts() {
		return subParts;
	}

	public void setSubParts(List<BodyParts> subParts) {
		this.subParts = subParts;
	}

	public List<String> getEs_position() {
		return es_position;
	}

	public void setEs_position(List<String> es_position) {
		this.es_position = es_position;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	/**
	 * @return the bodyPartsCodes
	 */
	public List<String> getBodyPartsCodes() {
		return bodyPartsCodes;
	}

	/**
	 * @param bodyPartsCodes the bodyPartsCodes to set
	 */
	public void setBodyPartsCodes(List<String> bodyPartsCodes) {
		this.bodyPartsCodes = bodyPartsCodes;
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

	

}
