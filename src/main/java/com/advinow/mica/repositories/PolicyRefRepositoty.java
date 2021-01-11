package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.PolicyRef;

@Repository
public interface PolicyRefRepositoty extends Neo4jRepository<PolicyRef,Long> {
	
    @Query("MATCH (sc:PolicyRef) return sc")
    List<PolicyRef> findPolicies();
    
	@Query("Match(sc:PolicyRef) return max(sc.policyID)")
	Integer getMaxPolicyID();
		
	@Query("match(sc:PolicyRef) where toUpper(sc.name) contains($policyName) return sc")
	List<PolicyRef> searchPolicyName(String policyName);
		
	@Query("match(sc:PolicyRef) where toUpper(sc.name)={$policyName} return sc")
	PolicyRef findByName(String policyName);

	@Query("match(sc:PolicyRef) where  sc.policyID = $policyID  detach delete sc")
	void delete(Integer policyID);
	
	@Query("match(sc:PolicyRef) where  sc.policyID = $policyID  set sc.name =$name return sc ")
	PolicyRef updatePolicyName(Integer policyID, String name);
	
	@Query("match(sc:PolicyRef) where  sc.policyID = $policyID  return sc")
	PolicyRef findByPolicyID(Integer policyID);
		
}
