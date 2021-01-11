package com.advinow.mica.domain;

import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@NodeEntity
public class SnomedCodes extends Neo4jEntity {
	
	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	private List<Long> conceptID;
	
	@JsonInclude(Include.NON_NULL)
	private String listValueCode;
	
	@JsonInclude(Include.NON_NULL)
	private String expression;
	
	@JsonInclude(Include.NON_NULL)
	private String type;
	


	public String getListValueCode() {
		return listValueCode;
	}

	public void setListValueCode(String listValueCode) {
		this.listValueCode = listValueCode;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the conceptID
	 */
	public List<Long> getConceptID() {
		return conceptID;
	}

	/**
	 * @param conceptID the conceptID to set
	 */
	public void setConceptID(List<Long> conceptID) {
		this.conceptID = conceptID;
	}


}
