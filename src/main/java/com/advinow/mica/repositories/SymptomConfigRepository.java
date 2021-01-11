package com.advinow.mica.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomConfig;
/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomConfigRepository extends Neo4jRepository<SymptomConfig,Long> {

	@Query("MATCH(config:SymptomConfig{type:$type}) return config")
	SymptomConfig getSymptomConfig(String type);

}
