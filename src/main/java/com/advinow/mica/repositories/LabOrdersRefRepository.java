/**
 * 
 */
package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.advinow.mica.domain.LabOrdersRef;

/**
 * @author Developer
 *
 */
public interface LabOrdersRefRepository extends Neo4jRepository<LabOrdersRef,Long> {

	@Query("match(sc:LabOrdersRef) return  sc")
	List<LabOrdersRef> getAllLabOrders();

}
