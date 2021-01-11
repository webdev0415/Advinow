package com.advinow.mica.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.advinow.mica.model.enums.TimeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * Represents all the symptom properties and will be used for the services and not being used in the MICA services.
 * 
 * @author Govinda Reddy
 *
 */
public class SymptomTemplateModel extends JSonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;

	@JsonInclude(Include.NON_EMPTY)	
	private Map<String, Float> antithesis= new HashMap <String, Float>();
	
	@JsonInclude(Include.NON_EMPTY)	
	private Map<String, String> icd10RCodes=  new HashMap <String, String>();
	

	@JsonInclude(Include.NON_EMPTY)	
	private Map<String, String> snomedCodes=  new HashMap <String, String>();
		
	@JsonInclude(Include.NON_EMPTY)	
	Map<String, Boolean> displayListValues=  new HashMap <String, Boolean>();
	
	
	@JsonInclude(Include.NON_NULL)
	private Boolean treatable;
	
	@JsonInclude(Include.NON_NULL)
	private String question;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_question;
	
	// This is not part of the symptom and to give the reponse status
	@JsonInclude(Include.NON_NULL)
	private String status;
	
	@JsonInclude(Include.NON_NULL)
	String multipleValues;
		
	@JsonInclude(Include.NON_NULL)
	private Float prior;

	@JsonInclude(Include.NON_EMPTY)
	private List<String> subGroups;
		
    @JsonInclude(Include.NON_EMPTY)
	SymptomsTmplModel symptomsModel;
 	
	@JsonInclude(Include.NON_NULL)
	private Integer painSwellingID; 
	
	@JsonInclude(Include.NON_NULL)
    private Date updatedDate;
		
	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	 private Date createddDate;
	
	@JsonInclude(Include.NON_NULL)	
	private Integer displayOrder;
	
	/*@JsonInclude(Include.NON_NULL)
 	private String name;*/
		
	@JsonInclude(Include.NON_NULL)
	private Boolean bias;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<Double> rangeValues;
	
	@JsonInclude(Include.NON_NULL)
	private String descriptors;
	
	@JsonInclude(Include.NON_NULL)
	private String descriptorFile;

	@JsonInclude(Include.NON_NULL)
	private Integer scaleTimeLimit;
	

	@JsonInclude(Include.NON_NULL)
	private Integer scaleTimeLimitStart;
	
	@JsonInclude(Include.NON_NULL)
	private Integer timeUnitDefault;

	@JsonInclude(Include.NON_NULL)
	private String scaleInfoText;
	
	@JsonIgnore
	@JsonInclude(Include.NON_EMPTY)	
	private List<String> dataStoreTypes;// = new ArrayList<String>();
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 
	
	@JsonInclude(Include.NON_NULL)	
	private String kioskName;
	
	@JsonInclude(Include.NON_NULL)	
	private String formalName;
	
	
	@JsonInclude(Include.NON_NULL)
	private String definition;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean medNecessary;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean displayDrApp; 
	
	@JsonInclude(Include.NON_NULL)
	private Boolean active;
	
/*	@JsonInclude(Include.NON_NULL)
	private char genderGroup;*/
	
	private String genderGroup;
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean cardinality;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private TimeType timeType;


	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}

	
	public Boolean getTreatable() {
		return treatable;
	}

	public void setTreatable(Boolean treatable) {
		this.treatable = treatable;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMultipleValues() {
		return multipleValues;
	}

	public void setMultipleValues(String multipleValues) {
		this.multipleValues = multipleValues;
	}

	public Float getPrior() {
		return prior;
	}

	public void setPrior(Float prior) {
		this.prior = prior;
	}

	public List<String> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(List<String> subGroups) {
		this.subGroups = subGroups;
	}

	public SymptomsTmplModel getSymptomsModel() {
		return symptomsModel;
	}

	public void setSymptomsModel(SymptomsTmplModel symptomsModel) {
		this.symptomsModel = symptomsModel;
	}

	public Integer getPainSwellingID() {
		return painSwellingID;
	}

	public void setPainSwellingID(Integer painSwellingID) {
		this.painSwellingID = painSwellingID;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreateddDate() {
		return createddDate;
	}

	public void setCreateddDate(Date createddDate) {
		this.createddDate = createddDate;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	

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

	public List<Double> getRangeValues() {
		return rangeValues;
	}

	public void setRangeValues(List<Double> rangeValues) {
		this.rangeValues = rangeValues;
	}

	public String getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(String descriptors) {
		this.descriptors = descriptors;
	}

	public String getDescriptorFile() {
		return descriptorFile;
	}

	public void setDescriptorFile(String descriptorFile) {
		this.descriptorFile = descriptorFile;
	}

	public Integer getScaleTimeLimit() {
		return scaleTimeLimit;
	}

	public void setScaleTimeLimit(Integer scaleTimeLimit) {
		this.scaleTimeLimit = scaleTimeLimit;
	}

	public Integer getScaleTimeLimitStart() {
		return scaleTimeLimitStart;
	}

	public void setScaleTimeLimitStart(Integer scaleTimeLimitStart) {
		this.scaleTimeLimitStart = scaleTimeLimitStart;
	}

	public Integer getTimeUnitDefault() {
		return timeUnitDefault;
	}

	public void setTimeUnitDefault(Integer timeUnitDefault) {
		this.timeUnitDefault = timeUnitDefault;
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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	

	public Map<String, Float> getAntithesis() {
		return antithesis;
	}

	public void setAntithesis(Map<String, Float> antithesis) {
		this.antithesis = antithesis;
	}

	public Map<String, String> getIcd10RCodes() {
		return icd10RCodes;
	}

	public void setIcd10RCodes(Map<String, String> icd10rCodes) {
		icd10RCodes = icd10rCodes;
	}

	public Boolean getDisplaySymptom() {
		return displaySymptom;
	}

	public void setDisplaySymptom(Boolean displaySymptom) {
		this.displaySymptom = displaySymptom;
	}

	public String getKioskName() {
		return kioskName;
	}

	public void setKioskName(String kioskName) {
		this.kioskName = kioskName;
	}

	public String getFormalName() {
		return formalName;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	

	public Map<String, Boolean> getDisplayListValues() {
		return displayListValues;
	}

	public void setDisplayListValues(Map<String, Boolean> displayListValues) {
		this.displayListValues = displayListValues;
	}

	public String getEs_question() {
		return es_question;
	}

	public void setEs_question(String es_question) {
		this.es_question = es_question;
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
	 * @return the snomedCodes
	 */
	public Map<String, String> getSnomedCodes() {
		return snomedCodes;
	}

	/**
	 * @param snomedCodes the snomedCodes to set
	 */
	public void setSnomedCodes(Map<String, String> snomedCodes) {
		this.snomedCodes = snomedCodes;
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
	 * @return the genderGroup
	 *//*
	public char getGenderGroup() {
		return genderGroup;
	}

	*//**
	 * @param genderGroup the genderGroup to set
	 *//*
	public void setGenderGroup(char genderGroup) {
		this.genderGroup = genderGroup;
	}*/

	/**
	 * @return the cardinality
	 */
	public Boolean getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality the cardinality to set
	 */
	public void setCardinality(Boolean cardinality) {
		this.cardinality = cardinality;
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

	
}
