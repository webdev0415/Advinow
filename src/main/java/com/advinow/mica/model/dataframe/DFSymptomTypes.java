package com.advinow.mica.model.dataframe;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import org.springframework.data.neo4j.annotation.QueryResult;
@SuppressWarnings("serial")
@QueryResult
public @Data class DFSymptomTypes implements Serializable {
	
	public String code;
	
	public String symptomType;
	
	public String name;
	
	public Boolean basicSymptom;
	
	public Boolean partOfGroup;
	
	public String symptomGroup;

	public List <String > bodyLocations;
	
	public List <String > subGroups = null;
	
	public String groupName;
	
	public String categoryID;
	
	public Boolean displaySymptom;
	
}