package com.advinow.mica.domain.queryresult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.advinow.mica.domain.SymptomCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * Query result wrapper for the neo4j queries.
 * 
 * @author Govinda Reddy
 *
 */
@QueryResult
public class GenericQueryResultEntity {

	@JsonInclude(Include.NON_EMPTY)
	private String symptomID;
	
	@JsonInclude(Include.NON_EMPTY)
	private String symptomName;

	@JsonInclude(Include.NON_EMPTY)
	private String listValue;

	@JsonInclude(Include.NON_EMPTY)
	private List<Integer> rule_id;
	
	@JsonInclude(Include.NON_EMPTY)
	private String snomedName;

	@JsonInclude(Include.NON_EMPTY)
	private Collection<Map<String, Object>> literalMap;
	
	@JsonInclude(Include.NON_EMPTY)
	private String groupName;
	
	@JsonInclude(Include.NON_EMPTY)
	private SymptomCategory sCategory;
	
	@JsonInclude(Include.NON_EMPTY)
	private Set<String> symptoms;
	

	@JsonInclude(Include.NON_EMPTY)
	private String icd10code; 
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer version; 
	
	
	@JsonInclude(Include.NON_EMPTY)
	private Float prior ;
	
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer cluster;
	
	@JsonInclude(Include.NON_NULL)
	String descriptorID;
	
	@JsonInclude(Include.NON_NULL)
	String descriptorName;
	
	@JsonInclude(Include.NON_NULL)
	Float dcl;
	
	@JsonInclude(Include.NON_NULL)
	String sectionID;
	
	@JsonInclude(Include.NON_NULL)
	String optionKey;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> referenceTypes;
	
	@JsonInclude(Include.NON_NULL)
	String imageName;
	
	
	@JsonInclude(Include.NON_NULL)
	String dfIcd10Code;
	
	
	@JsonInclude(Include.NON_NULL)
	String multipleValues;
	
	@JsonInclude(Include.NON_NULL)
	Float score;
	
	@JsonInclude(Include.NON_EMPTY)
	public List<String> subGroups;
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer groupID;
	
	@JsonInclude(Include.NON_EMPTY)
	private Integer painSwellingID;
	
	@JsonInclude(Include.NON_NULL)
	String symptomGroup;

	@JsonInclude(Include.NON_NULL)
	String categoryName;
	
	@JsonInclude(Include.NON_NULL)
	String symptomGroupName;
	
	@JsonInclude(Include.NON_NULL)
	List<String> logicalGroupNames = null;
	
	@JsonInclude(Include.NON_NULL)
	List<Integer> logicalGroupIDList = null;
	
	
	@JsonInclude(Include.NON_NULL)
	List<String> deGroupNames = null;
	

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
	 * @return the literalMap
	 */
	public Collection<Map<String, Object>> getLiteralMap() {
		return literalMap;
	}

	/**
	 * @param literalMap the literalMap to set
	 */
	public void setLiteralMap(Collection<Map<String, Object>> literalMap) {
		this.literalMap = literalMap;
	}

	/**
	 * @return the snomedName
	 */
	public String getSnomedName() {
		return snomedName;
	}

	/**
	 * @param snomedName the snomedName to set
	 */
	public void setSnomedName(String snomedName) {
		this.snomedName = snomedName;
	}

	/**
	 * @return the listValue
	 */
	public String getListValue() {
		return listValue;
	}

	/**
	 * @param listValue the listValue to set
	 */
	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the sCategory
	 */
	public SymptomCategory getsCategory() {
		return sCategory;
	}

	/**
	 * @param sCategory the sCategory to set
	 */
	public void setsCategory(SymptomCategory sCategory) {
		this.sCategory = sCategory;
	}

	/**
	 * @return the symptoms
	 */
	public Set<String> getSymptoms() {
		return symptoms;
	}

