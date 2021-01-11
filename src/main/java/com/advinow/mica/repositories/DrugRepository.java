package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Drug;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface DrugRepository extends Neo4jRepository<Drug,Long> {

}
