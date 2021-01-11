package com.advinow.mica.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Category;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface CategoryRepository extends Neo4jRepository<Category,Long> {

}
