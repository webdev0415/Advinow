package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.RawSymptomTemplate;

@Repository
public interface RawSymptomTemplateRepository extends Neo4jRepository<RawSymptomTemplate,Long> {
	
	@Depth(value = 0)
	@Query("MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source RETURN count(DISTINCT st)")
	Integer findBySourceCount(String source);
	
	@Depth(value = 0)
	@Query(value="MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source RETURN DISTINCT st  ORDER BY st.code ASC SKIP $skip LIMIT $limit")//, countQuery="MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source RETURN count(DISTINCT st)")
	List<RawSymptomTemplate> findBySource(String source, Integer skip, Integer limit);
	
	@Depth(value = 0)
	@Query(value="MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source AND toUpper(st.name) CONTAINS($name) RETURN DISTINCT st  ORDER BY st.code ASC SKIP $skip LIMIT $limit")//,countQuery="MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source AND toUpper(st.name) CONTAINS($name) RETURN count(DISTINCT st)")
	List<RawSymptomTemplate> findBySourceAndName(String source,String name, Integer skip, Integer limit);
	
	@Depth(value = 0)
	@Query("MATCH(st:RawSymptomTemplate) WHERE  toUpper(st.source)=$source AND toUpper(st.name) CONTAINS($name) RETURN count(DISTINCT st)")
	Integer findBySourceAndNameCount(String source,String name);
	
}
