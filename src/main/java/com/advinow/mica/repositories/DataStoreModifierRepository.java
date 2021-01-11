package com.advinow.mica.repositories;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DataStoreModifier;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface DataStoreModifierRepository extends	Neo4jRepository<DataStoreModifier,Long> {
	@Query("MATCH(ds:SymptomDataStore)-[dsm]-(dm:DataStoreModifier) WHERE  id(ds)= $ID  RETURN dm")		
	Set<DataStoreModifier> getAllModifiersForDS(Long ID);
	
}
