package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DrugRx;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface DrugRxRepository extends Neo4jRepository<DrugRx,Long> {

}
