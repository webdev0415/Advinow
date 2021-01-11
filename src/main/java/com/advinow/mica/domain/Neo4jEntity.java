/**
 * 
 */
package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Govinda Reddy
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public  abstract class Neo4jEntity {
	
	@Id
	@GeneratedValue
	private Long id;

	@JsonInclude(Include.NON_NULL)
	@Index(unique = true) 	
	private String code;
	
	@JsonInclude(Include.NON_NULL)
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	private String es_name;
	
	
	// enable this attribute if more than one language required.
	// refer more infor in https://graphaware.com/neo4j/2016/09/29/internationalization-with-spring-neo4j.html
	/*@JsonInclude(Include.NON_NULL)
	private String es_MX;*/
		
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || id == null || getClass() != o.getClass())
			return false;
	    
		Neo4jEntity entity = (Neo4jEntity) o;

		if (!id.equals(entity.id))
			return false;
			
		return true;
	}

	@Override
	public int hashCode() {
		return (id == null) ? -1 : id.hashCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEs_name() {
		return es_name;
	}

	public void setEs_name(String es_name) {
		this.es_name = es_name;
	}
}
