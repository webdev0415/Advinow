/**
 * 
 */
package com.advinow.mica.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.SymptomSourceInfo;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.domain.queryresult.IllnessSourcesResultEnitity;

/**
 * @author Developer
 *
 */
@Repository
public interface OtherSourcesIllnessRepository extends Neo4jRepository<Illness,Long> {
	
	@Depth(value = 0)
	Page<Illness> findBySource(String source, Pageable pageable);
	
	@Query("match(illness:Illness) where illness.source=$source return count(illness.icd10Code)")
	Integer getIllnessCountBySource(String source);
	
	
	@Depth(value = 0)
	@Query(value =" MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = $status "  +
     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
    " WITH icd10Code,  MAX(version_list) AS version  " +
    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return DISTINCT  illness ",
    countQuery= " MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   COUNT(DISTINCT illness) ") 
	Page<Illness> findBySourceAndStatus(String source,String status,Pageable pageable);
	
	@Query(" MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   COUNT(DISTINCT illness) ") 
	Integer getIllnessCountBySourceAndStatus(String source,String status);
		
	@Depth(value = 0)
	@Query(value=" MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status  AND ill.name contains($name) "  +
     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
    " WITH icd10Code,  MAX(version_list) AS version  " +
    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return DISTINCT  illness ",
    countQuery= " MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status  AND ill.name contains($name) "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness) ") 
    Page<Illness>  findBySourceAndIcd10CodeAndNameAndStatus(String source,String status,String icd10Code,String name, Pageable pageable);
	 
	@Query(" MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status  AND ill.name contains($name) "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness) ") 
	Integer getIllnessCountBySourceAndIcd10CodeAndNameAndStatus(String source,String status,String icd10Code,String name);
	
	@Depth(value = 0)
	@Query(value= " MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status "  +
     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
    " WITH icd10Code,  MAX(version_list) AS version  " +
    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return DISTINCT  illness ",
    countQuery= " MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness)") 
    Page<Illness>  findBySourceAndIcd10CodeAndStatus(String source,String status,String icd10Code, Pageable pageable);
	
	@Query(" MATCH (ill:Illness) WHERE toUpper(ill.icd10Code) contains($icd10Code) AND ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness)") 
	Integer	getIllnessCountBySourceAndIcd10CodeAndStatus(String source,String status,String icd10Code);
	
	@Depth(value = 0)
	@Query(value=" MATCH (ill:Illness) WHERE toUpper(ill.name) contains($name) AND ill.source=$source AND ill.state = $status "  +
     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
    " WITH icd10Code,  MAX(version_list) AS version  " +
    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return DISTINCT  illness ",
    countQuery= " MATCH (ill:Illness) WHERE toUpper(ill.name) contains($name) AND ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness) ") 
    Page<Illness>  findBySourceAndNameAndStatus(String source,String status,String name, Pageable pageable);
	
	@Query(" MATCH (ill:Illness) WHERE toUpper(ill.name) contains($name) AND ill.source=$source AND ill.state = $status "  +
		     " WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list  " +
		    " WITH icd10Code,  MAX(version_list) AS version  " +
		    " MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state:$status})  return   count(DISTINCT illness) ") 
	Integer	getIllnessCountBySourceAndNameAndStatus(String source,String status,String name);
	
	
	@Query("match(illness:Illness) where illness.source=$source and illness.state=$status return count(DISTINCT  illness)")
	Integer getTotalIllnessBySourceAndStatus(String source,String status);

