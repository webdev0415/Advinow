package com.advinow.mica.domain;



import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PolicyRef {
	
	@Id
	@GeneratedValue
	//@JsonIgnore
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private String es_name;
	
	@JsonInclude(Include.NON_NULL)
	private Integer policyID;

	public Integer getPolicyID() {
		return policyID;
	}

	public void setPolicyID(Integer policyID) {
		this.policyID = policyID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEs_name() {
		return es_name;
	}

	public void setEs_name(String es_name) {
		this.es_name = es_name;
	}

}
