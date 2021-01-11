package com.advinow.mica.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class BadIllness extends Neo4jEntity {

	@JsonInclude(Include.NON_NULL)
	String	icd10Code;
	@JsonInclude(Include.NON_NULL)
	Integer	version;
	@JsonInclude(Include.NON_NULL)
	String	criticality;
	@JsonInclude(Include.NON_NULL)
	List<String>	invalidCategories;	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type = "BAD_SYMPTOMS", direction = Relationship.OUTGOING)
	private List<BadSymptoms> symptoms= new ArrayList<BadSymptoms>();
	public String getIcd10Code() {
		return icd10Code;
	}
	public void setIcd10Code(String icd10Code) {
		this.icd10Code = icd10Code;
	}
	public String getCriticality() {
		return criticality;
	}
	public void setCriticality(String criticality) {
		this.criticality = criticality;
	}
	
	public List<BadSymptoms> getSymptoms() {
		return symptoms;
	}
	public void setSymptoms(List<BadSymptoms> symptoms) {
		this.symptoms = symptoms;
	}
	public List<String> getInvalidCategories() {
		return invalidCategories;
	}
	public void setInvalidCategories(List<String> invalidCategories) {
		this.invalidCategories = invalidCategories;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

}
