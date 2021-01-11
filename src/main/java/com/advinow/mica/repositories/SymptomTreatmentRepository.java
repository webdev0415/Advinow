package com.advinow.mica.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.advinow.mica.domain.SymptomTreatment;

/**
 *
 * @author Govinda Reddy
 *
 */
public interface SymptomTreatmentRepository  extends Neo4jRepository<SymptomTreatment,Long> {

	@Query("MATCH(symptomTreatment:SymptomTreatment) WHERE toUpper(symptomTreatment.code) = $icd10Code  return symptomTreatment")
	SymptomTreatment findTreatmentBySymptomID(String icd10Code);

	@Query("MATCH (symptomTreatment:SymptomTreatment)  WITH symptomTreatment MATCH p=(symptomTreatment)-[*0..4]-(m) RETURN p")
	Iterable<SymptomTreatment> getSymptomTreatments();

	@Query("MATCH(symptomTreatment:SymptomTreatment)  where toUpper(symptomTreatment.code)= $symptomID   WITH symptomTreatment MATCH p=(symptomTreatment)-[*0..4]-(m) RETURN p")
	SymptomTreatment findTreatmentsBySymptomID(String symptomID);

	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE  symptomTreatment,treatment,treatmentGroup,drug")
	void deleteIllnessAllTreatments(String code);
	
	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)-[drugs]-(drug)  where toUpper(symptomTreatment.code)= $code  optional match(drug)-[src]-(ts:TreatmentSources)   DETACH DELETE  symptomTreatment,treatment,treatmentGroup,drug,ts")
	void deleteSymptomTmtDrugAndNDrugSources(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment)-[tr]->(treatmentGroup:TreatmentGroup)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE  symptomTreatment,treatment,treatmentGroup")
	void deleteIllnessTreamentsGroups(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)-[itr]->(treatment:Treatment) where toUpper(symptomTreatment.code)= $code   DETACH DELETE  symptomTreatment,treatment")
	void deleteIllnessTreatmentsTmt(String code);

	@Query("MATCH(symptomTreatment:SymptomTreatment)  where toUpper(symptomTreatment.code)= $code   DETACH DELETE  symptomTreatment")
	void deleteSymptomTreatment(String code);
	
	

}
