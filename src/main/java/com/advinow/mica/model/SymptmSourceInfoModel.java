package com.advinow.mica.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptmSourceInfoModel {
		
	@JsonInclude(Include.NON_NULL)
	private String sourceType;
 
	@JsonInclude(Include.NON_NULL)
	private Long sourceRefDate;
	
	@JsonInclude(Include.NON_NULL)
	String source;
	
	@JsonInclude(Include.NON_NULL)
	Integer sourceId;
	
	
	@JsonInclude(Include.NON_NULL)
	String multiplier;
  
	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the sourceRefDate
	 */
	public Long getSourceRefDate() {
		return sourceRefDate;
	}

	/**
	 * @param sourceRefDate the sourceRefDate to set
	 */
	public void setSourceRefDate(Long sourceRefDate) {
		this.sourceRefDate = sourceRefDate;
	}

	

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the multiplier
	 */
	public String getMultiplier() {
		return multiplier;
	}

	/**
	 * @param multiplier the multiplier to set
	 */
	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * @return the sourceId
	 */
	public Integer getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

}
