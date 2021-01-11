package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class RCodeDataStore extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private String dsCode;
	
	@JsonInclude(Include.NON_NULL)
	private String m_icd10RCode;

	public String getDsCode() {
		return dsCode;
	}

	public void setDsCode(String dsCode) {
		this.dsCode = dsCode;
	}

	public String getM_icd10RCode() {
		return m_icd10RCode;
	}

	public void setM_icd10RCode(String m_icd10RCode) {
		this.m_icd10RCode = m_icd10RCode;
	}
	

}
