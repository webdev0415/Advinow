package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.IllnessTreatment;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface IllnessTreatmentRxRespository extends Neo4jRepository<IllnessTreatment,Long> {
	
	@Query("match(illness:IllnessTreatment) where toUpper(illness.icd10Code) = $icd10Code   return illness")
	IllnessTreatment findIllnessByCode(String icd10Code);
	
	@Query("MATCH (illnessTreatment:IllnessTreatment)  WITH illnessTreatment MATCH p=(illnessTreatment)-[*0..7]-(m) RETURN p")
	Iterable<IllnessTreatment> getIllnessTreatments();
	
    @Query("MATCH (illnessTreatment:IllnessTreatment)  where toUpper(illnessTreatment.icd10Code) = $icd10Code   WITH illnessTreatment MATCH p=(illnessTreatment)-[*0..6]-(m) RETURN p")
    List<IllnessTreatment> findTreatmentsByIcd10Code(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment) where toUpper(illness.icd10Code) = $icd10Code  DETACH DELETE  illness")
    void deleteIllnessTreatment(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment) where toUpper(illness.icd10Code) = $icd10Code  DETACH DELETE  treatment")
    void deleteIllnessTreatmentsTmt(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup) where toUpper(illness.icd10Code) = $icd10Code  DETACH DELETE  illness,treatment,treatmentGroup")
    void deleteIllnessTreamentsGroups(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug) where toUpper(illness.icd10Code) = $icd10Code  DETACH DELETE  drug")
    void deleteIllnessTmtdrugsNonDrugs(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drg]-(drug) where toUpper(illness.icd10Code) = $icd10Code optional match(drug)-[src]-(ts:TreatmentSources)    DETACH DELETE  ts")
    void deleteIllnessTmtDrugSources(String icd10Code);
    
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drg]-(drug) where toUpper(illness.icd10Code) = $icd10Code optional match(drug)-[src]-(ts:DrugDosage)    DETACH DELETE  ts")
    void deleteIllnessTmtDrugDosage(String icd10Code);
    
  /*  @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drg]-(drug) where toUpper(illness.icd10Code) = {0} optional match(drug)-[src]-(ts:rxNorms)    DETACH DELETE  ts")
    void deleteIllnessTmtDrugRxnorms(String icd10Code);
  */  
    @Query("MATCH(illness:IllnessTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drg]-(drug) where toUpper(illness.icd10Code) = $icd10Code optional match(drug)-[src]-(ts:TreatmentPolicy)-[sst]-(atl:AlternativeDrugRx)-[dsr]-(dd:DrugDosage)    DETACH DELETE  ts,atl,dd")
    void deleteIllnessTmtDrugPolicies(String icd10Code);
    
    
    
}
