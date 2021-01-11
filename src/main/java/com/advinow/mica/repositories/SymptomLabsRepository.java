package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomLabOrders;

@Repository
public interface SymptomLabsRepository extends Neo4jRepository<SymptomLabOrders,String> {

}
