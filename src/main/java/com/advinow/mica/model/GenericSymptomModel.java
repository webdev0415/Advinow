/**
 * 
 */
package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.advinow.mica.model.enums.TimeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author govind
 *
 */
public class GenericSymptomModel  extends JSonModel {

	@JsonInclude(Include.NON_NULL)
	private String bodyPart;
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private Float antithesis;
		
	@JsonInclude(Include.NON_NULL)
	private Boolean treatable;

	@JsonInclude(Include.NON_NULL)
	String multipleValues;  // used for multipliers

	@JsonInclude(Include.NON_EMPTY)
	public List<String> subGroups;

	/*@JsonInclude(Include.NON_EMPTY)
	private String questionText;*/
	
	@JsonInclude(Include.NON_NULL)	
	private String es_questionText;
	
	@JsonInclude(Include.NON_NULL)
	private Integer painSwellingID; 

	@JsonInclude(Include.NON_NULL)	
	private Boolean bias;

	@JsonInclude(Include.NON_EMPTY) 
	private List<String> dataStoreTemplates= new ArrayList<String>();
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 
	
	
	@JsonInclude(Include.NON_NULL)
	private String definition;

	@JsonInclude(Include.NON_NULL)
	private Boolean displayDrApp = true;

	@JsonInclude(Include.NON_NULL)
	private Boolean active= true;
	
	@JsonInclude(Include.NON_NULL)	
	private Integer displayOrder;
	
	@JsonInclude(Include.NON_NULL)
	private String question;
	

	@JsonInclude(Include.NON_EMPTY)
	private TimeType timeType;
		
	
	/**
	 * @return the bodyPart
	 */
	public String getBodyPart() {
		return bodyPart;
	}
	/**
	 * @param bodyPart the bodyPart to set
	 */
	public void setBodyPart(String bodyPart) {
		this.bodyPart = bodyPart;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the multipleValues
	 */
	public String getMultipleValues() {
		return multipleValues;
	}
	/**
	 * @param multipleValues the multipleValues to set
	 */
	public void setMultipleValues(String multipleValues) {
		this.multipleValues = multipleValues;
	}
	/**
	 * @return the subGroups
	 */
	public List<String> getSubGroups() {
		return subGroups;
	}
	/**
	 * @param subGroups the subGroups to set
	 */
	public void setSubGroups(List<String> subGroups) {
		this.subGroups = subGroups;
	}
	
	/**
	 * @return the questionText
	 *//*
	public String getQuestionText() {
		return questionText;
	}
	*//**
	 * @param questionText the questionText to set
	 *//*
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}*/
	/**
	 * @return the es_questionText
	 */
	public String getEs_questionText() {
		return es_questionText;
	}
	/**
	 * @param es_questionText the es_questionText to set
	 */
	public void setEs_questionText(String es_questionText) {
		this.es_questionText = es_questionText;
	}
	/**
	 * @return the painSwellingID
	 */
	public Integer getPainSwellingID() {
		return painSwellingID;
	}
	/**
	 * @param painSwellingID the painSwellingID to set
	 */
	public void setPainSwellingID(Integer painSwellingID) {
		this.painSwellingID = painSwellingID;
	}
	/**
	 * @return the bias
	 */
	public Boolean getBias() {
		return bias;
	}
	/**
	 * @param bias the bias to set
	 */
	public void setBias(Boolean bias) {
		this.bias = bias;
	}
	/**
	 * @return the dataStoreTemplates
	 */
	public List<String> getDataStoreTemplates() {
		return dataStoreTemplates;
	}
	/**
	 * @param dataStoreTemplates the dataStoreTemplates to set
	 */
	public void setDataStoreTemplates(List<String> dataStoreTemplates) {
		this.dataStoreTemplates = dataStoreTemplates;
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
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}
	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
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
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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
	public TimeType getTimeType() {
		return timeType;
	}
	public void setTimeType(TimeType timeType) {
		this.timeType = timeType;
	}

	
}
