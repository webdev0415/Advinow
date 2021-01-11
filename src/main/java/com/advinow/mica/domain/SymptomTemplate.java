package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class SymptomTemplate extends Neo4jEntity {

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

	@JsonInclude(Include.NON_EMPTY)
	private String questionText;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_questionText;

	@JsonInclude(Include.NON_NULL)	
	private Boolean bias;

	@JsonInclude(Include.NON_EMPTY)
	private List<String> rangeValues; // measurements

	@JsonInclude(Include.NON_NULL)
	private String descriptors;

	@JsonInclude(Include.NON_NULL)
	private String descriptorFile; // for immage

	@JsonInclude(Include.NON_NULL)
	private Integer timeRangeStart;
	
	@JsonInclude(Include.NON_NULL)
	private Integer timeRangeStop;
	
	@JsonInclude(Include.NON_NULL)
	private String scaleTimeLimitText;	
	
	@JsonInclude(Include.NON_NULL)
	private Integer timeUnitDefault;
	
	// This ID will be used to compare the symptoms and to copy in KIOSK
	@JsonInclude(Include.NON_NULL)
	private Integer painSwellingID; 

	@Transient
	@JsonInclude(Include.NON_EMPTY) 
	private Hashtable<String, Set<DataStore>> dataList= new Hashtable<String, Set<DataStore>>();


	@JsonInclude(Include.NON_EMPTY) 
	@JsonIgnore
	private List<String> dataStoreTemplates= new ArrayList<String>();
	
	@JsonInclude(Include.NON_NULL)
    private Long updatedDate;
		

	@JsonInclude(Include.NON_NULL)
	private Long createddDate;
	
	@JsonInclude(Include.NON_NULL)
	private String  symptomType;
	
	/*
	 *
	 *No of times this symptom is being used. this will be updated from the stored proc
	 *call updateSymptomCount(); 
	 */
	@JsonInclude(Include.NON_NULL)	
	private Integer displayOrder;
	
/*
	@JsonInclude(Include.NON_NULL)	
	private String ICDRCode;*/
	
	@JsonInclude(Include.NON_NULL)	
	private Boolean displaySymptom; 
	/*
	 * if false should only be able to select one
	 * true should allow to select more than one
	 * null means only symptom
	 * 
	 */
	@JsonInclude(Include.NON_NULL)	
	private Boolean cardinality;
	
	@JsonInclude(Include.NON_NULL)	
	private String kioskName;
	
	@JsonInclude(Include.NON_NULL)	
	private String es_kioskName;
	
	
	@JsonInclude(Include.NON_NULL)	
	private String formalName;
	
	@JsonInclude(Include.NON_NULL)
	private Double ecw_prior;
	
	@JsonInclude(Include.NON_NULL)
	private String definition;

	@JsonInclude(Include.NON_NULL)
	private Boolean displayDrApp = true;

	@JsonInclude(Include.NON_NULL)
	private Boolean active= true;
		
	@JsonInclude(Include.NON_EMPTY)
	private String timeType;
	
	// lab attributes starts here
	
	@JsonInclude(Include.NON_EMPTY)
	private Float  minRange;
	 
	@JsonInclude(Include.NON_EMPTY)
	private Float  maxRange;
	
	@JsonInclude(Include.NON_NULL)
	private Boolean range;
	
	// lab attribute ends here
	
	
		
/*	@JsonInclude(Include.NON_NULL)
	private char genderGroup;*/
	
	//@JsonInclude(Include.NON_NULL)
	private String genderGroup;
	
/*	@JsonInclude(Include.NON_NULL)
	private String type;*/
	
/*	// GroupID for the symptom, this is refered ad classification ID in groups.
	@JsonInclude(Include.NON_NULL)
	private List<Integer> logicalGroupID;
	*/
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "SMPT_HAS_SNOMED", direction =Relationship.OUTGOING)
	//public Set<SnomedCodes>  snomedCodes = new HashSet<SnomedCodes>();
	public Set<SnomedCodes>  snomedCodes ;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_IR_CODE", direction =Relationship.OUTGOING)
	public Set<RCodeDataStore>  rCodeDataStores = new HashSet<RCodeDataStore>();
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_LOGICAL_GROUP", direction =Relationship.OUTGOING)
	public Set<LogicalSymptomGroups>  logicalGroups = new HashSet<LogicalSymptomGroups>();	
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_LABS", direction =Relationship.OUTGOING)
	public Set<SymptomLabOrders>  labsOrdered ;	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_POLOCIES", direction =Relationship.OUTGOING)
	public Set<Policy>  policies = new HashSet<Policy>();	
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "START_LINKS", direction =Relationship.OUTGOING)
	public Set<LinkStart>  startLinks = new HashSet<LinkStart>();	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "END_LINKS", direction =Relationship.OUTGOING)
	public Set<LinkEnd>  endLinks = new HashSet<LinkEnd>();	
	
		

	public Set<SnomedCodes> getSnomedCodes() {
		return snomedCodes;
	}

	public void setSnomedCodes(Set<SnomedCodes> snomedCodes) {
		this.snomedCodes = snomedCodes;
	}



	public Set<RCodeDataStore> getrCodeDataStores() {
		return rCodeDataStores;
	}

	public void setrCodeDataStores(Set<RCodeDataStore> rCodeDataStores) {
		this.rCodeDataStores = rCodeDataStores;
	}
	
	


	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

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

	
	public String getScaleTimeLimitText() {
		return scaleTimeLimitText;
	}

	public void setScaleTimeLimitText(String scaleTimeLimitText) {
		this.scaleTimeLimitText = scaleTimeLimitText;
	}

	public Hashtable<String, Set<DataStore>> getDataList() {
		return dataList;
	}

	public void setDataList(Hashtable<String, Set<DataStore>> dataList) {
		this.dataList = dataList;
	}

	public Integer getTimeUnitDefault() {
		return timeUnitDefault;
	}

	public void setTimeUnitDefault(Integer timeUnitDefault) {
		this.timeUnitDefault = timeUnitDefault;
	}

	public String getDescriptorFile() {
		return descriptorFile;
	}

	public void setDescriptorFile(String descriptorFile) {
		this.descriptorFile = descriptorFile;
	}
	
	public Integer getCriticality() {
		return criticality;
	}

	public void setCriticality(Integer criticality) {
		this.criticality = criticality;
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
	
	public List<String> getDataStoreTemplates() {
		return dataStoreTemplates;
	}

	public void setDataStoreTemplates(List<String> dataStoreTemplates) {
		this.dataStoreTemplates = dataStoreTemplates;
	}

	public List<String> getRangeValues() {
		return rangeValues;
	}

	public void setRangeValues(List<String> rangeValues) {
		this.rangeValues = rangeValues;
	}

	public Integer getTimeRangeStart() {
		return timeRangeStart;
	}

	public void setTimeRangeStart(Integer timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}

	public Integer getTimeRangeStop() {
		return timeRangeStop;
	}

	public void setTimeRangeStop(Integer timeRangeStop) {
		this.timeRangeStop = timeRangeStop;
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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/*public String getICDRCode() {
		return ICDRCode;
	}

	public void setICDRCode(String iCDRCode) {
		ICDRCode = iCDRCode;
	}*/

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

	public String getEs_questionText() {
		return es_questionText;
	}

	public void setEs_questionText(String es_questionText) {
		this.es_questionText = es_questionText;
	}

	public Double getEcw_prior() {
		return ecw_prior;
	}

	public void setEcw_prior(Double ecw_prior) {
		this.ecw_prior = ecw_prior;
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

	/**
	 * @return the type
	 *//*
	public String getType() {
		return type;
	}

	*//**
	 * @param type the type to set
	 *//*
	public void setType(String type) {
		this.type = type;
	}
*/
	
	/**
	 * @return the logicalGroups
	 */
	public Set<LogicalSymptomGroups> getLogicalGroups() {
		return logicalGroups;
	}

	/**
	 * @param logicalGroups the logicalGroups to set
	 */
	public void setLogicalGroups(Set<LogicalSymptomGroups> logicalGroups) {
		this.logicalGroups = logicalGroups;
	}

	public Set<SymptomLabOrders> getLabsOrdered() {
		return labsOrdered;
	}

	public void setLabsOrdered(Set<SymptomLabOrders> labsOrdered) {
		this.labsOrdered = labsOrdered;
	}

	public String getSymptomType() {
		return symptomType;
	}

	public void setSymptomType(String symptomType) {
		this.symptomType = symptomType;
	}

	public Set<Policy> getPolicies() {
		return policies;
	}

	public void setPolicies(Set<Policy> policies) {
		this.policies = policies;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public Set<LinkStart> getStartLinks() {
		return startLinks;
	}

	public void setStartLinks(Set<LinkStart> startLinks) {
		this.startLinks = startLinks;
	}

	public Set<LinkEnd> getEndLinks() {
		return endLinks;
	}

	public void setEndLinks(Set<LinkEnd> endLinks) {
		this.endLinks = endLinks;
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

	public Boolean getRange() {
		return range;
	}

	public void setRange(Boolean range) {
		this.range = range;
	}



	
	
	/**
	 * @return the symptomSources
	 *//*
	public Set<SymptomSourceInfo> getSymptomSources() {
		return symptomSources;
	}

	*//**
	 * @param symptomSources the symptomSources to set
	 *//*
	public void setSymptomSources(Set<SymptomSourceInfo> symptomSources) {
		this.symptomSources = symptomSources;
	}

	
*/	
	
	
}
