package com.advinow.mica.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomDataStoreModel extends  JSonModel {
@JsonInclude(Include.NON_NULL)
private Boolean bias = true;

@JsonInclude(Include.NON_EMPTY)
private List<String> multiplier;

@JsonInclude(Include.NON_EMPTY)
private List<String> multiplierCode;

@JsonInclude(Include.NON_NULL)
private Integer likelihood;

@JsonInclude(Include.NON_EMPTY)
private List<ModifierTypeModel> modifierValues;
/*
@JsonInclude(Include.NON_NULL)
private Set<String> soureInfo;

@JsonInclude(Include.NON_NULL)
private Set<Integer> sources;

@JsonInclude(Include.NON_NULL)
private String sourceType;

@JsonInclude(Include.NON_NULL)
private Long sourceRefDate; */

@JsonInclude(Include.NON_NULL)
private Set<String> likelyDiseases;

@JsonInclude(Include.NON_NULL)
private Boolean ruleOut = false;

@JsonInclude(Include.NON_NULL)
private Boolean must = false;

//@JsonInclude(Include.NON_NULL)
private Boolean minDiagCriteria=  false;

@JsonInclude(Include.NON_NULL)
private Boolean medNecessary=false;

@JsonInclude(Include.NON_NULL)
private List<DataStoreSourcesModel> sourceInfo;

public Boolean getBias() {
	return bias;
}

public void setBias(Boolean bias) {
	this.bias = bias;
}


public Integer getLikelihood() {
	return likelihood;
}

public void setLikelihood(Integer likelihood) {
	this.likelihood = likelihood;
}

public List<ModifierTypeModel> getModifierValues() {
	return modifierValues;
}

public void setModifierValues(List<ModifierTypeModel> modifierValues) {
	this.modifierValues = modifierValues;
}

public List<String> getMultiplier() {
	return multiplier;
}

public void setMultiplier(List<String> multiplier) {
	this.multiplier = multiplier;
}
/*
*//**
 * @return the soureInfo
 *//*
public Set<String> getSoureInfo() {
	return soureInfo;
}

*//**
 * @param soureInfo the soureInfo to set
 *//*
public void setSoureInfo(Set<String> soureInfo) {
	this.soureInfo = soureInfo;
}

*//**
 * @return the sourceType
 *//*
public String getSourceType() {
	return sourceType;
}

*//**
 * @param sourceType the sourceType to set
 *//*
public void setSourceType(String sourceType) {
	this.sourceType = sourceType;
}

*//**
 * @return the sourceRefDate
 *//*
public Long getSourceRefDate() {
	return sourceRefDate;
}

*//**
 * @param sourceRefDate the sourceRefDate to set
 *//*
public void setSourceRefDate(Long sourceRefDate) {
	this.sourceRefDate = sourceRefDate;
}

*//**
 * @return the sources
 *//*
public Set<Integer> getSources() {
	return sources;
}

*//**
 * @param sources the sources to set
 *//*
public void setSources(Set<Integer> sources) {
	this.sources = sources;
}*/

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

public List<DataStoreSourcesModel> getSourceInfo() {
	return sourceInfo;
}

public void setSourceInfo(List<DataStoreSourcesModel> sourceInfo) {
	this.sourceInfo = sourceInfo;
}





}
