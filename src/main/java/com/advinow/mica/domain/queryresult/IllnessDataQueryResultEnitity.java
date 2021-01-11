package com.advinow.mica.domain.queryresult;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@QueryResult
public class IllnessDataQueryResultEnitity {
	

	@JsonInclude(Include.NON_NULL)
	public String icd10Code;

	@JsonInclude(Include.NON_NULL)
	public String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	public Double dsLikelihood;
	
	@JsonInclude(Include.NON_NULL)
	public Double dmLikelihood;
	
	@JsonInclude(Include.NON_NULL)
	public List<String> multiplier;
	
	@JsonInclude(Include.NON_NULL)
	public Integer pattern;
	
	@JsonInclude(Include.NON_NULL)
	public String illnessName;	
	
	@JsonInclude(Include.NON_NULL)
	public List<String> bodyParts;	
	
	@JsonInclude(Include.NON_NULL)
	public List<Boolean> displaySymptoms;
	
	@JsonInclude(Include.NON_NULL)
	public Integer symptomCount;	
	
	@JsonInclude(Include.NON_NULL)
	public List<String> symptoms;
	
	@JsonInclude(Include.NON_NULL)
	public Integer version;	

	
	@JsonInclude(Include.NON_NULL)
	public String timeFrame;
	
	
	public String getIcd10Code() {
		return icd10Code;
	}

	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}

	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	

	public List<String> getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(List<String> multiplier) {
		this.multiplier = multiplier;
	}

	

	public Double getDsLikelihood() {
		return dsLikelihood;
	}

	public void setDsLikelihood(Double dsLikelihood) {
		this.dsLikelihood = dsLikelihood;
	}

	public Double getDmLikelihood() {
		return dmLikelihood;
	}

	public void setDmLikelihood(Double dmLikelihood) {
		this.dmLikelihood = dmLikelihood;
	}
	
	public Integer getPattern() {
		return pattern;
	}

	public void setPattern(Integer pattern) {
		this.pattern = pattern;
	}


	/**
	 * @return the bodyParts
	 */
	public List<String> getBodyParts() {
		return bodyParts;
	}

	/**
	 * @param bodyParts the bodyParts to set
	 */
	public void setBodyParts(List<String> bodyParts) {
		this.bodyParts = bodyParts;
	}

	/**
	 * @return the displaySymptoms
	 */
	public List<Boolean> getDisplaySymptoms() {
		return displaySymptoms;
	}

	/**
	 * @param displaySymptoms the displaySymptoms to set
	 */
	public void setDisplaySymptoms(List<Boolean> displaySymptoms) {
		this.displaySymptoms = displaySymptoms;
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
	 * @return the symptomCount
	 */
	public Integer getSymptomCount() {
		return symptomCount;
	}

	/**
	 * @param symptomCount the symptomCount to set
	 */
	public void setSymptomCount(Integer symptomCount) {
		this.symptomCount = symptomCount;
	}

	/**
	 * @return the symptoms
	 */
	public List<String> getSymptoms() {
		return symptoms;
	}

	/**
	 * @param symptoms the symptoms to set
	 */
	public void setSymptoms(List<String> symptoms) {
		this.symptoms = symptoms;
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

	public String getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}

	



}
