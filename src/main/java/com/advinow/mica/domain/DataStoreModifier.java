package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class DataStoreModifier extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	/*@JsonInclude(Include.NON_NULL)
	private Integer start;
	
	@JsonInclude(Include.NON_NULL)
	private Integer stop;    
		
	@JsonInclude(Include.NON_NULL)
	private String slope;
	
	@JsonInclude(Include.NON_NULL)
	private Integer slopeStrength;
	
	@JsonInclude(Include.NON_NULL)
	private Integer value;*/
	// used to store the re-curs,ethnicity and Time
	@JsonInclude(Include.NON_NULL)
	private String modifierValue;
		
/*	@JsonInclude(Include.NON_NULL)
	private String timeUnit;*/
		
	@JsonInclude(Include.NON_NULL)
	private String timeFrame;

	public String getModifierValue() {
		return modifierValue;
	}

	public void setModifierValue(String modifierValue) {
		this.modifierValue = modifierValue;
	}

	/**
	 * @return the timeFrame
	 */
	public String getTimeFrame() {
		return timeFrame;
	}

	/**
	 * @param timeFrame the timeFrame to set
	 */
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	public Double getLikelihood() {
		return likelihood;
	}
	public void setLikelihood(Double likelihood) {
		this.likelihood = likelihood;
	}
	

}
