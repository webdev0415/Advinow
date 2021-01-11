package com.advinow.mica.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SymptomTemplateDTO extends JSonModel implements Serializable {
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomID;
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomName;

	@JsonInclude(Include.NON_NULL)
	String optionKey;
	
	@JsonInclude(Include.NON_NULL)
	String imageName;
	
	@JsonInclude(Include.NON_NULL)
	String symptomType;
	
	@JsonInclude(Include.NON_EMPTY)
	public List<String> subGroups;
	
	@JsonInclude(Include.NON_NULL)
	private String question;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_question;
	
	@JsonInclude(Include.NON_NULL)	
	private List<Object> logicalGroupNames;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	Collection<Map<String, Object>>  snomedCodes  = null;
	
	 //Map<String, Object> snomedCodes  = null;

	

	/**
	 * @return the optionKey
	 */
	public String getOptionKey() {
		return optionKey;
	}

	/**
	 * @param optionKey the optionKey to set
	 */
	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the symptomType
	 */
	public String getSymptomType() {
		return symptomType;
	}

	/**
	 * @param symptomType the symptomType to set
	 */
	public void setSymptomType(String symptomType) {
		this.symptomType = symptomType;
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
	 * @return the symptomName
	 */
	public String getSymptomName() {
		return symptomName;
	}

	/**
	 * @param symptomName the symptomName to set
	 */
	public void setSymptomName(String symptomName) {
		this.symptomName = symptomName;
	}

	/**
	 * @return the snomedCodes
	 */
	public Collection<Map<String, Object>> getSnomedCodes() {
		return snomedCodes;
	}

	/**
	 * @param snomedCodes the snomedCodes to set
	 */
	public void setSnomedCodes(Collection<Map<String, Object>> snomedCodes) {
		this.snomedCodes = snomedCodes;
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
	 * @return the logicalGroupNames
	 */
	public List<Object> getLogicalGroupNames() {
		return logicalGroupNames;
	}

	/**
	 * @param logicalGroupNames the logicalGroupNames to set
	 */
	public void setLogicalGroupNames(List<Object> logicalGroupNames) {
		this.logicalGroupNames = logicalGroupNames;
	}




}
