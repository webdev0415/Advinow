package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class TreatmentGroup extends Neo4jEntity {
	
	private Integer rank= 1;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GROUP_HAS_DRUGS", direction = Relationship.OUTGOING)
	private Set<Drug> drugs;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GROUP_HAS_RX_DRUGS", direction = Relationship.OUTGOING)
	private Set<DrugRx> drugRx;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "GROUP_HAS_NON_DRUGS", direction = Relationship.OUTGOING)
	private Set<NonDrug> nonDrungs;
	

	@JsonInclude(Include.NON_NULL)
	private String added;

	public Set<Drug> getDrugs() {
		return drugs;
	}

	public void setDrugs(Set<Drug> drugs) {
		this.drugs = drugs;
	}

	public Set<NonDrug> getNonDrungs() {
		return nonDrungs;
	}

	public void setNonDrungs(Set<NonDrug> nonDrungs) {
		this.nonDrungs = nonDrungs;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Set<DrugRx> getDrugRx() {
		return drugRx;
	}

	public void setDrugRx(Set<DrugRx> drugRx) {
		this.drugRx = drugRx;
	}

	public String getAdded() {
		return added;
	}

	public void setAdded(String added) {
		this.added = added;
	}



	
	
	
	
}
