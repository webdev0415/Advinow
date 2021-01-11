package com.advinow.mica.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.TreatmentSourceInfo;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface TreatmentSourceInfoRepository extends Neo4jRepository<TreatmentSourceInfo,Long> {

	@Query("Match(s:TreatmentSourceInfo) return max(s.sourceID)")
	Integer getMaxSourceID();

	@Query("match(src:TreatmentSourceInfo) where toUpper(src.source) contains($source) return src")
	List<TreatmentSourceInfo> searchSource(String source);

	TreatmentSourceInfo findBySource(String source);

	@Query("Match(s:TreatmentSourceInfo{sourceID:$sourceID}) detach delete s")
	void deleteBySourceID(Integer sourceID);


	@Query("MATCH(it:IllnessTreatment{icd10Code:$icd10code})-[itr]-(t:Treatment)-[tgr]-(tg:TreatmentGroup)-[tgn]-(dndrd)  optional match(dndrd)-[src]-(ts:TreatmentSources) with collect(distinct ts.sourceID) as sources match(tsi:TreatmentSourceInfo) where tsi.sourceID in sources return tsi")
	List<TreatmentSourceInfo> getTreatmentSourcesByIllness(String icd10code);
	
	
	//@Query("MATCH(SymptomTreatment{code:{0}})-[itr]-(t:Treatment)-[tgr]-(tg:TreatmentGroup)-[tgn]-(dndrd)  optional match(dndrd)-[src]-(ts:TreatmentSources) with collect(distinct ts.sourceID) as sources match(tsi:TreatmentSourceInfo) where tsi.sourceID in sources return tsi")
	@Query("MATCH(SymptomTreatment{code:$icd10code})-[itr]-(t:Treatment)-[tgr]-(tg:TreatmentGroup)-[tgn]-(dndrd)-[src]-(ts:TreatmentSources) with collect(distinct ts.sourceID) as sources match(tsi:TreatmentSourceInfo) where tsi.sourceID in sources return tsi")
	List<TreatmentSourceInfo> getTreatmentSourcesBySymptom(String icd10code);

	// remove source from drug/nondrug for illness
	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12] and toUpper(nd.name) = toUpper(p.drug) MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromDrugs(Collection<Map<String, Object>> sources);

	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11] and nd.typeDescID = p.typeDescID  MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromNonDrugs(Collection<Map<String, Object>> sources);


	// remove source form treatments

	@Query("WITH $drugsTreatment AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12]  MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromDrugTreatment(Collection<Map<String, Object>> drugsTreatment);

	@Query("WITH $nonDrugsTreatment AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11]  MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromNonDrugTreatment(Collection<Map<String, Object>> nonDrugsTreatment);


	// remove source from all treatments

	@Query("WITH $drugsAll AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment)-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12]  MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromAllDrugTreatment(Collection<Map<String, Object>> drugsAll);

	@Query("WITH $nonDrugsAll AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment)-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11]  MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromAllNonDrugTreatment(Collection<Map<String, Object>> nonDrugsAll);

	// add sources for treatments

	@Query("WITH $drugsAddSources AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug) WHERE  t.typeID IN [0,12] and toUpper(nd.name) = toUpper(p.drug)   MERGE (nd)-[:DRUG_TRT_SOURCES]-(tr:TreatmentSources{sourceID:p.sourceID})  ON CREATE SET tr.sourceRefDate=timestamp(),tr.addedBy = 'Doctor',tr.verified = true ON MATCH SET tr.verified = true    MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSourcesToDrugs(Collection<Map<String, Object>> drugsAddSources);

	@Query("WITH $nonDrugsAddSources AS pairs UNWIND pairs AS p  MATCH(illness:IllnessTreatment{icd10Code:p.icd10Code})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11] and nd.typeDescID = p.typeDescID   MERGE (nd)-[:NDRUG_TRT_SOURCES]-(tr:TreatmentSources{sourceID:p.sourceID})  ON CREATE SET tr.sourceRefDate=timestamp(),tr.addedBy = 'Doctor',tr.verified = true ON MATCH SET tr.verified = true      MERGE(sm:TreatmentSourceAudit{icd10Code:illness.icd10Code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSourcesToNonDrugs(Collection<Map<String, Object>> nonDrugsAddSources);



	// remove source from drug/nondrug for symptom
	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12] and toUpper(nd.name) = toUpper(p.drug) MERGE(sm:TreatmentSourceAudit{symptomID:st.code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromSymptomDrugs(Collection<Map<String, Object>> sources);

	@Query("WITH $sources AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11] and nd.typeDescID = p.typeDescID  MERGE(sm:TreatmentSourceAudit{symptomID:st.code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromNonDrugSymptoms(Collection<Map<String, Object>> sources);


	@Query("WITH $drugsTreatment AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12]  MERGE(sm:TreatmentSourceAudit{symptomID:st.code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromSymptomDrugTreatment(Collection<Map<String, Object>> drugsTreatment);

	@Query("WITH $nonDrugsTreatment AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11]  MERGE(sm:TreatmentSourceAudit{symptomID:st.code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromSymptomsNonDrugTreatment(Collection<Map<String, Object>> nonDrugsTreatment);


	@Query("WITH $drugsAddSources AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug) WHERE  t.typeID IN [0,12] and toUpper(nd.name) = toUpper(p.drug)   MERGE (nd)-[:DRUG_TRT_SOURCES]-(tr:TreatmentSources{sourceID:p.sourceID})  ON CREATE SET tr.sourceRefDate=timestamp(),tr.addedBy = 'Doctor',tr.verified = true ON MATCH SET tr.verified = true    MERGE(sm:TreatmentSourceAudit{symptomID:st.code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSourcesToDrugSymptoms(Collection<Map<String, Object>> drugsAddSources);

	@Query("WITH $nonDrugsAddSources AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment{code:p.symptomID})-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11] and nd.typeDescID = p.typeDescID   MERGE (nd)-[:NDRUG_TRT_SOURCES]-(tr:TreatmentSources{sourceID:p.sourceID})  ON CREATE SET tr.sourceRefDate=timestamp(),tr.addedBy = 'Doctor',tr.verified = true ON MATCH SET tr.verified = true      MERGE(sm:TreatmentSourceAudit{symptomID:st.code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action ")
	void mapSourcesToNonDrugsSymptoms(Collection<Map<String, Object>> nonDrugsAddSources);
	
	
	// remove source from all treatments

	@Query("WITH $drugsAll AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment)-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:Drug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [0,12]  MERGE(sm:TreatmentSourceAudit{symptomID:st.code,drugName:nd.name,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromAllDrugSymptomTreatment(Collection<Map<String, Object>> drugsAll);

	@Query("WITH $nonDrugsAll AS pairs UNWIND pairs AS p  MATCH(st:SymptomTreatment)-[itr]-(t:Treatment)-[ttr]-(tg:TreatmentGroup)-[rc]-(nd:NonDrug)-[ndr]-(tr:TreatmentSources{sourceID:p.sourceID}) WHERE  t.typeID IN [1,2,3,4,5,6,7,8,9,10,11]  MERGE(sm:TreatmentSourceAudit{symptomID:st.code,typeDescID:nd.typeDescID,sourceID:tr.sourceID}) ON CREATE SET sm.action=p.action ON MATCH SET sm.action=p.action  detach delete tr")
	void unMapSourcesFromAllNonDrugSymptomsTreatment(Collection<Map<String, Object>> nonDrugsAll);

	
	


}
