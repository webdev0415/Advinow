package com.advinow.mica.repositories;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Section;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SectionRepository extends Neo4jRepository<Section,Long> {
		/*    @Query("MATCH(sec:Section)-[sg]-(sc:SymptomCategory) where id(sec)={0} return sc")
		    Set<SymptomCategory>  getSymptomCategoryFromIDs(Long id);*/
		    
		    @Query("MATCH(se:Section) MATCH p= (se)-[src]-(sc:SymptomCategory)   return p")
		    Set<Section>  getPainSwellingSection();
	
}
