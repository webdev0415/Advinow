package com.advinow.mica.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ICD10CodeModel  extends JSonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
    
	@JsonInclude(Include.NON_NULL)
	private Double prior;
		
	@JsonInclude(Include.NON_NULL)
	private String status;
	
	@JsonInclude(Include.NON_NULL)
	private String state;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	   // @DateString("yy-MM-dd")  
	private Long updatedDate;
	
	@JsonInclude(Include.NON_NULL)
	private Long createddDate;
	
	@JsonInclude(Include.NON_NULL)
	private Integer weight;

	@JsonInclude(Include.NON_NULL)
	private String rejectionReason;
	

	@JsonInclude(Include.NON_NULL)
	private Integer version;


	@JsonInclude(Include.NON_NULL)
	private Integer userID;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean active;


	
	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}

	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPrior() {
		return prior;
	}

	public void setPrior(Double prior) {
		this.prior = prior;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	/**
	 * @return the createddDate
	 */
	public Long getCreateddDate() {
		return createddDate;
	}

	/**
	 * @param createddDate the createddDate to set
	 */
	public void setCreateddDate(Long createddDate) {
		this.createddDate = createddDate;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

}
