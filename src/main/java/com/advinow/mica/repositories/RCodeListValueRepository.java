package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.RCodeDataStore;

@Repository
public interface RCodeListValueRepository extends Neo4jRepository<RCodeDataStore,Long> {

}

