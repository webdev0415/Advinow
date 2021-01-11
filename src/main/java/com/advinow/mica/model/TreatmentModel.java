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
public class TreatmentModel  extends JSonModel {

	//@JsonInclude(Include.NON_NULL)
	private Integer typeID;

	/*@JsonInclude(Include.NON_EMPTY)
	private List<NonDrugDescModel> descriptions= new ArrayList<NonDrugDescModel>();*/

	//@JsonInclude(Include.NON_EMPTY)
	private List<TreatmentGroupModel> groups = new ArrayList<TreatmentGroupModel>();

	public Integer getTypeID() {
		return typeID;
	}
	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}
	/*public List<NonDrugDescModel> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<NonDrugDescModel> descriptions) {
		this.descriptions = descriptions;
	}*/
	public List<TreatmentGroupModel> getGroups() {
		return groups;
	}
	public void setGroups(List<TreatmentGroupModel> groups) {
		this.groups = groups;
	}


}
