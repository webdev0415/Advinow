package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * 
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class Treatment extends Neo4jEntity {
	
	private Integer typeID;
	@Relationship(type = "TYPE_HAS_GROUPS", direction = Relationship.OUTGOING)
	private Set<TreatmentGroup> groups;

	
	
	public Integer getTypeID() {
		return typeID;
	}

	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}

	public Set<TreatmentGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<TreatmentGroup> groups) {
		this.groups = groups;
	}
	
}
