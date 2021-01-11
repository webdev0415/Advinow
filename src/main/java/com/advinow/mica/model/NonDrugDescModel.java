package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class NonDrugDescModel  extends JSonModel {
	
	@JsonInclude(Include.NON_NULL)
	private Double likelihood;
	
	@JsonInclude(Include.NON_NULL)
    private String shortName;
	@JsonInclude(Include.NON_NULL)
	private String longName;
	@JsonInclude(Include.NON_NULL)
	private Integer priority;
	
	@JsonInclude(Include.NON_NULL)
	private Integer typeDescID;	
	
	@JsonInclude(Include.NON_NULL)
	@JsonIgnore
	private  Integer rank;
	
	@JsonInclude(Include.NON_NULL)
	private String description;
	
	@JsonInclude(Include.NON_NULL)
	private String notes;
	
	/*@JsonInclude(Include.NON_NULL)
	private Set<Integer> sources;*/
	
	@JsonInclude(Include.NON_NULL)
	private List<TreatmentSourcesModel> sourceInfo;

		
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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public List<TreatmentSourcesModel> getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(List<TreatmentSourcesModel> sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
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

}
