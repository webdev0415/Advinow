package com.advinow.mica.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public @Data class LinkEnd {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "HAS_ITEMS", direction =Relationship.OUTGOING)
	public Set<LinkListItem>  listItems = new HashSet<LinkListItem>();	
	

	
}
