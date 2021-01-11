package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class Section extends Neo4jEntity {
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "SECTION_HAS_CATEGORY", direction = Relationship.OUTGOING)
	private Set<SymptomCategory> symptomCategories;

	public Set<SymptomCategory> getSymptomCategories() {
		return symptomCategories;
	}

	public void setSymptomCategories(Set<SymptomCategory> symptomCategories) {
		this.symptomCategories = symptomCategories;
	}

}
