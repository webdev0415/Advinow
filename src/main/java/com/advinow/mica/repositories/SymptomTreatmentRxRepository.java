package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.advinow.mica.domain.SymptomTreatment;

/**
 *
 * @author Govinda Reddy
 *
 */
public interface SymptomTreatmentRxRepository  extends Neo4jRepository<SymptomTreatment,Long> {

	@Query("MATCH(symptomTreatment:SymptomTreatment) WHERE toUpper(symptomTreatment.code) = $icd10Code  return symptomTreatment")
	SymptomTreatment findTreatmentBySymptomID(String icd10Code);

	@Query("MATCH (symptomTreatment:SymptomTreatment)  WITH symptomTreatment MATCH p=(symptomTreatment)-[*0..7]-(m) RETURN p")
	Iterable<SymptomTreatment> getSymptomTreatments();

	@Query("MATCH(symptomTreatment:SymptomTreatment)  where toUpper(symptomTreatment.code)= $symptomID   WITH symptomTreatment MATCH p=(symptomTreatment)-[*0..4]-(m) RETURN p")
	List<SymptomTreatment> findTreatmentsBySymptomID(String symptomID);

	
	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code  optional match(drug)-[src]-(ts:DrugDosage)    DETACH DELETE  ts")
	 void deleteIllnessTmtDrugDosage(String code);
	
	 @Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code  optional match(drug)-[src]-(ts:TreatmentPolicy)-[sst]-(atl:AlternativeDrugRx)-[dsr]-(dd:DrugDosage)    DETACH DELETE  ts,atl,dd")
	 void deleteIllnessTmtDrugPolicies(String code);
	 
	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE drug")
	void deleteSymptomTmtDrugsNonDrugs(String code);
	
	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code  optional match(drug)-[src]-(ts:TreatmentSources)   DETACH DELETE  ts")
	void deleteSymptomTmtDrugSources(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE  treatmentGroup")
	void deleteIllnessTreamentsGroups(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment) where toUpper(symptomTreatment.code)= $code   DETACH DELETE  treatment")
	void deleteIllnessTreatmentsTmt(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE  symptomTreatment")
	void deleteSymptomTreatment(String code);
	
	

}
