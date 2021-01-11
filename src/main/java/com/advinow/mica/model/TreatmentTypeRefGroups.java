package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

public class TreatmentTypeRefGroups extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TreatmentTypeRefModel> treatmentTypes;
	
	public List<TreatmentTypeRefModel> getTreatmentTypes() {
		return treatmentTypes;
	}
	public void setTreatmentTypes(List<TreatmentTypeRefModel> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
}
