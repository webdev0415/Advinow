/**
 *
 */
package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.Illness;
import com.advinow.mica.model.dataframe.TimeSymptoms;


/**
 * @author Govinda Reddy
 *
 */
@Repository
public interface IllnessReportRepository  extends Neo4jRepository<Illness,Long> {

	

	@Query("match(dk:DataKeys{name:'Timebucket'})-[src]-(ds:DataStore) return ds.name order by ds.displayOrder") 
	List<String> getTimePeriodsMasterList();
	
	@Query("MATCH(ill:Illness) where ill.state = 'APPROVED' AND  ill.source='MICA'  with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:'MICA',dfstatus:'APPROVED',version:version,icd10Code:icd10Code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsm]-(dm:DataStoreModifier) RETURN DISTINCT illness.icd10Code AS icd10Code, illness.version as version,  s.code AS symptomID,ds.multiplier AS multiplier,dm.timeFrame as timeFrame, dm.likelihood AS likelihood, id(dm) as dmId, id(ds) as dsId ")  
	List<TimeSymptoms>  getTimeSymptoms();
	
	@Query("MATCH(ill:Illness) where ill.state = 'APPROVED' AND  ill.source='MICA' and toUpper(ill.name) contains(toUpper('Chronic'))    with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:'MICA',dfstatus:'APPROVED',version:version,icd10Code:icd10Code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsm]-(dm:DataStoreModifier) with illness,ds,dm,s match(st:SymptomTemplate{code:s.code})   RETURN DISTINCT illness.icd10Code AS icd10Code,illness.name as icd10Name,  st.code AS symptomID,st.name as symptomName,  ds.multiplier AS multiplier,dm.timeFrame as timeFrame, dm.likelihood AS likelihood, id(dm) as dmId,ds.minDiagCriteria as mdc")  
	List<TimeSymptoms>  getChronicTimeSymptoms();
     
    
	@Query("MATCH(ill:Illness) where ill.state = 'APPROVED' AND  ill.source='MICA' and toUpper(ill.name) contains(toUpper('acute'))    with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version  match(illness:Illness{source:'MICA',dfstatus:'APPROVED',version:version,icd10Code:icd10Code})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)-[dsm]-(dm:DataStoreModifier) with illness,ds,dm,s match(st:SymptomTemplate{code:s.code})   RETURN DISTINCT illness.icd10Code AS icd10Code,illness.name as icd10Name,  st.code AS symptomID,st.name as symptomName,  ds.multiplier AS multiplier,dm.timeFrame as timeFrame, dm.likelihood AS likelihood, id(dm) as dmId,ds.minDiagCriteria as mdc")  
	List<TimeSymptoms>  getAcuteTimeSymptoms();
     
	
	
}