package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AdtionalSettings {
	
	private String optionCode = null;

	private String optionDescription = null;
	
	@JsonInclude(Include.NON_NULL)
	private String es_optionDescription;
	
	@JsonInclude(Include.NON_NULL)	
	private String kioskName;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_kioskName;
	
	@JsonInclude(Include.NON_NULL)
	private Float antithesis;
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 
	
	

	// lab attributes starts here
	
	//@JsonInclude(Include.NON_NULL)
	private Float lowerLimit;
	
	//@JsonInclude(Include.NON_NULL)	
	private String lowerLimitCondition;
	
	//@JsonInclude(Include.NON_NULL)
	private Float upperLimit;
	
	//@JsonInclude(Include.NON_NULL)	
	private String upperLimitCondition;
	
	//@JsonInclude(Include.NON_NULL)
	Boolean isNormal;
	
	// lab attributes ends here	
	
	
	@JsonInclude(Include.NON_NULL)
	private Boolean anyOption; 
	
	
	
	List<SnomedCodeModel> snomedCodes = new ArrayList<SnomedCodeModel>();
	 
	List<String> icd10RCodes = new ArrayList<String>();
/*	
	List<String> labOrders = new ArrayList<String>();*/

	/**
	 * @return the optionCode
	 */
	public String getOptionCode() {
		return optionCode;
	}

	/**
	 * @param optionCode the optionCode to set
	 */
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	/**
	 * @return the optionDescription
	 */
	public String getOptionDescription() {
		return optionDescription;
	}

	/**
	 * @param optionDescription the optionDescription to set
	 */
	public void setOptionDescription(String optionDescription) {
		this.optionDescription = optionDescription;
	}

	/**
	 * @return the es_optionDescription
	 */
	public String getEs_optionDescription() {
		return es_optionDescription;
	}

	/**
	 * @param es_optionDescription the es_optionDescription to set
	 */
	public void setEs_optionDescription(String es_optionDescription) {
		this.es_optionDescription = es_optionDescription;
	}

	/**
	 * @return the kioskName
	 */
	public String getKioskName() {
		return kioskName;
	}

	/**
	 * @param kioskName the kioskName to set
	 */
	public void setKioskName(String kioskName) {
		this.kioskName = kioskName;
	}

	/**
	 * @return the es_kioskName
	 */
	public String getEs_kioskName() {
		return es_kioskName;
	}

	/**
	 * @param es_kioskName the es_kioskName to set
	 */
	public void setEs_kioskName(String es_kioskName) {
		this.es_kioskName = es_kioskName;
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
	 * @return the snomedCodes
	 */
	public List<SnomedCodeModel> getSnomedCodes() {
		return snomedCodes;
	}

	/**
	 * @param snomedCodes the snomedCodes to set
	 */
	public void setSnomedCodes(List<SnomedCodeModel> snomedCodes) {
		this.snomedCodes = snomedCodes;
	}

	/**
	 * @return the icd10RCodes
	 */
	public List<String> getIcd10RCodes() {
		return icd10RCodes;
	}

	/**
	 * @param icd10rCodes the icd10RCodes to set
	 */
	public void setIcd10RCodes(List<String> icd10rCodes) {
		icd10RCodes = icd10rCodes;
	}

	public Float getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Float lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	

	public Float getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Float upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getUpperLimitCondition() {
		return upperLimitCondition;
	}

	public void setUpperLimitCondition(String upperLimitCondition) {
		this.upperLimitCondition = upperLimitCondition;
	}

	public Boolean getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Boolean isNormal) {
		this.isNormal = isNormal;
	}

	public String getLowerLimitCondition() {
		return lowerLimitCondition;
	}

	public void setLowerLimitCondition(String lowerLimitCondition) {
		this.lowerLimitCondition = lowerLimitCondition;
	}

	public Boolean getAnyOption() {
		return anyOption;
	}

	public void setAnyOption(Boolean anyOption) {
		this.anyOption = anyOption;
	}

/*	public List<String> getLabOrders() {
		return labOrders;
	}

	public void setLabOrders(List<String> labOrders) {
		this.labOrders = labOrders;
	}*/

}
