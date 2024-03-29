package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.LinkEnd;

/**
 * 
 * @author Govinda Reddy
 *
 */

@Repository
public interface LinkEndRepository extends Neo4jRepository<LinkEnd,Long> {

	
}
