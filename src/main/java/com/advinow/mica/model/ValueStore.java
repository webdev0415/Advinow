package com.advinow.mica.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ValueStore extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		
	@JsonInclude(Include.NON_NULL)
	 Boolean defaultValue;

	@JsonInclude(Include.NON_NULL)
	 String value;

	@JsonInclude(Include.NON_NULL)
	private Float m_antithesis;
		
	@JsonInclude(Include.NON_NULL)
	private String m_icd10Rcode;
	
	@JsonInclude(Include.NON_NULL)
	private Integer count;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean displayListValue; 
	
	@JsonInclude(Include.NON_NULL)	
	private String kiosk_name;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_kiosk_name;
	
	@JsonInclude(Include.NON_NULL)	
	private Integer displayOrder;
	
	

	// lab attributes starts here
	
	@JsonInclude(Include.NON_NULL)
	private Float lowerLimit;
	
	@JsonInclude(Include.NON_NULL)	
	private String lowerLimitCondition;
	
	@JsonInclude(Include.NON_NULL)
	private Float upperLimit;
	
	@JsonInclude(Include.NON_NULL)	
	private String upperLimitCondition;
	
	@JsonInclude(Include.NON_NULL)
	Boolean isNormal;
	
	// lab attributes starts here	
	
	@JsonInclude(Include.NON_NULL)
	private Boolean anyOption; 
	
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Float getM_antithesis() {
		return m_antithesis;
	}

	public void setM_antithesis(Float m_antithesis) {
		this.m_antithesis = m_antithesis;
	}

	public String getM_icd10Rcode() {
		return m_icd10Rcode;
	}

	public void setM_icd10Rcode(String m_icd10Rcode) {
		this.m_icd10Rcode = m_icd10Rcode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getDisplayListValue() {
		return displayListValue;
	}

	public void setDisplayListValue(Boolean displayListValue) {
		this.displayListValue = displayListValue;
	}

	/**
	 * @return the kiosk_name
	 */
	public String getKiosk_name() {
		return kiosk_name;
	}

	/**
	 * @param kiosk_name the kiosk_name to set
	 */
	public void setKiosk_name(String kiosk_name) {
		this.kiosk_name = kiosk_name;
	}

	/**
	 * @return the es_kiosk_name
	 */
	public String getEs_kiosk_name() {
		return es_kiosk_name;
	}

	/**
	 * @param es_kiosk_name the es_kiosk_name to set
	 */
	public void setEs_kiosk_name(String es_kiosk_name) {
		this.es_kiosk_name = es_kiosk_name;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Float getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Float lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	

	public Float getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Float upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getUpperLimitCondition() {
		return upperLimitCondition;
	}

	public void setUpperLimitCondition(String upperLimitCondition) {
		this.upperLimitCondition = upperLimitCondition;
	}

	public Boolean getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Boolean isNormal) {
		this.isNormal = isNormal;
	}

	public String getLowerLimitCondition() {
		return lowerLimitCondition;
	}

	public void setLowerLimitCondition(String lowerLimitCondition) {
		this.lowerLimitCondition = lowerLimitCondition;
	}

	public Boolean getAnyOption() {
		return anyOption;
	}

	public void setAnyOption(Boolean anyOption) {
		this.anyOption = anyOption;
	}

	

	
}