	/**
	 * @param symptoms the symptoms to set
	 */
	public void setSymptoms(Set<String> symptoms) {
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
	 * @return the cluster
	 */
	public Integer getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(Integer cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the descriptorID
	 */
	public String getDescriptorID() {
		return descriptorID;
	}

	/**
	 * @param descriptorID the descriptorID to set
	 */
	public void setDescriptorID(String descriptorID) {
		this.descriptorID = descriptorID;
	}

	/**
	 * @return the descriptorName
	 */
	public String getDescriptorName() {
		return descriptorName;
	}

	/**
	 * @param descriptorName the descriptorName to set
	 */
	public void setDescriptorName(String descriptorName) {
		this.descriptorName = descriptorName;
	}

	/**
	 * @return the dcl
	 */
	public Float getDcl() {
		return dcl;
	}

	/**
	 * @param dcl the dcl to set
	 */
	public void setDcl(Float dcl) {
		this.dcl = dcl;
	}

	/**
	 * @return the sectionID
	 */
	public String getSectionID() {
		return sectionID;
	}

	/**
	 * @param sectionID the sectionID to set
	 */
	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}

	/**
	 * @return the rule_id
	 */
	public List<Integer> getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id the rule_id to set
	 */
	public void setRule_id(List<Integer> rule_id) {
		this.rule_id = rule_id;
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
	 * @return the referenceTypes
	 */
	public List<String> getReferenceTypes() {
		return referenceTypes;
	}

	/**
	 * @param referenceTypes the referenceTypes to set
	 */
	public void setReferenceTypes(List<String> referenceTypes) {
		this.referenceTypes = referenceTypes;
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
	 * @return the dfIcd10Code
	 */
	public String getDfIcd10Code() {
		return dfIcd10Code;
	}

	/**
	 * @param dfIcd10Code the dfIcd10Code to set
	 */
	public void setDfIcd10Code(String dfIcd10Code) {
		this.dfIcd10Code = dfIcd10Code;
	}

	/**
	 * @return the score
	 */
	public Float getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Float score) {
		this.score = score;
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
	 * @return the groupID
	 */
	public Integer getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
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
	 * @return the symptomGroup
	 */
	public String getSymptomGroup() {
		return symptomGroup;
	}

	/**
	 * @param symptomGroup the symptomGroup to set
	 */
	public void setSymptomGroup(String symptomGroup) {
		this.symptomGroup = symptomGroup;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the symptomGroupName
	 */
	public String getSymptomGroupName() {
		return symptomGroupName;
	}

	/**
	 * @param symptomGroupName the symptomGroupName to set
	 */
	public void setSymptomGroupName(String symptomGroupName) {
		this.symptomGroupName = symptomGroupName;
	}

	/**
	 * @return the icd10code
	 */
	public String getIcd10code() {
		return icd10code;
	}

	/**
	 * @param icd10code the icd10code to set
	 */
	public void setIcd10code(String icd10code) {
		this.icd10code = icd10code;
	}

	/**
	 * @return the logicalGroupNames
	 */
	public List<String> getLogicalGroupNames() {
		return logicalGroupNames;
	}

	/**
	 * @param logicalGroupNames the logicalGroupNames to set
	 */
	public void setLogicalGroupNames(List<String> logicalGroupNames) {
		this.logicalGroupNames = logicalGroupNames;
	}

	/**
	 * @return the logicalGroupIDList
	 */
	public List<Integer> getLogicalGroupIDList() {
		return logicalGroupIDList;
	}

	/**
	 * @param logicalGroupIDList the logicalGroupIDList to set
	 */
	public void setLogicalGroupIDList(List<Integer> logicalGroupIDList) {
		this.logicalGroupIDList = logicalGroupIDList;
	}

	public List<String> getDeGroupNames() {
		return deGroupNames;
	}

	public void setDeGroupNames(List<String> deGroupNames) {
		this.deGroupNames = deGroupNames;
	}

	

	

	
}
