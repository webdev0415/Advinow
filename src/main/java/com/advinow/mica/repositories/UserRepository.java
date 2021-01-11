package com.advinow.mica.repositories;


import java.util.Set;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.User;


/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface UserRepository extends Neo4jRepository<User,Long>{

	User findByUserID(Integer userID, @Depth int depth);
	
	@Query("MATCH (user:User) WHERE NOT ((user)--()) DELETE user")
	void deleteUser();
	
	@Query("match(user:User)-[ur]-(illness:Illness) WHERE illness.source=$source and illness.icd10Code=$icd10Code and illness.version=$version RETURN user.userID")
	Integer getUserID(String source,String icd10Code,Integer version);		
	
	@Query("MATCH(user:User)  with user   MATCH p= (user)-[uhi]-(illness:Illness{source:$source})   return p")
	Set<User>  findIllnessInfoBysource(String source);
	
	@Query("MATCH(user:User{userID:$userID})  with user   MATCH p= (user)-[uhi]-(illness:Illness{source:$source})   return p")
	Set<User>  findIllnessInfoByUserAndSource(Integer userID,String source);
}
