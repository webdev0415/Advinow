package com.advinow.mica.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class DrugRx extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private Integer rank;
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	@JsonInclude(Include.NON_NULL)
	private String productId;
	
	// active ingredient rxcui or generic rxcui
	@JsonInclude(Include.NON_NULL)
	private Integer ingredientRxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String ingredientRxcuiDesc;
	
/*	@JsonInclude(Include.NON_NULL)
	private Integer rxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String rxcuiDesc;*/
	
	
	@JsonInclude(Include.NON_NULL)
	private String tty;
	
	@JsonInclude(Include.NON_NULL)
	String addedBy;
	
	@JsonInclude(Include.NON_NULL)
     Boolean offlabel ;
	
/*	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUGS_HAS_RXNORMS", direction = Relationship.OUTGOING)
	private Set<RxNorms> rxNorms;*/
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_TRT_SOURCES", direction = Relationship.OUTGOING)
	private Set<TreatmentSources> sourceInfo;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_HAS_DOSAGE", direction = Relationship.OUTGOING)
	private DrugDosage dosage;


	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "DRUG_HAS_POLICIES", direction = Relationship.OUTGOING)
	private Set<TreatmentPolicy> policies;


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public Double getLikelihood() {
		return likelihood;
	}


	public void setLikelihood(Double likelihood) {
		this.likelihood = likelihood;
	}


/*	public Integer getRxcui() {
		return rxcui;
	}


	public void setRxcui(Integer rxcui) {
		this.rxcui = rxcui;
	}
*/

	public String getTty() {
		return tty;
	}


	public void setTty(String tty) {
		this.tty = tty;
	}

 

	public Set<TreatmentSources> getSourceInfo() {
		return sourceInfo;
	}


	public void setSourceInfo(Set<TreatmentSources> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}




	public Set<TreatmentPolicy> getPolicies() {
		return policies;
	}


	public void setPolicies(Set<TreatmentPolicy> policies) {
		this.policies = policies;
	}


	/*public String getRxcuiDesc() {
		return rxcuiDesc;
	}


	public void setRxcuiDesc(String rxcuiDesc) {
		this.rxcuiDesc = rxcuiDesc;
	}

*/
	public String getAddedBy() {
		return addedBy;
	}


	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public DrugDosage getDosage() {
		return dosage;
	}


	public void setDosage(DrugDosage dosage) {
		this.dosage = dosage;
	}


	public Boolean getOfflabel() {
		return offlabel;
	}


	public void setOfflabel(Boolean offlabel) {
		this.offlabel = offlabel;
	}


	public Integer getIngredientRxcui() {
		return ingredientRxcui;
	}


	public void setIngredientRxcui(Integer ingredientRxcui) {
		this.ingredientRxcui = ingredientRxcui;
	}


	public String getIngredientRxcuiDesc() {
		return ingredientRxcuiDesc;
	}


	public void setIngredientRxcuiDesc(String ingredientRxcuiDesc) {
		this.ingredientRxcuiDesc = ingredientRxcuiDesc;
	}
	
	
	
}