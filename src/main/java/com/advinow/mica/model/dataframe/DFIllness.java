/**
 * 
 */
package com.advinow.mica.model.dataframe;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.google.gson.annotations.SerializedName;

/**
 * @author Developer
 *
 */
@SuppressWarnings("serial")
@QueryResult
public @Data class DFIllness implements Serializable {
	
	
	private transient  Integer version;
	
	@SerializedName("icd10")
	private String icd10Code;
	
	private transient  String source;
	
	private Integer criticality=0;
	
	private String name;
	
	private String group=null;
	
	private Integer cluster=0;
	
	private Integer prevalence = 0;

	
	public DFIllness(String icd10Code, String source, Integer prevalence,
			String name,Integer criticality,String group,Integer cluster) {
		super();
		this.icd10Code = icd10Code;
		this.source = source;
		this.prevalence = prevalence;
		this.name = name;
		this.criticality = criticality;
		this.group= group;
		this.cluster=cluster;
		
	}
	
	/**
	 * 
	 */
	public DFIllness() {
	}


}
