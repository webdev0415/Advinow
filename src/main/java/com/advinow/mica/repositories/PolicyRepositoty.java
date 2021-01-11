package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Policy;

@Repository
public interface PolicyRepositoty extends Neo4jRepository<Policy,Long> {
	
   
		
}
