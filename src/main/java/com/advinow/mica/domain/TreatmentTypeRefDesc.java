package com.advinow.mica.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class TreatmentTypeRefDesc extends Neo4jEntity {
	
	private String shortName;
	
	private String longName;
	
	private Boolean defaultValue;
	
	private Integer priority;
	
	private Integer typeDescID;
	
	private Integer rank;
	
	private String description;
	
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "TRT_HAS_SNOMED_CODES", direction =Relationship.OUTGOING)
	public Set<SnomedCodes>  snomedCodes = new HashSet<SnomedCodes>();
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "TRT_HAS_CPTCODES", direction =Relationship.OUTGOING)
	public Set<CPTCodes>  cPTCodes = new HashSet<CPTCodes>();
	

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Boolean defaultValue) {
		this.defaultValue = defaultValue;
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

	public Integer getTypeDescID() {
		return typeDescID;
	}

	public void setTypeDescID(Integer typeDescID) {
		this.typeDescID = typeDescID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * @return the snomedCodes
	 */
	public Set<SnomedCodes> getSnomedCodes() {
		return snomedCodes;
	}

	/**
	 * @param snomedCodes the snomedCodes to set
	 */
	public void setSnomedCodes(Set<SnomedCodes> snomedCodes) {
		this.snomedCodes = snomedCodes;
	}

	/**
	 * @return the cPTCodes
	 */
	public Set<CPTCodes> getcPTCodes() {
		return cPTCodes;
	}

	/**
	 * @param cPTCodes the cPTCodes to set
	 */
	public void setcPTCodes(Set<CPTCodes> cPTCodes) {
		this.cPTCodes = cPTCodes;
	}


	
}
