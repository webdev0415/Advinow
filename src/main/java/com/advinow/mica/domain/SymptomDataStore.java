package com.advinow.mica.domain;

import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class SymptomDataStore extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Boolean bias = true;
	
/*	@JsonInclude(Include.NON_NULL)
	private Integer likelihood =null;*/
	
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood =null;
	
	@JsonInclude(Include.NON_NULL)
	private Integer mergeCount =1;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> multiplier;
	
	@JsonInclude(Include.NON_NULL)
	private List<String> multiplierCode;
	
	/*@JsonInclude(Include.NON_NULL)
	private Set<String> soureInfo;

	@JsonInclude(Include.NON_NULL)
	private String sourceType;
 
	@JsonInclude(Include.NON_NULL)
	private Long sourceRefDate; 
	
	@JsonInclude(Include.NON_NULL)
	private Set<Integer> sources;*/
	
	@JsonInclude(Include.NON_NULL)
	private Set<String> likelyDiseases;
	
	/*
	 * Rule out. Means this symptoms disqualifies the illness.
	 */
	@JsonInclude(Include.NON_NULL)
	private Boolean ruleOut = false;
	
	/**
	 *  Checkbox for Must symptoms: Indicates that this is a symptom you must have in order to have the disease. (Will be part of MDC, but, must symptoms are not optional)
	 */
	@JsonInclude(Include.NON_NULL)
	private Boolean must = false;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean minDiagCriteria=  false;
	
	
	 @JsonInclude(Include.NON_NULL)
	 private Boolean medNecessary=false;
	

	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_MODIFIERS", direction = Relationship.OUTGOING)
	private Set<DataStoreModifier> modifierValues;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_SOURCES", direction = Relationship.OUTGOING)
	private Set<DataStoreSources> sourceInfo;

	public Boolean getBias() {
		return bias;
	}

	public void setBias(Boolean bias) {
		this.bias = bias;
	}

	
	public Double getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(Double likelihood) {
		this.likelihood = likelihood;
	}

	public Set<DataStoreModifier> getModifierValues() {
		return modifierValues;
	}

	public void setModifierValues(Set<DataStoreModifier> modifierValues) {
		this.modifierValues = modifierValues;
	}

	public List<String> getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(List<String> multiplier) {
		this.multiplier = multiplier;
	}

	public Integer getMergeCount() {
		return mergeCount;
	}

	public void setMergeCount(Integer mergeCount) {
		this.mergeCount = mergeCount;
	}

	

	/**
	 * @return the multiplierCode
	 */
	public List<String> getMultiplierCode() {
		return multiplierCode;
	}

	/**
	 * @param multiplierCode the multiplierCode to set
	 */
	public void setMultiplierCode(List<String> multiplierCode) {
		this.multiplierCode = multiplierCode;
	}

	public Set<String> getLikelyDiseases() {
		return likelyDiseases;
	}

	public void setLikelyDiseases(Set<String> likelyDiseases) {
		this.likelyDiseases = likelyDiseases;
	}



	public Boolean getMinDiagCriteria() {
		return minDiagCriteria;
	}

	public void setMinDiagCriteria(Boolean minDiagCriteria) {
		this.minDiagCriteria = minDiagCriteria;
	}

	public Boolean getMedNecessary() {
		return medNecessary;
	}

	public void setMedNecessary(Boolean medNecessary) {
		this.medNecessary = medNecessary;
	}

	public Boolean getMust() {
		return must;
	}

	public void setMust(Boolean must) {
		this.must = must;
	}

	public Boolean getRuleOut() {
		return ruleOut;
	}

	public void setRuleOut(Boolean ruleOut) {
		this.ruleOut = ruleOut;
	}

	public Set<DataStoreSources> getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(Set<DataStoreSources> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

/*	public Set<String> getSoureInfo() {
		return soureInfo;
	}

	public void setSoureInfo(Set<String> soureInfo) {
		this.soureInfo = soureInfo;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceRefDate() {
		return sourceRefDate;
	}

	public void setSourceRefDate(Long sourceRefDate) {
		this.sourceRefDate = sourceRefDate;
	}

	public Set<Integer> getSources() {
		return sources;
	}

	public void setSources(Set<Integer> sources) {
		this.sources = sources;
	}*/

/*	public Set<String> getSoureInfo() {
		return soureInfo;
	}

	public void setSoureInfo(Set<String> soureInfo) {
		this.soureInfo = soureInfo;
	}
*/
	

	

	
	
}
