package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 
 * @author govind
 *o
 */
@NodeEntity
public class SymptomCategory extends Neo4jEntity {
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "SYMPTOM_TEMPLATE", direction =Relationship.OUTGOING)
	public Set<SymptomTemplate> symptomTemplates ;
	
	@JsonInclude(Include.NON_EMPTY) 
	@JsonIgnore
	private List<String> bodyLocations=  new ArrayList<String>();
	
	@JsonInclude(Include.NON_EMPTY) 
	@JsonIgnore
	private List<String> es_bodyLocations=  new ArrayList<String>();
	
	@JsonInclude(Include.NON_NULL)
	private String parent;
	
	@JsonInclude(Include.NON_NULL)
	private Integer orderID;
	

	@JsonInclude(Include.NON_NULL)
	private String bodypartCode;
		
	
	public List<String> getBodyLocations() {
		return bodyLocations;
	}



	public void setBodyLocations(List<String> bodyLocations) {
		this.bodyLocations = bodyLocations;
	}



	public String getParent() {
		return parent;
	}



	public void setParent(String parent) {
		this.parent = parent;
	}



	/*public Set<Symptom> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(Set<Symptom> symptoms) {
		this.symptoms = symptoms;
	}*/


	public Set<SymptomTemplate> getSymptomTemplates() {
		return symptomTemplates;
	}



	public void setSymptomTemplates(Set<SymptomTemplate> symptomTemplates) {
		this.symptomTemplates = symptomTemplates;
	}



	public List<String> getEs_bodyLocations() {
		return es_bodyLocations;
	}



	public void setEs_bodyLocations(List<String> es_bodyLocations) {
		this.es_bodyLocations = es_bodyLocations;
	}



	/**
	 * @return the orderID
	 */
	public Integer getOrderID() {
		return orderID;
	}



	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}



	/**
	 * @return the bodypartCode
	 */
	public String getBodypartCode() {
		return bodypartCode;
	}



	/**
	 * @param bodypartCode the bodypartCode to set
	 */
	public void setBodypartCode(String bodypartCode) {
		this.bodypartCode = bodypartCode;
	}

	
}
