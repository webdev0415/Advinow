package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.LogicalSymptomGroupsRef;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;

@Repository
public interface LogicalSymptomGroupsRefRepositoty extends Neo4jRepository<LogicalSymptomGroupsRef,Long> {
	
    @Query("MATCH (sc:LogicalSymptomGroupsRef) WHERE sc.type IN ['ROS','LOGICAL']  return sc")
    List<LogicalSymptomGroupsRef> findROSGroups();
    
	@Query("Match(sc:LogicalSymptomGroupsRef) return max(sc.groupID)")
	Integer getMaxGroupID();
		
	@Query("match(sc:LogicalSymptomGroupsRef) where toUpper(sc.name) contains($groupName) return sc")
	List<LogicalSymptomGroupsRef> searchGroup(String groupName);
		
	@Query("match(sc:LogicalSymptomGroupsRef) where toUpper(sc.name)=$groupName return sc")
	LogicalSymptomGroupsRef findByName(String groupName);

	@Query("match(sc:LogicalSymptomGroupsRef) where  sc.groupID = $groupID  detach delete sc")
	void delete(Integer groupID);
	

	@Query("match(sc:LogicalSymptomGroupsRef) where  sc.groupID = $groupID  return sc")
	LogicalSymptomGroupsRef findByGroupID(Integer groupID);
	
	@Query("match(sc:LogicalSymptomGroupsRef) return  collect({logicalGroupID:sc.groupID,name:sc.name}) as literalMap")
	GenericQueryResultEntity  getLogicalSymptomGroups();

	
	@Query("match(sc:LogicalSymptomGroupsRef) return  sc")
	List<LogicalSymptomGroupsRef> getLogicalSystemGroups();

	//LogicalSymptomGroupsRef findByGroupID(Integer groupID);
}
