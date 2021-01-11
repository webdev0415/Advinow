/**
 * 
 */
package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author govind
 *
 */
@NodeEntity
public class Coding_Rules extends Neo4jEntity {
	
	private Integer rule_id;
	
	private String description;

	/**
	 * @return the rule_id
	 */
	public Integer getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id the rule_id to set
	 */
	public void setRule_id(Integer rule_id) {
		this.rule_id = rule_id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	


}
