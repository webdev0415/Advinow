package com.advinow.mica.domain.queryresult;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.advinow.mica.domain.SymptomCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Query result wrapper for the neo4j queries.
 * 
 * @author Govinda Reddy
 *
 */
@QueryResult
public class DataframeQueryResultEntity {

	@JsonInclude(Include.NON_EMPTY)
	private String illnessName;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<Integer> rule_id;
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomID;

	@JsonInclude(Include.NON_EMPTY)
	private String listValue;

	@JsonInclude(Include.NON_EMPTY)
	private String snomedName;

	@JsonInclude(Include.NON_EMPTY)
	private Collection<Map<String, Object>> literalMap;
	
	@JsonInclude(Include.NON_EMPTY)
	private String groupName;
	
	@JsonInclude(Include.NON_EMPTY)
	private SymptomCategory sCategory;
	
	@JsonInclude(Include.NON_EMPTY)
	private Set<String> symptoms;
	

	@JsonInclude(Include.NON_EMPTY)
	private String icd10code; 
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer version; 
	
	
	@JsonInclude(Include.NON_EMPTY)
	private Float prior; 
	
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer cluster; 
	
	@JsonInclude(Include.NON_NULL)
	String descriptorID;
	
	@JsonInclude(Include.NON_NULL)
	String descriptorName;
	
	@JsonInclude(Include.NON_NULL)
	Float dcl;
	
	@JsonInclude(Include.NON_EMPTY)
	private Set<String> timePeriods;
	
	@JsonInclude(Include.NON_NULL)
	List<String> logicalGroupNames = null;
	
	@JsonInclude(Include.NON_NULL)
	String type;
	


	/**
	 * @return the symptomID
	 */
	public String getSymptomID() {
		return symptomID;
	}

	/**
	 * @param symptomID the symptomID to set
	 */
	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}


	/**
	 * @return the literalMap
	 */
	public Collection<Map<String, Object>> getLiteralMap() {
		return literalMap;
	}

	/**
	 * @param literalMap the literalMap to set
	 */
	public void setLiteralMap(Collection<Map<String, Object>> literalMap) {
		this.literalMap = literalMap;
	}

	/**
	 * @return the snomedName
	 */
	public String getSnomedName() {
		return snomedName;
	}

	/**
	 * @param snomedName the snomedName to set
	 */
	public void setSnomedName(String snomedName) {
		this.snomedName = snomedName;
	}

	/**
	 * @return the listValue
	 */
	public String getListValue() {
		return listValue;
	}

	/**
	 * @param listValue the listValue to set
	 */
	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the sCategory
	 */
	public SymptomCategory getsCategory() {
		return sCategory;
	}

	/**
	 * @param sCategory the sCategory to set
	 */
	public void setsCategory(SymptomCategory sCategory) {
		this.sCategory = sCategory;
	}

	/**
	 * @return the symptoms
	 */
	public Set<String> getSymptoms() {
		return symptoms;
	}

	/**
	 * @param symptoms the symptoms to set
	 */
	public void setSymptoms(Set<String> symptoms) {
		this.symptoms = symptoms;
	}

	/**
	 * @return the icd10code
	 */
	public String getIcd10code() {
		return icd10code;
	}

	/**
	 * @param icd10code the icd10code to set
	 */
	public void setIcd10code(String icd10code) {
		this.icd10code = icd10code;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the prior
	 */
	public Float getPrior() {
		return prior;
	}

	/**
	 * @param prior the prior to set
	 */
	public void setPrior(Float prior) {
		this.prior = prior;
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
	 * @return the descriptorID
	 */
	public String getDescriptorID() {
		return descriptorID;
	}

	/**
	 * @param descriptorID the descriptorID to set
	 */
	public void setDescriptorID(String descriptorID) {
		this.descriptorID = descriptorID;
	}

	/**
	 * @return the descriptorName
	 */
	public String getDescriptorName() {
		return descriptorName;
	}

	/**
	 * @param descriptorName the descriptorName to set
	 */
	public void setDescriptorName(String descriptorName) {
		this.descriptorName = descriptorName;
	}

	/**
	 * @return the dcl
	 */
	public Float getDcl() {
		return dcl;
	}

	/**
	 * @param dcl the dcl to set
	 */
	public void setDcl(Float dcl) {
		this.dcl = dcl;
	}

	/**
	 * @return the illnessName
	 */
	public String getIllnessName() {
		return illnessName;
	}

	/**
	 * @param illnessName the illnessName to set
	 */
	public void setIllnessName(String illnessName) {
		this.illnessName = illnessName;
	}

	/**
	 * @return the rule_id
	 */
	public List<Integer> getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id the rule_id to set
	 */
	public void setRule_id(List<Integer> rule_id) {
		this.rule_id = rule_id;
	}

	public Set<String> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Set<String> timePeriods) {
		this.timePeriods = timePeriods;
	}

	public List<String> getLogicalGroupNames() {
		return logicalGroupNames;
	}

	public void setLogicalGroupNames(List<String> logicalGroupNames) {
		this.logicalGroupNames = logicalGroupNames;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
	
}
