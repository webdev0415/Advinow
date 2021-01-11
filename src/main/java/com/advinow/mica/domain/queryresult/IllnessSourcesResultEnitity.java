package com.advinow.mica.domain.queryresult;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.advinow.mica.domain.Symptom;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@QueryResult
public class IllnessSourcesResultEnitity {
	

	@JsonInclude(Include.NON_NULL)
	public String icd10Code;

	@JsonInclude(Include.NON_NULL)
	List<Symptom> 	symptoms;

	

}
