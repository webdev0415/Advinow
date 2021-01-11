package com.advinow.mica.model;

import com.advinow.mica.model.JSonModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DrugDocModel  extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private String nameDetails;
	@JsonInclude(Include.NON_NULL)
	private Integer priority;
	@JsonInclude(Include.NON_NULL)
	private Integer rank;
	@JsonInclude(Include.NON_NULL)
	private String description;
	
	@JsonInclude(Include.NON_NULL)
	private String groupName;
	
	@JsonInclude(Include.NON_NULL)
	private String groupCode;
	
/*	@JsonInclude(Include.NON_NULL)
	private Integer rxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String rxcuiDesc;*/
	
	@JsonInclude(Include.NON_NULL)
	private Integer ingredientRxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String ingredientRxcuiDesc;
		
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getNameDetails() {
		return nameDetails;
	}
	public void setNameDetails(String nameDetails) {
		this.nameDetails = nameDetails;
	}
	/*public String getRxcuiDesc() {
		return rxcuiDesc;
	}
	public void setRxcuiDesc(String rxcuiDesc) {
		this.rxcuiDesc = rxcuiDesc;
	}
	public Integer getRxcui() {
		return rxcui;
	}
	public void setRxcui(Integer rxcui) {
		this.rxcui = rxcui;
	}*/
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public Integer getIngredientRxcui() {
		return ingredientRxcui;
	}
	public void setIngredientRxcui(Integer ingredientRxcui) {
		this.ingredientRxcui = ingredientRxcui;
	}
	public String getIngredientRxcuiDesc() {
		return ingredientRxcuiDesc;
	}
	public void setIngredientRxcuiDesc(String ingredientRxcuiDesc) {
		this.ingredientRxcuiDesc = ingredientRxcuiDesc;
	}

}
