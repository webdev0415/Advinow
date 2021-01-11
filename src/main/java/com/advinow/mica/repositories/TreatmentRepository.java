package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Treatment;



/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface TreatmentRepository extends Neo4jRepository<Treatment,Long> {

}
