package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */

public class IllnessModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Integer userID;
  	
	@JsonInclude(Include.NON_NULL)
	private String idIcd10Code;
	
	@JsonInclude(Include.NON_NULL)
	private Integer version;
	
	@JsonInclude(Include.NON_NULL)
	private String state;
	
	@JsonInclude(Include.NON_NULL)
	private String dfstatus;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	   // @DateString("yy-MM-dd")  
	private Long updatedDate;
	
	@JsonInclude(Include.NON_NULL)
	private Long createdDate;
		
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private Double prior=0d;
	
	@JsonInclude(Include.NON_NULL)
	private Integer weight;
	
	@JsonInclude(Include.NON_NULL)
	private Integer prevalence = 0;

	@JsonInclude(Include.NON_NULL)
	private String rejectionReason;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean active;
	
	//@JsonInclude(Include.NON_EMPTY)
	private List<String> groupsComplete= new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY)
	List<SymptomGroups> symptomGroups;
	
	
	

	
	@JsonInclude(Include.NON_EMPTY)
	List<Symptoms> symptoms;
		
	public String getIdIcd10Code() {
		return idIcd10Code;
	}

	public void setIdIcd10Code(String idIcd10Code) {
		this.idIcd10Code = idIcd10Code;
	}

	public List<SymptomGroups> getSymptomGroups() {
		return symptomGroups;
	}

	public void setSymptomGroups(List<SymptomGroups> symptomGroups) {
		this.symptomGroups = symptomGroups;
	}


	public List<Symptoms> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(List<Symptoms> symptoms) {
		this.symptoms = symptoms;
	}

	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Double getPrior() {
		return prior;
	}

	public void setPrior(Double prior) {
		this.prior = prior;
	}

	public List<String> getGroupsComplete() {
		return groupsComplete;
	}

	public void setGroupsComplete(List<String> groupsComplete) {
		this.groupsComplete = groupsComplete;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the createdDate
	 */
	public Long getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getDfstatus() {
		return dfstatus;
	}

	public void setDfstatus(String dfstatus) {
		this.dfstatus = dfstatus;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getPrevalence() {
		return prevalence;
	}

	public void setPrevalence(Integer prevalence) {
		this.prevalence = prevalence;
	}

	

	}
