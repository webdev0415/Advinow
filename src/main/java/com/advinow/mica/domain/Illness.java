package com.advinow.mica.domain;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */

@NodeEntity
public class Illness extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private String icd10Code;
	
	@JsonInclude(Include.NON_NULL)
	private Integer version;
		     
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private Double prior;
	
	@JsonInclude(Include.NON_NULL)
	private String state;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> groupsComplete;
	
	@JsonInclude(Include.NON_NULL)
   // @DateString("yy-MM-dd")  
	private Long modifiedDate;
	
	@JsonInclude(Include.NON_NULL)
	private Long createdDate; 
	
	@JsonInclude(Include.NON_NULL)
	private String rejectionReason;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	private Integer weight;
		
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "ILLNESS_HAS_CATEGORY", direction = Relationship.OUTGOING)
	private Set<Category> categories= new HashSet<Category>();
	
	@JsonInclude(Include.NON_NULL)
	private Integer oldUserID;
	
	@JsonInclude(Include.NON_NULL)
	private Integer mergeCount;
	
	@JsonInclude(Include.NON_EMPTY)
	private Set<Integer> mergedVersions = 	new HashSet<Integer>();
	
	@JsonInclude(Include.NON_NULL)
	private Boolean active= true;
	
	
	@JsonInclude(Include.NON_NULL)
	private Integer cluster=0;
	
	@JsonInclude(Include.NON_NULL)
	private Integer prevalence = 0;
	
	@NotNull(message="dfstatus is not null")
	@JsonInclude(Include.NON_NULL)
	private String dfstatus;
	
	
	
	
	public Integer getMergeCount() {
		return mergeCount;
	}

	public void setMergeCount(Integer mergeCount) {
		this.mergeCount = mergeCount;
	}

	public Set<Integer> getMergedVersions() {
		return mergedVersions;
	}

	public void setMergedVersions(Set<Integer> mergedVersions) {
		this.mergedVersions = mergedVersions;
	}

	public Integer getOldUserID() {
		return oldUserID;
	}

	public void setOldUserID(Integer oldUserID) {
		this.oldUserID = oldUserID;
	}

	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
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


	public String getRejectionReason() {
		return rejectionReason;
	}


	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Double getPrior() {
		return prior;
	}


	public void setPrior(Double prior) {
		this.prior = prior;
	}



	public Set<Category> getCategories() {
		return categories;
	}


	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public List<String> getGroupsComplete() {
		return groupsComplete;
	}

	public void setGroupsComplete(List<String> groupsComplete) {
		this.groupsComplete = groupsComplete;
	}

	

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	/**
	 * @return the cluster
	 */
	public Integer getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Integer cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the prevalence
	 */
	public Integer getPrevalence() {
		return prevalence;
	}

	/**
	 * @param prevalence the prevalence to set
	 */
	public void setPrevalence(Integer prevalence) {
		this.prevalence = prevalence;
	}

	public String getDfstatus() {
		return dfstatus;
	}

	public void setDfstatus(String dfstatus) {
		this.dfstatus = dfstatus;
	}

	

	


}
