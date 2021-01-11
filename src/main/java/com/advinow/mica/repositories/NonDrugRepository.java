package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.NonDrug;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface NonDrugRepository  extends Neo4jRepository<NonDrug,Long> {

}
