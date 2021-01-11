/**
 * 
 */
package com.advinow.mica.repositories;

import java.util.List;
import java.util.Set;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Coding_Rules;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.Symptom;
import com.advinow.mica.domain.queryresult.IllnessDataQueryResultEnitity;

/**
 * @author Govinda Reddy
 *
 */
@Repository
public interface IllnessRepository extends Neo4jRepository<Illness,Long> {

	@Query("MATCH(illness:Illness) where illness.icd10Code=$icd10Code AND illness.source='MICA' AND illness.version=$verstion  return illness;")
	Set<Illness>  findIllnessByCode(String icd10Code,Integer verstion);

	@Query("match(illness:Illness) where illness.source='MICA' AND illness.icd10Code=$icd10Code  AND illness.version=$version    RETURN illness")
	List<Illness> findIllnessIdByICD10CodeAndVersion(String icd10Code,Integer version);
	
	@Query("match(illness:Illness) where illness.source='MICA' AND illness.icd10Code=$icd10Code AND illness.version=$version set illness.state = $state,illness.dfstatus = $state")
	void updateIllnessIdByICD10CodeAndVersion(String icd10Code,Integer version, String state);

	@Query("match(illness:Illness)-[ILLNESS_HAS_CATEGORY]-(c:Category)-[r]-(s:Symptom) where s.code IN $symptomIDs  AND illness.source='MICA'  return illness")
	Iterable<Illness> findIllnessBySymptomsID(List<String> symptomIDs);

	@Query("match(illness:Illness) where illness.state = $state AND illness.icd10Code = $icd10Code AND illness.source='MICA'   return illness")
	Iterable<Illness> findByIllnessStatusAndICD10Code(String state,	String icd10Code);

	@Query("match(illness:Illness) where illness.state IN $states AND illness.icd10Code = $icd10Code AND illness.source='MICA' AND illness.version=$verstion  return illness")
	Iterable<Illness> findIllnessByStatusAndICD10CodeAndVersion(List<String> states,String icd10Code,Integer verstion);
	
	
/*	@Query("match(illness:Illness) where illness.icd10Code = {0} AND illness.source='MICA' AND illness.version={1}  return illness")
	Iterable<Illness> findIllnessByICD10CodeAndVersion(String icd10Code,Integer verstion);
*/

	@Query("match(user:User)-[r]-(illness:Illness) where illness.state = $state  AND illness.icd10Code = $icd10Code AND illness.source='MICA'  DELETE r")
	void deleteIllness(String state, String icd10Code);

	@Query("match(user:User)-[r]-(illness:Illness) where illness.state IN $state AND illness.icd10Code = $icd10Code AND illness.source='MICA'  AND illness.version=$version   DELETE r")
	void deleteIllnessRelation(List<String> state, String icd10Code,Integer version);

