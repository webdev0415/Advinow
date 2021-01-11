/**
 * 
 */
package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Developer
 *
 */
public class MICASymptomsGroup implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@JsonInclude(Include.NON_EMPTY)
	List<SymptomGroups> symptomGroups;


	public List<SymptomGroups> getSymptomGroups() {
		return symptomGroups;
	}


	public void setSymptomGroups(List<SymptomGroups> symptomGroups) {
		this.symptomGroups = symptomGroups;
	}


}
