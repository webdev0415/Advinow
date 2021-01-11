package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Developer
 *
 */
public class ICD10CodesModel extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	List <ICD10CodeModel> illnesses;

	public List<ICD10CodeModel> getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(List<ICD10CodeModel> illnesses) {
		this.illnesses = illnesses;
	}
	

}
