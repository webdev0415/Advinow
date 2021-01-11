package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.TreatmentGroup;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface TreatmentGroupRespository  extends Neo4jRepository<TreatmentGroup,Long> {

}
