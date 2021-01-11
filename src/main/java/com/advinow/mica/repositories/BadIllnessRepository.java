package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.BadIllness;

@Repository
public interface BadIllnessRepository extends Neo4jRepository <BadIllness, Long> {

}
