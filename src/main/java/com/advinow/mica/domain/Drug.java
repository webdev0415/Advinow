package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class Drug extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Integer rank;
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	/*@JsonInclude(Include.NON_NULL)
	private Set<Integer> sources;*/
	
/*	@JsonInclude(Include.NON_NULL)
	private Integer priority;*/
	
/*	@JsonInclude(Include.NON_NULL)
	private Set<Integer> rxNormCode;*/
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUGS_HAS_RXNORMS", direction = Relationship.OUTGOING)
	private Set<RxNorms> rxNorms;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_TRT_SOURCES", direction = Relationship.OUTGOING)
	private Set<TreatmentSources> sourceInfo;
	
/*	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_HAS_DOSAGES", direction = Relationship.OUTGOING)
	private Set<DrugDosage> dosage;*/

/*
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_HAS_POLICIES", direction = Relationship.OUTGOING)
	private Set<TreatmentPolicy> policies;*/
	
	
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

/*	public Set<Integer> getRxNormCode() {
		return rxNormCode;
	}

	public void setRxNormCode(Set<Integer> rxNormCode) {
		this.rxNormCode = rxNormCode;
	}*/

	public Set<RxNorms> getRxNorms() {
		return rxNorms;
	}

	public void setRxNorms(Set<RxNorms> rxNorms) {
		this.rxNorms = rxNorms;
	}

	public Set<TreatmentSources> getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(Set<TreatmentSources> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}




	public Double getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(Double likelihood) {
		this.likelihood = likelihood;
	}





	/*public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}*/
}