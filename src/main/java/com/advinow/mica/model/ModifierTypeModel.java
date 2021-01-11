package com.advinow.mica.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ModifierTypeModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Integer likelihood=0;
	
	@JsonInclude(Include.NON_NULL)
	private String modifierValue;
	
	@JsonInclude(Include.NON_NULL)
	private ScaleModel scale;
	
	public ScaleModel getScale() {
		return scale;
	}

	public void setScale(ScaleModel scale) {
		this.scale = scale;
	}

	public Integer getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(Integer likelihood) {
		this.likelihood = likelihood;
	}

	public String getModifierValue() {
		return modifierValue;
	}

	public void setModifierValue(String modifierValue) {
		this.modifierValue = modifierValue;
	}

	

	
}
