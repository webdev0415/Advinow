package com.advinow.mica.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomGroup;
/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomGroupRepository extends Neo4jRepository<SymptomGroup,Long> {

	SymptomGroup findByName(String name, @Depth int depth);

	SymptomGroup findByGroupID(String groupID, @Depth int depth);

	@Query("MATCH (smptg:SymptomGroup) WHERE smptg.groupID = $code  WITH smptg  MATCH p=(smptg)-[scr]-(SymptomCategory)-[stm]-(SymptomTemplate)   RETURN p;")
	SymptomGroup  findByGroupCode(String code);

	@Query("MATCH (smptg:SymptomGroup) WHERE smptg.groupID = $code WITH  smptg   MATCH p=(smptg)-[se]-(Section)-[scr]-(SymptomCategory)-[stm]-(SymptomTemplate) RETURN p;")
	SymptomGroup  findByGroupPainCode(String code);

	@Query("MATCH (smptg:SymptomGroup)-[scr]-(sc:SymptomCategory)-[stm]-(st:SymptomTemplate{code:$code}) set smptg.updatedDate = timestamp()   RETURN smptg")
	SymptomGroup  updateGroupTime(String code);

	@Query("MATCH (smptg:SymptomGroup)-[se]-(Section)-[scr]-(sc:SymptomCategory)-[stm]-(st:SymptomTemplate{code:$code}) set smptg.updatedDate = timestamp()   RETURN smptg")
	SymptomGroup  updateGroupPainSwellingTime(String code);

	@Query("MATCH (smptg:SymptomGroup) return smptg")
	Set<SymptomGroup> findGroups();

/*	@Query("MATCH (smptg:SymptomGroup) WHERE smptg.groupID = {0}  WITH smptg  MATCH p=(smptg)-[scr]-(SymptomCategory)-[stm]-(SymptomTemplate{code:'SYMPT0004345'})   RETURN p;")
	SymptomGroup findGroupByNLP(String groupID);*/
	
	@Query("MATCH(sc:SymptomGroup)  -[*1..5]->(st:SymptomTemplate) where st.code IN $symptoms  set sc.updatedDate = timestamp()  ")
	void updateSymptomGroupTime(List<String> symptoms);

}
