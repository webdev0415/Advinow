package com.advinow.mica.model;

import java.util.List;

import com.advinow.mica.model.enums.TimeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Represents all the properties symptom for multipliers
 * 
 * @author Developer
 *
 */
public class SymptomsTmplModel extends JSonModel {
/*
	@JsonInclude(Include.NON_NULL)
 	private String name;*/
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean bias;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<Double> rangeValues;
	
	@JsonInclude(Include.NON_NULL)
	private String descriptors;
	
	@JsonInclude(Include.NON_NULL)
	private String descriptorFile;

	@JsonInclude(Include.NON_NULL)
	private String questionText;

	@JsonInclude(Include.NON_NULL)
	private Integer scaleTimeLimit;
	

	@JsonInclude(Include.NON_NULL)
	private Integer scaleTimeLimitStart;
	
	@JsonInclude(Include.NON_NULL)
	private Integer timeUnitDefault;

	@JsonInclude(Include.NON_NULL)
	private String scaleInfoText;

	@JsonInclude(Include.NON_EMPTY)	
	private List<String> dataStoreTypes;// = new ArrayList<String>();
	

	
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean treatable;
	
	@JsonInclude(Include.NON_NULL)
	private Float prior;

	@JsonInclude(Include.NON_NULL)
	private String question;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_question;
	
	@JsonInclude(Include.NON_NULL)
	private Float antithesis;


	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 

	@JsonInclude(Include.NON_NULL)
	private Boolean medNecessary;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean minDiagCriteria;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean displayDrApp; 
	
	@JsonInclude(Include.NON_NULL)
	private String genderGroup;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private TimeType timeType;
	

	@JsonInclude(Include.NON_EMPTY)
	private Float  minRange;
	 
	@JsonInclude(Include.NON_EMPTY)
	private Float  maxRange;


	
	/*public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}*/

	public Boolean getBias() {
		return bias;
	}

	public void setBias(Boolean bias) {
		this.bias = bias;
	}

	public String getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(String descriptors) {
		this.descriptors = descriptors;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Integer getScaleTimeLimit() {
		return scaleTimeLimit;
	}

	public void setScaleTimeLimit(Integer scaleTimeLimit) {
		this.scaleTimeLimit = scaleTimeLimit;
	}

	public String getScaleInfoText() {
		return scaleInfoText;
	}

	public void setScaleInfoText(String scaleInfoText) {
		this.scaleInfoText = scaleInfoText;
	}

	public List<String> getDataStoreTypes() {
		return dataStoreTypes;
	}

	public void setDataStoreTypes(List<String> dataStoreTypes) {
		this.dataStoreTypes = dataStoreTypes;
	}

	public Integer getTimeUnitDefault() {
		return timeUnitDefault;
	}

	public void setTimeUnitDefault(Integer timeUnitDefault) {
		this.timeUnitDefault = timeUnitDefault;
	}

	public Integer getScaleTimeLimitStart() {
		return scaleTimeLimitStart;
	}

	public void setScaleTimeLimitStart(Integer scaleTimeLimitStart) {
		this.scaleTimeLimitStart = scaleTimeLimitStart;
	}

	public String getDescriptorFile() {
		return descriptorFile;
	}

	public void setDescriptorFile(String descriptorFile) {
		this.descriptorFile = descriptorFile;
	}

	public List<Double> getRangeValues() {
		return rangeValues;
	}

	public void setRangeValues(List<Double> rangeValues) {
		this.rangeValues = rangeValues;
	}

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
	 * @return the criticality
	 */
	public Integer getCriticality() {
		return criticality;
	}

	/**
	 * @param criticality the criticality to set
	 */
	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}

	/**
	 * @return the treatable
	 */
	public Boolean getTreatable() {
		return treatable;
	}

	/**
	 * @param treatable the treatable to set
	 */
	public void setTreatable(Boolean treatable) {
		this.treatable = treatable;
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
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the es_question
	 */
	public String getEs_question() {
		return es_question;
	}

	/**
	 * @param es_question the es_question to set
	 */
	public void setEs_question(String es_question) {
		this.es_question = es_question;
	}

	/**
	 * @return the antithesis
	 */
	public Float getAntithesis() {
		return antithesis;
	}

	/**
	 * @param antithesis the antithesis to set
	 */
	public void setAntithesis(Float antithesis) {
		this.antithesis = antithesis;
	}

	/**
	 * @return the displaySymptom
	 */
	public Boolean getDisplaySymptom() {
		return displaySymptom;
	}

	/**
	 * @param displaySymptom the displaySymptom to set
	 */
	public void setDisplaySymptom(Boolean displaySymptom) {
		this.displaySymptom = displaySymptom;
	}

	/**
	 * @return the medNecessary
	 */
	public Boolean getMedNecessary() {
		return medNecessary;
	}

	/**
	 * @param medNecessary the medNecessary to set
	 */
	public void setMedNecessary(Boolean medNecessary) {
		this.medNecessary = medNecessary;
	}

	/**
	 * @return the minDiagCriteria
	 */
	public Boolean getMinDiagCriteria() {
		return minDiagCriteria;
	}

	/**
	 * @param minDiagCriteria the minDiagCriteria to set
	 */
	public void setMinDiagCriteria(Boolean minDiagCriteria) {
		this.minDiagCriteria = minDiagCriteria;
	}

	/**
	 * @return the displayDrApp
	 */
	public Boolean getDisplayDrApp() {
		return displayDrApp;
	}

	/**
	 * @param displayDrApp the displayDrApp to set
	 */
	public void setDisplayDrApp(Boolean displayDrApp) {
		this.displayDrApp = displayDrApp;
	}

	/**
	 * @return the genderGroup
	 */
	public String getGenderGroup() {
		return genderGroup;
	}

	/**
	 * @param genderGroup the genderGroup to set
	 */
	public void setGenderGroup(String genderGroup) {
		this.genderGroup = genderGroup;
	}

	public TimeType getTimeType() {
		return timeType;
	}

	public void setTimeType(TimeType timeType) {
		this.timeType = timeType;
	}

	public Float getMinRange() {
		return minRange;
	}

	public void setMinRange(Float minRange) {
		this.minRange = minRange;
	}

	public Float getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Float maxRange) {
		this.maxRange = maxRange;
	}

	
	
	
}
