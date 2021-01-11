package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DataKeys;

/**
 * 
 * @author Govinda Reddy
 *
 */

@Repository
public interface DataKeysRepository extends Neo4jRepository<DataKeys,Long> {

	 	 DataKeys findByName(String name, @Depth int depth);

	     @Query("MATCH (dk:DataKeys) where dk.name IN $names   WITH dk MATCH p=(dk)-[*0..1]-(m) RETURN p")
	   	Iterable<DataKeys>  getDataKeysForNames( List<String> names);
	
}
