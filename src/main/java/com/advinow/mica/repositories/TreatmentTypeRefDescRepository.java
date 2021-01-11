package com.advinow.mica.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.TreatmentTypeRefDesc;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface TreatmentTypeRefDescRepository extends Neo4jRepository<TreatmentTypeRefDesc,Long> {

	@Query("Match(tt:TreatmentTypeRefDesc) return max(tt.typeDescID)")
	Integer getMaxTypeDescID();

}
