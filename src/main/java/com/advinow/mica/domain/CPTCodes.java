package com.advinow.mica.domain;

import java.util.List;




import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@NodeEntity
public class CPTCodes extends Neo4jEntity {
	
	@JsonInclude(Include.NON_NULL)
	private List<String> cptCode;

	/**
	 * @return the cptCode
	 */
	public List<String> getCptCode() {
		return cptCode;
	}

	/**
	 * @param cptCode the cptCode to set
	 */
	public void setCptCode(List<String> cptCode) {
		this.cptCode = cptCode;
	}

	

	
}
