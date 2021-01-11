package com.advinow.mica.repositories;


import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomDataStore;


/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomDataStoreRepository extends Neo4jRepository<SymptomDataStore,Long>{
  

}
