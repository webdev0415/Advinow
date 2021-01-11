package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.LinkEnd;
import com.advinow.mica.domain.LinkStart;

/**
 * 
 * @author Govinda Reddy
 *
 */

@Repository
public interface LinkStartRepository extends Neo4jRepository<LinkStart,Long> {
	
	
}
