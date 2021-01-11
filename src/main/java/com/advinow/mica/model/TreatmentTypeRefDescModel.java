package com.advinow.mica.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TreatmentTypeRefDescModel  extends JSonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude(Include.NON_NULL)
     private String shortName;
	@JsonInclude(Include.NON_NULL)
	private String longName;
	@JsonInclude(Include.NON_NULL)
	private Integer priority;
	@JsonInclude(Include.NON_NULL)
	private Boolean defaultValue;
	@JsonInclude(Include.NON_NULL)
	private Integer typeDescID;
	@JsonInclude(Include.NON_NULL)
    private String description;
//	@JsonInclude(Include.NON_NULL)
	private List<Long> conceptID;
//	@JsonInclude(Include.NON_NULL)
	private List<String> cptCode;
	

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public Integer getTypeDescID() {
		return typeDescID;
	}

	public void setTypeDescID(Integer typeDescID) {
		this.typeDescID = typeDescID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the conceptID
	 */
	public List<Long> getConceptID() {
		return conceptID;
	}

	/**
	 * @param conceptID the conceptID to set
	 */
	public void setConceptID(List<Long> conceptID) {
		this.conceptID = conceptID;
	}

	/**
	 * @return the cptCode
	 */
	public List<String> getCptCode() {
		return cptCode;
	}

	/**
	 * @param cptCode the cptCode to set
	 */
	public void setCptCode(List<String> cptCode) {
		this.cptCode = cptCode;
	}

	
	
	

	
	
}
