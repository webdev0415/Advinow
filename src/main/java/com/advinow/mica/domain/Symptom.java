package com.advinow.mica.domain;

import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class Symptom extends Neo4jEntity {

	
	//@JsonInclude(Include.NON_NULL)
	//private Boolean minDiagCriteria=  false;
	
	//@JsonInclude(Include.NON_NULL)
	//private Boolean medNecessary=false;
	

	
	@JsonInclude(Include.NON_NULL)
	private List<String> bodyParts;
	
			
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_DATASTORE", direction = Relationship.OUTGOING)
	private Set<SymptomDataStore> symptomDataStore;


	public Set<SymptomDataStore> getSymptomDataStore() {
		return symptomDataStore;
	}

	public void setSymptomDataStore(Set<SymptomDataStore> symptomDataStore) {
		this.symptomDataStore = symptomDataStore;
	}


	public List<String> getBodyParts() {
		return bodyParts;
	}

	public void setBodyParts(List<String> bodyParts) {
		this.bodyParts = bodyParts;
	}


}