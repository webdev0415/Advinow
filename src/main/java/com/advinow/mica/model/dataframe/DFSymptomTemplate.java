package com.advinow.mica.model.dataframe;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * 
 * @author Govinda Reddy
 *
 */
@SuppressWarnings("serial")
@QueryResult
public @Data class DFSymptomTemplate implements Serializable {

	String symptomID;
    Float symptomAntithesis;
    String dsCode;
    Float dsAntithesis;
    String dsAlieasName;
    String dsName;
	
}
