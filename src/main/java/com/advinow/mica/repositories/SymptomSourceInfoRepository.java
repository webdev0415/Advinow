package com.advinow.mica.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomSourceInfo;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomSourceInfoRepository extends Neo4jRepository<SymptomSourceInfo,Long> {

	@Query("Match(s:SymptomSourceInfo) return max(s.sourceID)")
	Integer getMaxSourceID();
		
	@Query("match(src:SymptomSourceInfo) where toUpper(src.source) contains($source) return src")
	List<SymptomSourceInfo> searchSource(String source);
		
	SymptomSourceInfo findBySource(String source);
	
	@Query("Match(s:SymptomSourceInfo{sourceID:$sourceID}) detach delete s")
	void deleteBySourceID(Integer sourceID);

	/*@Query("match(illness:Illness{source:'MICA',state:{1},version:{2},icd10Code:{0}})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsr]-(scs:DataStoreSources) with collect(distinct scs.sourceID) as sources match(ssi:SymptomSourceInfo) where ssi.sourceID in sources return ssi")
	List<SymptomSourceInfo> getIllnessSymptomsSources(String icd10code,	String state, Integer version);
	*/

	@Query("match(illness:Illness{icd10Code:$icd10code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsr]-(scs:DataStoreSources) with collect(distinct scs.sourceID) as sources match(ssi:SymptomSourceInfo) where ssi.sourceID in sources return ssi")
	List<SymptomSourceInfo> getIllnessSymptomsSources(String icd10code,	String state, Integer version);
	
	
	@Query("match(user:User{userID:$userID})-[src]-(illness:Illness{source:'MICA'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsr]-(scs:DataStoreSources) where illness.state in $state  with collect(distinct scs.sourceID) as sources match(ssi:SymptomSourceInfo) where ssi.sourceID in sources return ssi")
	List<SymptomSourceInfo> getIllnessSymptomsSourcesForGivenUser(Integer userID, List<String> state);
	
	
	// self or symptom unmap
	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom{code:p.symptomID})-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID}) where ds.multiplier[0]= p.multiplier MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code, multiplier : ds.multiplier[0], sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete dss")
	void unMapSymptomSourcesWithMultipliers(Collection<Map<String, Object>> sources);
	
	@Query("WITH $sources AS pairs UNWIND pairs AS p   MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom{code:p.symptomID})-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID})  MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code,sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action   detach delete dss")
	void unMapSymptomSourcesWithNoMultipliers(Collection<Map<String, Object>> sources);
	
	
	//  unmap illness sources
	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID}) where ds.multiplier is not null MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code, multiplier : ds.multiplier[0], sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action detach delete dss")
	void unMapIllnessSourcesWithMultipliers(Collection<Map<String, Object>> sources);
	
	@Query("WITH $sources AS pairs UNWIND pairs AS p   MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID})  MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code,sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action detach delete dss")
	void unMapIllnessSourcesWithNoMultipliers(Collection<Map<String, Object>> sources);
		
	// unmap all sources
		
	@Query("WITH $sources AS pairs UNWIND pairs AS p   MATCH(illness:Illness{source:'MICA'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID}) where ds.multiplier is not null MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code, multiplier : ds.multiplier[0], sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action detach delete dss")
	void unMapAllSourcesWithMultipliers(Collection<Map<String, Object>> sources);
	
	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(illness:Illness{source:'MICA'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[sss]-(dss:DataStoreSources{sourceID:p.sourceID})  MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code,sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action detach delete dss")
	void unMapAllSourcesWithNoMultipliers(Collection<Map<String, Object>> sources);

	// add sources
	
	@Query("WITH $addSources AS pairs UNWIND pairs AS p  MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom{code:p.symptomID})-[ss]-(ds:SymptomDataStore) where ds.multiplier[0]= p.multiplier  merge(ds)-[:HAS_SOURCES]-(dss:DataStoreSources{sourceID:p.sourceID}) ON CREATE SET dss.sourceRefDate=timestamp(),dss.addedBy = 'Doctor',dss.verified = true ON MATCH SET dss.verified = true   MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code, multiplier : ds.multiplier[0], sourceID:dss.sourceID})ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSymptomSourcesWithMultipliers(Collection<Map<String, Object>> addSources);
     
	@Query("WITH $addSources AS pairs UNWIND pairs AS p   MATCH(illness:Illness{source:'MICA',state:p.state,icd10Code:p.icd10Code,version:p.version})-[ic]-(c:Category)-[cs]-(s:Symptom{code:p.symptomID})-[ss]-(ds:SymptomDataStore)  merge(ds)-[:HAS_SOURCES]-(dss:DataStoreSources{sourceID:p.sourceID}) ON CREATE SET dss.sourceRefDate=timestamp(),dss.addedBy = 'Doctor',dss.verified = true ON MATCH SET dss.verified = true  MERGE(sm:SymptomSourceAudit{icd10Code:illness.icd10Code,symptomID:s.code,sourceID:dss.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSymptomSourcesWithNoMultipliers(Collection<Map<String, Object>> addSources);
	
	
	
}