	@Query("MATCH (illness:Illness) WHERE ID(illness) IN $ID AND illness.source='MICA'   WITH illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	Iterable<Illness>  getIllnessFromIDs(List<Long> ID);

	@Query("MATCH(illness:Illness) WHERE illness.source='MICA' AND illness.version=1 return illness")
	Iterable<Illness> getIllnesses();

	@Query("MATCH(illness:Illness)  WHERE illness.icd10Code IN $icdCodes AND illness.source='MICA' AND  illness.version=1 AND illness.state='APPROVED'  RETURN  illness")
	Iterable<Illness> getIllnessesForGivenICDcodes(List<String> icdCodes);

	@Query("MATCH(illness:Illness) where toUpper(illness.icd10Code) CONTAINS($icd10Code)  AND  illness.source='MICA' AND  illness.state IN $state WITH  illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	Iterable<Illness> getIllnessDataByIcd10CodeAndState(String icd10Code, List<String>  state);

	@Query("MATCH(user:User)-[ur]-(illness:Illness)  where user.userID =$userID AND  illness.source='MICA' AND illness.state IN $state  WITH illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	List<Illness> getIllnessDataByUserIcd10CodeAndState(Integer userID,List<String>  state);

	@Query("match(illness:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsm]-(dm:DataStoreModifier)  where  id(illness) IN $id     detach delete dm")
	void deleteIllnessDataModifier(List<Long> id);
	
	@Query("match(illness:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsr]-(dss:DataStoreSources)  where  id(illness) IN $id     detach delete dss")
	void deleteIllnesDataStoreSources(List<Long> id);

	@Query("match(illness:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  where  id(illness) IN $id  detach delete illness,c,s,ds")
	void deleteIllnessToDataStore(List<Long> id);

	@Query("match(illness:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom)  where  id(illness) IN $id   detach delete illness,c,s")
	void deleteIllnessToSymptom(List<Long> id);

	@Query("match(illness:Illness)-[ic]-(c:Category)  where  id(illness) IN $id   detach delete illness,c")
	void deleteIllnessToCategory(List<Long> id);

	@Query("match(illness:Illness)  where  id(illness) IN $id   detach delete illness")
	void deleteIllness(List<Long> id);

	@Query("match(u:User)-[:ILLNESS_HAS_AUTHOR]-(illness:Illness) where illness.state IN $state AND illness.icd10Code = $icd10Code AND illness.source='MICA' AND illness.version=$version  return u.userID")
	List<Integer> findUserByStatusAndICD10CodeAndVersion(List<String> state,String icd10Code,Integer version);

	// Start Queries for illness merge 
	@Query("MATCH(illness:Illness) where toUpper(illness.icd10Code)= $icd10Code AND illness.source='MICA' AND illness.state='FINAL'  WITH  illness   MATCH p=(illness)-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[*0..2]-(m)  return p")
	Illness findByIcd10Code(String icd10Code);

	// Start Queries for illness merge 
	@Query("MATCH(illness:Illness{icd10Code:$icd10Code})-[ic]-(c:Category) WHERE illness.source='MICA' AND illness.version=$version  RETURN DISTINCT c.code")
	Set<String> getCategoryCodesForIllness(String icd10Code,Integer version);

	@Query("MATCH(illness:Illness{icd10Code:$icd10Code,source:'MICA'})-[ic]-(c:Category{code:$categoryCode})-[scr]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with s,ds  optional match (ds)-[drs]-(dm:DataStoreModifier) return  s")
	List<Symptom> getRelatedSymptomData(String icd10Code,String categoryCode);
	//End  Queries for illness merge 

	@Query("MATCH(illness:Illness{icd10Code:$icd10Code,source:'MICA'})-[ic]-(c:Category{code:$categoryCode})-[scr]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with s,ds  optional match (ds)-[drs]-(dm:DataStoreModifier) return  s")
	List<Symptom> getRelatedSymptomModifiers(String icd10Code,String categoryCode);
	//End  Queries for illness merge 

	// Query for reset test data
	@Query("MATCH(user:User{userID:$userID})-[usr]-(illness:Illness{source:'MICA'}) RETURN id(illness)")
	List<Long> getAllTestIllnessIDs(Integer userID);	
	
	@Query("MATCH(user:User{userID:$userID})-[usr]-(illness:Illness{source:'MICA'}) SET illness.state='READY-FOR-REVIEW' RETURN id(illness)")
	List<Long> updateIllness(Integer userID);	
	
	@Query("MATCH(user:User)-[ur]-(illness:Illness)  where user.userID =$userID AND  illness.source='MICA' AND illness.state = $state  return  illness")
	List<Illness>  findByicd10CodeByUserAndStatus(Integer userID,String state);
	
	@Query("MATCH(user:User)-[usr]-(ill:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom) where ill.state = 'APPROVED' AND  ill.source='MICA'  with ill.icd10Code as icd10Code, collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  " + 
            "MATCH(illness:Illness{icd10Code: icd10Code,source:'MICA',state:'APPROVED',version:version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)    with illness,c,s,ds " +
            "OPTIONAL MATCH(ds)-[dsm]-(dm:DataStoreModifier) RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,s.bodyParts AS bodyParts,ds.likelihood AS dsLikelihood ,ds.multiplier AS multiplier, dm.timeFrame AS timeFrame, dm.likelihood AS dmLikelihood") 
	List<IllnessDataQueryResultEnitity> getUniqueApprovedIllnesses();
	
	@Query("MATCH(illness:Illness{icd10Code:$icd10Code,source:'MICA',state:'READY-FOR-REVIEW',version:$version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)    with illness,c,s,ds " +
            "OPTIONAL MATCH(ds)-[dsm]-(dm:DataStoreModifier) RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,s.bodyParts AS bodyParts, ds.likelihood AS dsLikelihood ,ds.multiplier AS multiplier, dm.timeFrame AS timeFrame, dm.likelihood AS dmLikelihood") 
	List<IllnessDataQueryResultEnitity> getIllnessDataForGivenIcd10(String icd10Code, Integer version);
     
	
	
	@Depth(value = 0)
	@Query(value = "MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) WHERE toUpper(s.code)  CONTAINS($symptomID)  return DISTINCT illness",
	countQuery="MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) WHERE toUpper(s.code)  CONTAINS($symptomID)  return count(DISTINCT illness)")
	Page<Illness> findApprovedIllnessBySymptomCode(String source,String symptomID, Pageable pageable);
	
	
	
	@Depth(value = 0)
	@Query("MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) WHERE toUpper(s.code)  CONTAINS($symptomID)  return count(DISTINCT illness)")
	Integer findApprovedIllnessBySymptomCodeCount(String source,String symptomID);
	
	
	
	@Depth(value = 0)
	@Query(value = "MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) WHERE s.code IN $symptoms   return DISTINCT illness" ,
	countQuery="MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  WHERE s.code IN $symptoms   return count(DISTINCT illness)")
	Page<Illness> findApprovedIllnessBySymptomName(String source,List<String> symptoms, Pageable pageable);
	
	
	
	@Depth(value = 0)
	@Query("MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  WHERE s.code IN $symptoms   return count(DISTINCT illness)")
	Integer findApprovedIllnessBySymptomNameCount(String source,List<String> symptoms);

	
	@Depth(value = 0)
	@Query(value = "MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)   return DISTINCT illness",
			countQuery="MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:$source,state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  return count(DISTINCT illness)")
	Page<Illness> findApprovedIllnessBySource(String source, Pageable pageable);
	
	@Depth(value = 0)
	@Query("MATCH (ill:Illness) WHERE  ill.source=$source AND ill.state = 'APPROVED'   WITH ill.icd10Code AS icd10Code, COLLECT(ill.version) AS versions UNWIND versions AS version_list   WITH icd10Code,  MAX(version_list) AS version   MATCH (illness:Illness{icd10Code:icd10Code,version:version,source:{0},state :'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  return count(DISTINCT illness)")
	Integer findApprovedIllnessBySourceCount(String source);
	
	
	@Query("MATCH(user:User)-[usr]-(ill:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom) where ill.state = 'APPROVED' AND  ill.source=$source  with ill.icd10Code as icd10Code, collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  " + 
            "MATCH(illness:Illness{icd10Code: icd10Code,source:$source,state:'APPROVED',version:version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)    with illness,c,s,ds " +
            "OPTIONAL MATCH(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code, illness.name as illnessName") 
	List<IllnessDataQueryResultEnitity> getApprovedIllnessesBySource(String Source);

	@Query("Match(cr:Coding_Rules) return cr")
	Iterable<Coding_Rules> getDiseaseCodingRules();
	
	@Query("MATCH(illness:Illness{icd10Code:toUpper($icd10Code),source:'MICA',state:'READY-FOR-REVIEW',version:$version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)    with illness,c,s,ds OPTIONAL MATCH(ds)-[dsm]-(dm:DataStoreModifier)  with collect (distinct s.code) as symptoms MATCH(st:SymptomTemplate) where st.code in symptoms return distinct collect(distinct  st.displaySymptom) as displaySymptoms") 
	IllnessDataQueryResultEnitity chiefcomplaintcheck(String icd10Code, Integer version);

	@Query("MATCH(ill:Illness) where ill.state = 'APPROVED' AND  ill.source='MICA' AND  ill.icd10Code IN  $icd10Codes   with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:'MICA',state:'APPROVED',version:version,icd10Code:icd10Code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  where  ds.minDiagCriteria = &flag    with illness,c,s,ds" +
    " RETURN DISTINCT illness.icd10Code AS icd10Code,illness.version as version,   collect(distinct s.code) AS symptoms,count(s.code) as symptomCount") 
	List<IllnessDataQueryResultEnitity> getMDCSymptomsByIllnesses(List<String> icd10Codes, Boolean flag  );
    
	@Query("MATCH(ill:Illness) where ill.state = 'APPROVED' AND  ill.source='MICA' AND  ill.icd10Code IN  $icd10Codes   with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:'MICA',state:'APPROVED',version:version,icd10Code:icd10Code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  where  ds.bias = $flag    with illness,c,s,ds" +
		    " RETURN DISTINCT illness.icd10Code AS icd10Code,illness.version as version,   collect(distinct s.code) AS symptoms,count(s.code) as symptomCount") 
			List<IllnessDataQueryResultEnitity> getNegativeBiasSymptomsByIllnesses(List<String> icd10Codes, Boolean flag );
		    
	
	
}
