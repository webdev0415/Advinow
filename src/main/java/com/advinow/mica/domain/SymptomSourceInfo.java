package com.advinow.mica.domain;



import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class SymptomSourceInfo {
	
//	@JsonIgnore
	@Id
	@GeneratedValue
	private Long id;
		
	@JsonInclude(Include.NON_NULL)
	private String source;


	@JsonInclude(Include.NON_NULL)
	private Integer sourceID;
	
	@JsonInclude(Include.NON_NULL)
	private String sourceType;
	
	@JsonInclude(Include.NON_NULL)
	private String sourceTitle;
	
	
	@JsonInclude(Include.NON_NULL)
	private Integer rank=1;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public Integer getSourceID() {
		return sourceID;
	}


	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}


	public String getSourceType() {
		return sourceType;
	}


	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}


	public String getSourceTitle() {
		return sourceTitle;
	}


	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}
	

}
