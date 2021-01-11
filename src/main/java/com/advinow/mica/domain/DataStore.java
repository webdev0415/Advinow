package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class DataStore extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	 String value;
		
	@JsonInclude(Include.NON_NULL)
	 Boolean defaultValue;
	
	@JsonInclude(Include.NON_NULL)
	private Float m_antithesis;
	
	/*
	 *
	 *No of times this listvalue is being used. this will be updated from the stored proc
	 *call updateSymptomCount(); 
	 */
	
	@JsonInclude(Include.NON_NULL)
	private Integer count=0;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean displayListValue; 
	
	@JsonInclude(Include.NON_NULL)
	private Boolean anyOption; 
	
	
	@JsonInclude(Include.NON_NULL)	
	private String kioskName;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_kioskName;
	
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
	Boolean normal;
	
	// lab attributes starts here	
	

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
	 * @return the kioskName
	 */
	public String getKioskName() {
		return kioskName;
	}

	/**
	 * @param kioskName the kioskName to set
	 */
	public void setKioskName(String kioskName) {
		this.kioskName = kioskName;
	}

	/**
	 * @return the es_kioskName
	 */
	public String getEs_kioskName() {
		return es_kioskName;
	}

	/**
	 * @param es_kioskName the es_kioskName to set
	 */
	public void setEs_kioskName(String es_kioskName) {
		this.es_kioskName = es_kioskName;
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

	public Boolean getNormal() {
		return normal;
	}

	public void setNormal(Boolean normal) {
		this.normal = normal;
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
