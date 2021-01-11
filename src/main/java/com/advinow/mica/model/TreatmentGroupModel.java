package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentGroupModel extends JSonModel {

//	@JsonInclude(Include.NON_NULL)
	private String groupName;

//	@JsonInclude(Include.NON_NULL)
	private String groupCode;
	
	@JsonInclude(Include.NON_NULL)
	@JsonIgnore
	private Integer rank;

	@JsonInclude(Include.NON_EMPTY)
	private List<DrugDescModel> drugDescriptions = new ArrayList<DrugDescModel>();
	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<DrugDescModel> drugs = new ArrayList<DrugDescModel>();
	

	@JsonInclude(Include.NON_EMPTY)
	private List<NonDrugDescModel> descriptions= new ArrayList<NonDrugDescModel>();

	@JsonInclude(Include.NON_EMPTY)
	private List<NonDrugDescModel> nonDrugs= new ArrayList<NonDrugDescModel>();
	
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}




	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public List<DrugDescModel> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<DrugDescModel> drugs) {
		this.drugs = drugs;
	}

	public List<NonDrugDescModel> getNonDrugs() {
		return nonDrugs;
	}

	public void setNonDrugs(List<NonDrugDescModel> nonDrugs) {
		this.nonDrugs = nonDrugs;
	}

	public List<DrugDescModel> getDrugDescriptions() {
		return drugDescriptions;
	}

	public void setDrugDescriptions(List<DrugDescModel> drugDescriptions) {
		this.drugDescriptions = drugDescriptions;
	}

	public List<NonDrugDescModel> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<NonDrugDescModel> descriptions) {
		this.descriptions = descriptions;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}


}
