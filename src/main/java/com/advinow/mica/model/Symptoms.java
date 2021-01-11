package com.advinow.mica.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.advinow.mica.model.enums.TimeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Symptoms extends JSonModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private String symptomID;
	
	@JsonInclude(Include.NON_NULL)
	String multipleValues;
	
	@JsonInclude(Include.NON_NULL)
	private Integer criticality;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean treatable;
	
	@JsonInclude(Include.NON_NULL)
	private Double prior;

	@JsonInclude(Include.NON_NULL)
	private String question;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_question;
	
	@JsonInclude(Include.NON_NULL)
	private Float antithesis;

	@JsonInclude(Include.NON_EMPTY)
	private List<String> subGroups;
		
    @JsonInclude(Include.NON_EMPTY)
	SymptomsTmplModel symptomsModel;

	@JsonInclude(Include.NON_EMPTY)
	private List<SymptomDataStoreModel> rows;      

	@JsonInclude(Include.NON_EMPTY)
	private List<String> bodyParts;
	
	@JsonInclude(Include.NON_NULL)
	private Integer painSwellingID; 
	
	@JsonInclude(Include.NON_NULL)
    private Long updatedDate;
		
	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	 private Long createddDate;
	
	@JsonInclude(Include.NON_NULL)	
	private Integer displayOrder=0;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> icdRCodes;
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 
	
	@JsonInclude(Include.NON_NULL)	
	private String kioskName;
	
	@JsonInclude(Include.NON_NULL)	
	private String formalName;
	
	@JsonInclude(Include.NON_EMPTY)
    //  key = data store name , value = icdR10Code
	Map<String, String> listIcdRCodes = new HashMap<String, String>();
	
	//@JsonInclude(Include.NON_EMPTY)
	private Set<SnomedCodesModel>  snomedCodes = new HashSet<SnomedCodesModel>();
	
	@JsonInclude(Include.NON_NULL)
	private Boolean medNecessary;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean minDiagCriteria;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean displayDrApp; 
	
	/*@JsonInclude(Include.NON_NULL)
	private char genderGroup;*/
	
	private String genderGroup;
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean cardinality;
	
	@JsonInclude(Include.NON_NULL)	
	private List<String> logicalGroupNames;
	
	@JsonInclude(Include.NON_NULL)	
	private List<String> deGroups;

	@JsonInclude(Include.NON_NULL)
	private String  symptomType;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private TimeType timeType;
	
	

	// lab attributes starts here
	
	@JsonInclude(Include.NON_EMPTY)
	private Float  minRange;
	 
	@JsonInclude(Include.NON_EMPTY)
	private Float  maxRange;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean isRange;
	
	// lab attribute ends here
	
	
	
	public SymptomsTmplModel getSymptomsModel() {
		return symptomsModel;
	}

	public void setSymptomsModel(SymptomsTmplModel symptomsModel) {
		this.symptomsModel = symptomsModel;
	}

	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
	}


	public String getSymptomID() {
		return symptomID;
	}

	public void setSymptomID(String symptomID) {
		this.symptomID = symptomID;
	}

	public String getMultipleValues() {
		return multipleValues;
	}

	public void setMultipleValues(String multipleValues) {
		this.multipleValues = multipleValues;
	}

	public List<String> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(List<String> subGroups) {
		this.subGroups = subGroups;
	}

	/*public List<SymptomDataStoreModel> getRows() {
		return rows;
	}

	public void setRows(List<SymptomDataStoreModel> rows) {
		this.rows = rows;
	}*/

	

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<SymptomDataStoreModel> getRows() {
		return rows;
	}

	public void setRows(List<SymptomDataStoreModel> rows) {
		this.rows = rows;
	}

	public Double getPrior() {
		return prior;
	}

	public void setPrior(Double prior) {
		this.prior = prior;
	}

	
	public List<String> getBodyParts() {
		return bodyParts;
	}

	public void setBodyParts(List<String> bodyParts) {
		this.bodyParts = bodyParts;
	}

	public Float getAntithesis() {
		return antithesis;
	}

	public void setAntithesis(Float antithesis) {
		this.antithesis = antithesis;
	}

	public Boolean getTreatable() {
		return treatable;
	}

	public void setTreatable(Boolean treatable) {
		this.treatable = treatable;
	}

	public Integer getPainSwellingID() {
		return painSwellingID;
	}

	public void setPainSwellingID(Integer painSwellingID) {
		this.painSwellingID = painSwellingID;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreateddDate() {
		return createddDate;
	}

	public void setCreateddDate(Long createddDate) {
		this.createddDate = createddDate;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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

	public Map<String, String> getListIcdRCodes() {
		return listIcdRCodes;
	}

	public void setListIcdRCodes(Map<String, String> listIcdRCodes) {
		this.listIcdRCodes = listIcdRCodes;
	}

	public List<String> getIcdRCodes() {
		return icdRCodes;
	}

	public void setIcdRCodes(List<String> icdRCodes) {
		this.icdRCodes = icdRCodes;
	}

	public Set<SnomedCodesModel> getSnomedCodes() {
		return snomedCodes;
	}

	public void setSnomedCodes(Set<SnomedCodesModel> snomedCodes) {
		this.snomedCodes = snomedCodes;
	}

	public String getEs_question() {
		return es_question;
	}

	public void setEs_question(String es_question) {
		this.es_question = es_question;
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

/*	*//**
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

	public List<String> getLogicalGroupNames() {
		return logicalGroupNames;
	}

	public void setLogicalGroupNames(List<String> logicalGroupNames) {
		this.logicalGroupNames = logicalGroupNames;
	}

	public String getSymptomType() {
		return symptomType;
	}

	public void setSymptomType(String symptomType) {
		this.symptomType = symptomType;
	}

	public List<String> getDeGroups() {
		return deGroups;
	}

	public void setDeGroups(List<String> deGroups) {
		this.deGroups = deGroups;
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

	public Boolean getIsRange() {
		return isRange;
	}

	public void setIsRange(Boolean isRange) {
		this.isRange = isRange;
	}



	

	
	
	
	}
