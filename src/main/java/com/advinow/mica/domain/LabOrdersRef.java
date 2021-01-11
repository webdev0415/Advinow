/**
 * 
 */
package com.advinow.mica.domain;

import lombok.Data;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



/**
 * @author Govinda Reddy
 *
 */
@NodeEntity
public @Data class LabOrdersRef  {

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private String es_name;
		
	@JsonInclude(Include.NON_NULL)
	private Integer orderID;

	@JsonInclude(Include.NON_NULL)
	private String loincCode;

	
}