	@Query("MATCH(user:User)-[ur]-(illness:Illness)  where user.userID =$userID AND  illness.source=$source  WITH illness    MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	Iterable<Illness> getIllnessDataByUserIDAndSource(Integer userID,String source);
	
	@Query("match(illness:Illness) where illness.state = $state  AND illness.icd10Code = $icd10Code  AND illness.source=$source AND illness.version=$version  return illness")
	Illness findIllnessByStatusAndICD10CodeAndVersionAndSource(String state,String icd10Code,Integer version,String source);
	
	/*@Query("match(illness:Illness) WHERE illness.source={2} AND illness.icd10Code={0} AND illness.version={1}   RETURN id(illness)")
	List<Long> findIllnessByUserAndICD10Code(String icd10Code,Integer verstion,String source);*/
	
	@Query("match(illness:Illness) WHERE illness.source=$source AND illness.icd10Code=$icd10Code AND illness.version=$version  RETURN RETURN id(illness)")
	List<Long> findIllnessByICD10Code(String icd10Code,Integer version,String source);

//	@Query("MATCH(illness:Illness) where toUpper(illness.icd10Code) CONTAINS({0})  AND  illness.source={2} AND  illness.state IN {1} AND  illness.version = 1  WITH  illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	@Query("MATCH(ill:Illness) where toUpper(ill.icd10Code) CONTAINS($icd10Code)  AND  ill.source=$source AND ill.state = $state  with   ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:$source,state:$state , version:version,icd10Code:icd10Code}) with  illness  MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	Iterable<Illness> getIllnessDataByIcd10CodeAndState(String icd10Code,String  state,String source);


//	@Query("MATCH(illness:Illness) where toUpper(illness.icd10Code) CONTAINS({0})  AND  illness.source={1} AND  illness.version = 1 WITH  illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	@Query("MATCH(ill:Illness) where toUpper(ill.icd10Code) CONTAINS($icd10Code)  AND  ill.source=$source  with   ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:$source , version:version,icd10Code:icd10Code}) with  illness  MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	
	Iterable<Illness> getIllnessDataByIcd10CodeAndNoState(String icd10Code,String source);

	/*@Query("match(illness:Illness{icd10Code:{0},version:1,source:{2},state:{1}})-[ic]-(c:Category)-[cs]-(s:Symptom) with s   MATCH p= (s:Symptom)-[ss]-(ds:SymptomDataStore)  return p")
	List<Symptom> getSymptomsWithSourceAndState(String icd10Code, String state, String source);
*/	
	
	
	@Query("MATCH (ill:Illness{icd10Code:$icd10Code,source:$source,state:$state})  WITH ill.icd10Code AS icd10Code, ill.state as state,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, state,source   match(illness:Illness{icd10Code:icd10Code,version:version,source:source,state:state})-[ic]-(c:Category)-[cs]-(s:Symptom) with s   MATCH p= (s)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources)   return p")
	List<Symptom> getSymptomsWithSourceAndState(String icd10Code, String state, String source);
	
	@Query("MATCH (ill:Illness{source:$source1,state:$state1})    WITH ill.icd10Code AS icd10Code, ill.state as state,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, state,source   match(illness:Illness{icd10Code:icd10Code,version:version,source:source,state:state})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources) where illness.icd10Code IN $icdcodes     MATCH (st:SymptomTemplate) where st.code = s.code   return distinct  st.code as symptomID, st.name as symptomName ")
	 List<GenericQueryResultEntity>  getAllSymptoms(Set<String> icdcodes,String state1, String source1);
	
	
	/*@Query("match(illness:Illness{icd10Code:{0},version:1,source:{1}})-[ic]-(c:Category)-[cs]-(s:Symptom) with s   MATCH p= (s:Symptom)-[ss]-(ds:SymptomDataStore)  return p  ")
	List<Symptom> getSymptomsWithSource(String icd10Code, String source);*/

	
	/*@Query("MATCH (ill:Illness{icd10Code:{0},source:{1}})  WITH ill.icd10Code AS icd10Code,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, source match(illness:Illness{icd10Code:icd10Code,version:version,source:source})-[ic]-(c:Category)-[cs]-(s:Symptom) with s   MATCH p= (s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources)   return p")
	List<Symptom> getSymptomsWithSource(String icd10Code, String source);*/
	
	/*
	@Query("MATCH (ill:Illness{source:{1}})  where ill.icd10Code  IN {0}   WITH ill.icd10Code AS icd10Code,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, source match(illness:Illness{icd10Code:icd10Code,version:version,source:source})-[ic]-(c:Category)-[cs]-(s:Symptom) with s,illness   MATCH symptoms= (s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources)   return symptoms,illness.icd10Code as icd10Code")
	Iterable<IllnessSourcesResultEnitity> getSymptomsWithIllnessSource(List<String> icd10Code, String source);
	
*/	
	@Query("MATCH (ill:Illness{source:$source,state:$state})   WITH ill.icd10Code AS icd10Code, ill.state as state,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, state,source   match(illness:Illness{icd10Code:icd10Code,version:version,source:source,state:state})-[ic]-(c:Category)-[cs]-(s:Symptom)  where illness.icd10Code IN $icd10Code   with s,illness   MATCH symptoms= (s)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources)   return symptoms,illness.icd10Code as icd10Code")
	Collection<Map<String, Object>>  getSymptomsWithIllnessSourceAndState(List<String> icd10Code, String state, String source);

	
	

/*	@Query("match(src:SymptomSourceInfo) where src.sourceID in {0} return src ")
	List<SymptomSourceInfo> getSymptomSourceInfo(Set<Integer> source );
	*/
	
	// new 

	
	@Query("match(src:SymptomSourceInfo) where src.sourceID in $source return src ")
	List<SymptomSourceInfo> getSymptomSourceInfo(List<Integer> source );
	
	@Query("MATCH (ill:Illness{source:$source,state:$state})   WITH ill.icd10Code AS icd10Code, ill.state as state,ill.source as source, COLLECT(ill.version) AS versions UNWIND versions AS version_list WITH icd10Code,  MAX(version_list) AS version, state,source   match(illness:Illness{icd10Code:icd10Code,version:version,source:source,state:state})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources)  where illness.icd10Code IN $icd10Code      match(src:SymptomSourceInfo) where src.sourceID = dss.sourceID return src  ")
	List<SymptomSourceInfo> getSymptomMasterSourceInfo(Set<String> icd10Code,String state, String source);
	
	 /*@Query("MATCH (st:SymptomTemplate) where st.code IN {0}   return  st.code as symptomID, st.name as symptomName " )
	 List<GenericQueryResultEntity> getSymptomNames(List<String> codes); */
	
}
