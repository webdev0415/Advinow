package com.advinow.mica.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DrugDescModel extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	/*@JsonInclude(Include.NON_NULL)
	private Integer priority;
*/	
	@JsonInclude(Include.NON_NULL)
	@JsonIgnore
	private Integer rank=1;
	
	@JsonInclude(Include.NON_NULL)
	private Integer ingredientRxcui;
	
	@JsonInclude(Include.NON_NULL)
	private String ingredientRxcuiDesc;
	
	@JsonInclude(Include.NON_NULL)
	private String addedBy;
	
	@JsonInclude(Include.NON_NULL)
	private String tty;
	
	
	@JsonInclude(Include.NON_NULL)
	private String drugName;
	
	@JsonInclude(Include.NON_NULL)
	private String productId;
	
	
	/*@JsonInclude(Include.NON_NULL)
	private Set<Integer> sources;*/
	
	//@JsonInclude(Include.NON_NULL)
	private List<TreatmentSourcesModel> sourceInfo = new ArrayList<TreatmentSourcesModel>();

	
	@JsonInclude(Include.NON_EMPTY)
	private List<RxNormsModel> rxNorms = new ArrayList<RxNormsModel>();
	
	//@JsonInclude(Include.NON_NULL)
	private DrugDosageModel dosageRecommendation = new DrugDosageModel();
	
	//@JsonInclude(Include.NON_NULL)
	private List<TreatmentPolicyModel> policies  = new ArrayList<TreatmentPolicyModel>();
	
	
	

	/*public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}*/

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

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
	}
*/
	/**
	 * @param sources the sources to set
	 */
	/*public void setSources(Set<Integer> sources) {
		this.sources = sources;
	}*/

	public List<TreatmentSourcesModel> getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(List<TreatmentSourcesModel> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public List<RxNormsModel> getRxNorms() {
		return rxNorms;
	}

	public void setRxNorms(List<RxNormsModel> rxNorms) {
		this.rxNorms = rxNorms;
	}



	public List<TreatmentPolicyModel> getPolicies() {
		return policies;
	}

	public void setPolicies(List<TreatmentPolicyModel> policies) {
		this.policies = policies;
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

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getTty() {
		return tty;
	}

	public void setTty(String tty) {
		this.tty = tty;
	}

	/*public String getRxcuiDesc() {
		return rxcuiDesc;
	}

	public void setRxcuiDesc(String rxcuiDesc) {
		this.rxcuiDesc = rxcuiDesc;
	}*/

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public DrugDosageModel getDosageRecommendation() {
		return dosageRecommendation;
	}

	public void setDosageRecommendation(DrugDosageModel dosageRecommendation) {
		this.dosageRecommendation = dosageRecommendation;
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
