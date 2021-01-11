package com.advinow.mica.repositories;


import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Symptom;


/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomRepository extends Neo4jRepository<Symptom,Long>{
	    @Query("match(s:Symptom{code:$code}) return s")
	     Set<Symptom> findByCode(String code);
	     @Query("MATCH (ill:MITA_Illness) where ill.icd10Code=$icd10Code  return ill.icd10Code as rCode")
	     String getMasterIcd10Code(String icd10Code); 
}
