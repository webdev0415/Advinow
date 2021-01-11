package com.advinow.mica.domain.queryresult;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.advinow.mica.domain.SymptomCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Query result wrapper for the neo4j queries.
 * 
 * @author Govinda Reddy
 *
 */
@QueryResult
public class SourceQueryResultEntity {
@JsonInclude(Include.NON_EMPTY)
private List<Integer> sources;

public List<Integer> getSources() {
	return sources;
}

public void setSources(List<Integer> sources) {
	this.sources = sources;
}

}
