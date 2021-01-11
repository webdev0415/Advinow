package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class SymptomTreatment extends Neo4jEntity {
		
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "SYMPTOM_HAS_TREATMENTS", direction = Relationship.OUTGOING)
	private Set<Treatment> treatments;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	   // @DateString("yy-MM-dd")  
		private Long modifiedDate;
		
		@JsonInclude(Include.NON_NULL)
		private Long createdDate; 
		

	public Set<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(Set<Treatment> treatments) {
		this.treatments = treatments;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	
}
