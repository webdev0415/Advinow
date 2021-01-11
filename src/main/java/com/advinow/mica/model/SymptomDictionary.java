package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * Holds templates,snomed and groups updated date  dictionaries.
 * 
 * @author Govinda Reddy
 *
 */
public class SymptomDictionary {
	    
	/* 
	 * Template dictionary 
	 */
     @JsonInclude(Include.NON_EMPTY)
	private Map<String,Map<String, Object>> symptom_id_dict = new Hashtable<String,Map<String, Object>>();
     

     @JsonInclude(Include.NON_EMPTY)
     private List<GenericQueryResultEntity> symptom_snomed_dict = new ArrayList<GenericQueryResultEntity>();
     
     @JsonInclude(Include.NON_EMPTY)
     private List<GenericQueryResultEntity> symptom_groups_dict = new ArrayList<GenericQueryResultEntity>();
     
     
	/*
	 * 
	 * Updated date dictionary.
	 */
     @JsonInclude(Include.NON_EMPTY)
     private Map<String, Object> groupsUpdatedDate = new HashMap<String, Object>();
     
     
     

	/**
	 * @return the symptom_id_dict
	 */
	public Map<String, Map<String, Object>> getSymptom_id_dict() {
		return symptom_id_dict;
	}

	/**
	 * @param symptom_id_dict the symptom_id_dict to set
	 */
	public void setSymptom_id_dict(Map<String, Map<String, Object>> symptom_id_dict) {
		this.symptom_id_dict = symptom_id_dict;
	}

	/**
	 * @return the groupsUpdatedDate
	 */
	public Map<String, Object> getGroupsUpdatedDate() {
		return groupsUpdatedDate;
	}

	/**
	 * @param groupsUpdatedDate the groupsUpdatedDate to set
	 */
	public void setGroupsUpdatedDate(Map<String, Object> groupsUpdatedDate) {
		this.groupsUpdatedDate = groupsUpdatedDate;
	}

	/**
	 * @return the symptom_snomed_dict
	 */
	public List<GenericQueryResultEntity> getSymptom_snomed_dict() {
		return symptom_snomed_dict;
	}

	/**
	 * @param symptom_snomed_dict the symptom_snomed_dict to set
	 */
	public void setSymptom_snomed_dict(
			List<GenericQueryResultEntity> symptom_snomed_dict) {
		this.symptom_snomed_dict = symptom_snomed_dict;
	}

	/**
	 * @return the symptom_groups_dict
	 */
	public List<GenericQueryResultEntity> getSymptom_groups_dict() {
		return symptom_groups_dict;
	}

	/**
	 * @param symptom_groups_dict the symptom_groups_dict to set
	 */
	public void setSymptom_groups_dict(
			List<GenericQueryResultEntity> symptom_groups_dict) {
		this.symptom_groups_dict = symptom_groups_dict;
	}

	
	/**
	 * @return the symptom_snomed_dict
	 */
	/*public Map<Pair<String, String>, Object> getSymptom_snomed_dict() {
		return symptom_snomed_dict;
	}

	*//**
	 * @param symptom_snomed_dict the symptom_snomed_dict to set
	 *//*
	public void setSymptom_snomed_dict(
			Map<Pair<String, String>, Object> symptom_snomed_dict) {
		this.symptom_snomed_dict = symptom_snomed_dict;
	}*/

	
     
}
