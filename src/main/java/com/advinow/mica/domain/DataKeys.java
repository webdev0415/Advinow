package com.advinow.mica.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class DataKeys extends Neo4jEntity {
	
	private String title ;
	
	@JsonInclude(Include.NON_NULL)
	private String es_title;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "RELATED_TO", direction =Relationship.OUTGOING)
	public Set<DataStore> dataStoreList;
	
	public DataKeys(){
		dataStoreList = new HashSet<DataStore>();
	}


	public Set<DataStore> getDataStoreList() {
		return dataStoreList;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getEs_title() {
		return es_title;
	}


	public void setEs_title(String es_title) {
		this.es_title = es_title;
	}


	
}
