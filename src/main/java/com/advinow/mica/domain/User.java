package com.advinow.mica.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Govinda Reddy
 *
 */
@NodeEntity
public class User extends Neo4jEntity {

	@Index(unique=true)
	private Integer userID;
	
	@JsonInclude(Include.NON_NULL)
	@Relationship(type = "ILLNESS_HAS_AUTHOR", direction =Relationship.OUTGOING)
	public Set<Illness>  illnesses = new HashSet<Illness>();


	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Set<Illness> getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(Set<Illness> illnesses) {
		this.illnesses = illnesses;
	}

	
}
