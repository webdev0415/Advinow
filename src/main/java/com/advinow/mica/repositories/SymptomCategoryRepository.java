package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomCategory;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomCategoryRepository extends	Neo4jRepository<SymptomCategory,Long> {

	SymptomCategory findByCode(String code, @Depth int depth);
	

    @Query("MATCH (symptomGroup:SymptomGroup{code:'SG004'})-[src]-(sg: SymptomCategory)  WHERE sg.parent is NULL   return sg")
    SymptomCategory findByRootBodyPart();
    
    
    @Query("MATCH (symptomGroup:SymptomGroup{code:'SG005'})-[se]-(s:Section)-[src]-(sg: SymptomCategory)  WHERE sg.parent is NULL return s.name as groupName, sg as sCategory")
    List<GenericQueryResultEntity> getPainSwellingBodyParts();
 
    @Query("MATCH (sg: SymptomCategory{parent:$parent})   return sg")
     List<SymptomCategory> findBySubBodyPart(String parent);
 		
    
    @Query("MATCH (symptomGroup:SymptomGroup{code:'SG004'})-[src]-(sg: SymptomCategory)  return sg")
    List<SymptomCategory> findAllBodyParts();
    
   @Query("MATCH (symptomGroup:SymptomGroup{code:'SG005'})-[se]-(s:Section)-[src]-(sg: SymptomCategory)  WHERE sg.parent is NULL return s.name as groupName, sg as sCategory, s.code as sectionID")
    List<GenericQueryResultEntity> getAllPainSwellingCategories();
   
   @Query("MATCH (s:Section)-[src]-(sg: SymptomCategory)    WHERE s.code in $sectionCode    return sg")
   List<SymptomCategory> findPainSwellingBodyParts(List<String> sectionCode);
}
