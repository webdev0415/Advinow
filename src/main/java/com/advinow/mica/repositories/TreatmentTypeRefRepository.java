package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.TreatmentTypeRef;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface TreatmentTypeRefRepository extends Neo4jRepository<TreatmentTypeRef,Long> {

	@Query("MATCH (treatmentType:TreatmentTypeRef) where toUpper(treatmentType.name)= $name  WITH treatmentType OPTIONAL MATCH p=(treatmentType)-[tr]-(treatmentTypeDesc:TreatmentTypeRefDesc) RETURN p")
	TreatmentTypeRef getTreatmentTypeByName(String name);
	
	@Query("MATCH (treatmentType:TreatmentTypeRef{type:$type})  WITH treatmentType OPTIONAL MATCH p=(treatmentType)-[tr]-(treatmentTypeDesc:TreatmentTypeRefDesc) RETURN p")
	TreatmentTypeRef getTreatmentByType(String type);

	@Query("MATCH (treatmentType:TreatmentTypeRef{typeID:$typeID})  WITH treatmentType OPTIONAL MATCH p=(treatmentType)-[tr]-(treatmentTypeDesc:TreatmentTypeRefDesc) RETURN p")
	TreatmentTypeRef getTreatmentByTypeID(Integer typeID);

	@Query("Match(treatmentType:TreatmentTypeRef) return max(treatmentType.typeID)")
	Integer getMaxTypeID();

	/*@Query("MATCH (treatmentType:TreatmentTypeRef{typeID:{0}}) RETURN  treatmentType")
	TreatmentTypeRef findTreatmentTypeID(Integer typeID);
	
	@Query("MATCH (treatmentType:TreatmentTypeRef{typeID:{0}})-[tr]-(treatmentTypeDesc:TreatmentTypeRefDesc{defaultValue:true}) RETURN  treatmentTypeDesc.typeDescID")
	List<Integer> findDefatultTreatmentTypeDescID(Integer typeID);*/
	
	@Query("MATCH (treatmentType:TreatmentTypeRef)  WITH treatmentType OPTIONAL MATCH p=(treatmentType)-[tr]-(treatmentTypeDesc:TreatmentTypeRefDesc) RETURN p")
	List<TreatmentTypeRef> getAllTreatmentsWithDesc();
	

}
