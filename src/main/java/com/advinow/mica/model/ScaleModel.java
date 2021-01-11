/**
 * 
 */
package com.advinow.mica.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author govind
 *
 */

public class ScaleModel extends JSonModel {
/*	
	@JsonInclude(Include.NON_NULL)
	private Integer upperTimeLimit;	
	@JsonInclude(Include.NON_NULL)
	private Integer scaleTimeLimitStart;     
	@JsonInclude(Include.NON_NULL)	
	private Integer value;
	@JsonInclude(Include.NON_NULL)
	private String slope;
	@JsonInclude(Include.NON_NULL)
	private Integer slopeStrength;
	@JsonInclude(Include.NON_NULL)
	private String timeUnit;*/
	
	@JsonInclude(Include.NON_NULL)
	private String timeFrame;
/*


	public Integer getUpperTimeLimit() {
		return upperTimeLimit;
	}
	public void setUpperTimeLimit(Integer upperTimeLimit) {
		this.upperTimeLimit = upperTimeLimit;
	}
	public Integer getScaleTimeLimitStart() {
		return scaleTimeLimitStart;
	}
	public void setScaleTimeLimitStart(Integer scaleTimeLimitStart) {
		this.scaleTimeLimitStart = scaleTimeLimitStart;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getSlope() {
		return slope;
	}
	public void setSlope(String slope) {
		this.slope = slope;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public Integer getSlopeStrength() {
		return slopeStrength;
	}
	public void setSlopeStrength(Integer slopeStrength) {
		this.slopeStrength = slopeStrength;
	}*/
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

}
