package com.advinow.mica.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentTypeRefModel extends JSonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude(Include.NON_NULL)
	private Integer typeID;
	@JsonInclude(Include.NON_NULL)
	private String type;
	
	//@JsonInclude(Include.NON_EMPTY)
	//@JsonInclude(Include.NON_NULL)
	private List<TreatmentTypeRefDescModel> treatmentTypeDesc= new ArrayList<TreatmentTypeRefDescModel>();


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TreatmentTypeRefDescModel> getTreatmentTypeDesc() {
		return treatmentTypeDesc;
	}

	public void setTreatmentTypeDesc(List<TreatmentTypeRefDescModel> treatmentTypeDesc) {
		this.treatmentTypeDesc = treatmentTypeDesc;
	}

	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}


}
