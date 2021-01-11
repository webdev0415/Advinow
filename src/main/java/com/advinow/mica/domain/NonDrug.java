package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class NonDrug extends Neo4jEntity {
	/*
	private String drugName;*/
	
	private Integer priority= 0;
	
	private Integer rank = 0;
	
	private Integer typeDescID;
	
	private String notes;
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	/*@JsonInclude(Include.NON_NULL)
	private Set<Integer> sources;*/
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "NDRUG_TRT_SOURCES", direction = Relationship.OUTGOING)
	private Set<TreatmentSources> sourceInfo;
	
	public Set<TreatmentSources> getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(Set<TreatmentSources> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
	/*@JsonInclude(Include.NON_NULL)
	private Set<Integer> rxNormCode;
	*/
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getTypeDescID() {
		return typeDescID;
	}
	public void setTypeDescID(Integer typeDescID) {
		this.typeDescID = typeDescID;
	}
	/*public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}*/
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	/**
	 * @return the sources
	 */
	/*public Set<Integer> getSources() {
		return sources;
	}*/
	/**
	 * @param sources the sources to set
	 */
	/*public void setSources(Set<Integer> sources) {
		this.sources = sources;
	}*/
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Double getLikelihood() {
		return likelihood;
	}
	public void setLikelihood(Double likelihood) {
		this.likelihood = likelihood;
	}
	
/*	public Set<Integer> getRxNormCode() {
		return rxNormCode;
	}
	public void setRxNormCode(Set<Integer> rxNormCode) {
		this.rxNormCode = rxNormCode;
	}*/
	
}
