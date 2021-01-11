package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@NodeEntity
public class SymptomGroup extends Neo4jEntity {

	private String groupID;
		
	private Long updatedDate;
		
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GROUP_HAS_SECTION", direction = Relationship.OUTGOING)
	private Set<Section> sections;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GROUP_HAS_CATEGORY", direction = Relationship.OUTGOING)
	private Set<SymptomCategory> categories;
	
	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	public Set<SymptomCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<SymptomCategory> categories) {
		this.categories = categories;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

		
}
