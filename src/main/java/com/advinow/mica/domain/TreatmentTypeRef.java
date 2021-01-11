package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

@NodeEntity
public class TreatmentTypeRef extends Neo4jEntity {
	
	private String type;
	
	private Integer typeID;
	
	private Boolean active;
	
	@Transient	
	List<String> defaultTreatments =   new ArrayList<String>();
	
	
	//@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "TREATMENT_HAS_DETAILS", direction = Relationship.OUTGOING)
	private Set<TreatmentTypeRefDesc> treatmentDetails;

	

	public Set<TreatmentTypeRefDesc> getTreatmentDetails() {
		return treatmentDetails;
	}

	public void setTreatmentDetails(Set<TreatmentTypeRefDesc> treatmentDetails) {
		this.treatmentDetails = treatmentDetails;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getDefaultTreatments() {
		return defaultTreatments;
	}

	public void setDefaultTreatments(List<String> defaultTreatments) {
		this.defaultTreatments = defaultTreatments;
	}

	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
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

	

}
