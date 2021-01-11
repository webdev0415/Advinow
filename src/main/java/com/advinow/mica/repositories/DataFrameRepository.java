/**
 *
 */
package com.advinow.mica.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DataStore;
import com.advinow.mica.domain.Illness;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.DataframeQueryResultEntity;
import com.advinow.mica.model.dataframe.DFIllness;
import com.advinow.mica.model.dataframe.DFIllnessSymptom;
import com.advinow.mica.model.dataframe.DFSymptomData;
import com.advinow.mica.model.dataframe.DFSymptomTemplate;
import com.advinow.mica.model.dataframe.DFSymptomTypes;


/**
 * @author Govinda Reddy
 *
 */
@Repository
public interface DataFrameRepository  extends Neo4jRepository<Illness,Long> {

	@Query("WITH $illnesses AS pairs UNWIND pairs AS p  MATCH(illness:Illness) where illness.icd10Code =p.icd10Code AND illness.source='MICA' AND illness.version=p.version AND illness.dfstatus='APPROVED'  SET illness.active=true  return illness")
	List<Illness> updateDataFrameIllnesses(Collection<Map<String, Object>> illnesses);

	@Query("MATCH(illness:Illness) where  illness.source='MICA' AND illness.active=true  AND illness.dfstatus='APPROVED'  SET illness.active=false  return illness")
	List<Illness> updateDFIllnessesToFalse();


	@Query("MATCH(ill:Illness)-[ic]-(c:Category)-[cs]-(s:Symptom) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'  with ill.icd10Code as icd10Code, collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   match(illness:Illness{source:'MICA',dfstatus:'APPROVED',version:version,icd10Code:icd10Code,active:true}) return distinct illness.icd10Code as icd10code,illness.version as version") 
	List<DataframeQueryResultEntity> getDataFrameIllnesses();

	@Query("WITH $illnesses AS pairs UNWIND pairs AS p  MATCH(illness:Illness) where illness.icd10Code =p.icd10Code AND illness.source='MICA'  AND illness.dfstatus='APPROVED'  SET illness.prior=p.prior  return illness")
	List<Illness> updateDFIllnessPriors(Collection<Map<String, Object>> illnesses);

	@Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'   with  ill.icd10Code as icd10Code, collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version MATCH(illness:Illness{icd10Code: icd10Code,source:'MICA',dfstatus:'APPROVED',version:version})  RETURN  distinct illness.icd10Code as icd10Code, illness.prior as prior,illness.cluster as cluster")
	List<DataframeQueryResultEntity> getDataFramePriorsANDClusters();

	@Query("WITH $illnesses AS pairs UNWIND pairs AS p  MATCH(illness:Illness) where illness.icd10Code =p.icd10Code AND illness.source='MICA'  AND illness.dfstatus='APPROVED'  SET illness.cluster=p.cluster  return illness")
	List<Illness> updateDFIllnessClusters(Collection<Map<String, Object>> illnesses);

	@Query("CALL populateDfIllnesses()")
	String createDFIllnessesFromSources();

	@Query("CALL getSymptomTypes('All') YIELD active,groupName,code,name,categoryID,symptomType,bodyLocations,subGroups,symptomGroup,basicSymptom,partOfGroup, displaySymptom  RETURN groupName,code,name,categoryID,symptomType,bodyLocations,subGroups,symptomGroup,basicSymptom,partOfGroup, displaySymptom")
	List<DFSymptomTypes>  getSymptomTypes();

	@Query("CALL getSymptomTypes('ALL')  YIELD active,groupName,code,name,categoryID,symptomType,bodyLocations,subGroups,symptomGroup,painSwellingID,basicSymptom,partOfGroup, displaySymptom where active=$flag OR painSwellingID in[600,8,800]  RETURN groupName,code,name,categoryID,symptomType,bodyLocations,subGroups,symptomGroup,basicSymptom,partOfGroup, displaySymptom")
	List<DFSymptomTypes>  getSymptomTypes(Boolean flag);

	@Query("MATCH (dk:DataKeys) WHERE NOT (dk)-[:RELATED_TO]-() WITH dk MATCH (st:SymptomTemplate{multipleValues:dk.name}) return collect(st.code) as  symptoms")
	DataframeQueryResultEntity getExternalSymptoms();

	@Query("MATCH (dk:DataKeys) WHERE NOT (dk)-[:RELATED_TO]-() WITH dk MATCH (st:SymptomTemplate{multipleValues:dk.name,active:{0}}) return collect(st.code) as  symptoms")
	DataframeQueryResultEntity getExternalSymptoms(Boolean flag);

/*	@Query("Match(st:SymptomTemplate{active:{0}}) where st.painSwellingID in [8,600,800]  return  collect(st.code) as symptoms")
	GenericQueryResultEntity getBasicSymptoms(Boolean flag);
*/
	/*@Query("Match(st:SymptomTemplate) where st.painSwellingID in [8,600,800]  return  collect(st.code) as symptoms")
	GenericQueryResultEntity getBasicSymptoms();*/
	@Query("Match(st:SymptomTemplate) where  st.active =$flag OR  st.painSwellingID in [8,600,800]  with st  OPTIONAL MATCH (dk:DataKeys{name:st.multipleValues})-[dkr]-(ds:DataStore) RETURN   ds.name as dsName, ds.value as dsAlieasName,ds.m_antithesis AS  dsAntithesis, ds.code as dsCode,st.code As symptomID ,st.antithesis AS symptomAntithesis")
	List<DFSymptomTemplate> getSymptomAntithesis(Boolean flag);

	@Query("Match(st:SymptomTemplate)  with st  OPTIONAL MATCH (dk:DataKeys{name:st.multipleValues})-[dkr]-(ds:DataStore) RETURN   ds.name as dsName, ds.value as dsAlieasName,ds.m_antithesis AS  dsAntithesis, ds.code as dsCode,st.code As symptomID ,st.antithesis AS symptomAntithesis")
	List<DFSymptomTemplate> getSymptomAntithesis();

	/* @Query("MATCH (ill:DFPreIllness)   WHERE ill.active={0} AND  ill.source='MICA'  WITH ill  MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore) where ds.bias=true  with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,illness.version AS version,s.code AS symptomID ,s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.start AS start, dm.value AS stop , dm.timeUnit As timeUnit,ds.likelihood AS dsLikelihood ,dm.likelihood AS dmLikelihood , dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.relatedIc10Codes as relatedIc10Codes ")
	 List<DFSymptomData> getApprovedIllneness(Boolean active) ;

	 @Query("MATCH (ill:DFPreIllness)   WHERE  ill.source='MICA'  WITH ill  MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,illness.version AS version,s.code AS symptomID ,s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.start AS start, dm.value AS stop , dm.timeUnit As timeUnit,ds.likelihood AS dsLikelihood ,dm.likelihood AS dmLikelihood,dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.relatedIc10Codes as relatedIc10Codes ")
	 List<DFSymptomData> getApprovedIllneness() ;*/
	 
	 
/*	 @Query("MATCH (ill:DFPreIllness)   WHERE ill.active={0} AND  ill.source='MICA'  WITH ill  MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)   with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.relatedIc10Codes as relatedIc10Codes, ds.bias as bias")
	 List<DFSymptomData> getApprovedIllneness(Boolean active) ;*/
	
	 @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA' and ill.active=$active  with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,c.code as categoryID, s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.likelyDiseases as relatedIc10Codes, ds.bias as bias,ds.minDiagCriteria as mdc, ds.ruleOut as ruleOut,ds.must as must, id(ds) as dsId ")
	 List<DFSymptomData> getApprovedIllneness(Boolean active) ;

	/* @Query("MATCH (ill:DFPreIllness)   WHERE  ill.source='MICA'  WITH ill  MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.relatedIc10Codes as relatedIc10Codes, ds.bias as bias ")
	 List<DFSymptomData> getApprovedIllneness() ;
*/
	 
	 @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'  with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)-[ss]-(ds:SymptomDataStore)  with illness,c,s,ds optional match(ds)-[dsm]-(dm:DataStoreModifier)  RETURN DISTINCT illness.icd10Code AS icd10Code,s.code AS symptomID ,c.code as categoryID, s.bodyParts AS bodyParts ,ds.multiplier AS multiplier, dm.timeFrame as timeFrame ,CASE WHEN ds.likelihood is null  THEN dm.likelihood ELSE ds.likelihood   END AS likelihood, ds.likelyDiseases as relatedIc10Codes, ds.bias as bias,ds.minDiagCriteria as mdc, ds.ruleOut as ruleOut,ds.must as must,id(ds) as dsId")
	 List<DFSymptomData> getApprovedIllneness(); 
	 
	 
/*	 @Query("MATCH (ill:MITA_Illness)   return  collect({icd10Code:ill.icd10Code,groupName:ill.name,rule_id:ill.rule_id}) as literalMap   " )
	 GenericQueryResultEntity getMasterIllnessInfo();*/

	/* @Query("MATCH (ill:MITA_Illness) where ill.icd10Code IN {0}   return  collect({icd10Code:ill.icd10Code,groupName:ill.name}) as literalMap   " )
	 GenericQueryResultEntity getIllnessGroupNames(List<String> illness);
*/
     
	 @Query("MATCH (ill:MITA_Illness)     return ill.icd10Code as icd10code ,ill.name as groupName ,ill.rule_id as rule_id   " )
	 List<DataframeQueryResultEntity> getMasterIllnessInfo();
	 
	 
/*	 @Query("MATCH (ill:DFPreIllness)  WHERE   ill.source='MICA' AND ill.active = {0}   WITH ill     MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) return DISTINCT  illness.icd10Code as icd10Code  ,  illness.name as name,   illness.criticality AS criticality,illness.cluster as cluster, illness.prevalence as prevalence" )
	 List<DFIllness> getApprovedDiseases(Boolean active);
*/

	 
	 @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'  AND ill.active = $active   with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) return DISTINCT  illness.icd10Code as icd10Code  ,  illness.name as name, illness.criticality AS criticality,illness.cluster as cluster, illness.prevalence as prevalence" )
	 List<DFIllness> getApprovedDiseases(Boolean active);

	 
	 
  /*    @Query("MATCH (ill:DFPreIllness)  WHERE   ill.source='MICA'   WITH ill     MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) return DISTINCT  illness.icd10Code as icd10Code  ,  illness.name as name, illness.criticality AS criticality,illness.cluster as cluster, illness.prevalence as prevalence" )
 	 List<DFIllness> getApprovedDiseases();*/
	 
	    @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'   with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom) return DISTINCT  illness.icd10Code as icd10Code  ,  illness.name as name, illness.criticality AS criticality,illness.cluster as cluster, illness.prevalence as prevalence" )
	 	 List<DFIllness> getApprovedDiseases();
	 
/*
     @Query("MATCH (ill:DFPreIllness)   WHERE  ill.source='MICA' AND ill.active = {0}    WITH ill MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  WHERE  s.code IN {1}  return illness.version as version, illness.icd10Code as icd10Code, illness.dfstatus as dfstatus, collect(distinct s.code) as symptoms " )
	 List<DFIllnessSymptom> getIcd10CodesWithSymptomsFromAllSources(Boolean active,	List<String> includeSymptoms);
*/


     @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'  AND ill.active = $active   with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)  WHERE  s.code IN $includeSymptoms  return illness.version as version, illness.icd10Code as icd10Code, illness.dfstatus as dfstatus, collect(distinct s.code) as symptoms " )
	 List<DFIllnessSymptom> getIcd10CodesWithSymptomsFromAllSources(Boolean active,	List<String> includeSymptoms);
     
     
    /* @Query("MATCH (ill:DFPreIllness)   WHERE  ill.source='MICA'   WITH ill MATCH(illness:Illness{icd10Code:ill.icd10Code, version:ill.version,source:ill.source,dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)   return illness.version as version, illness.icd10Code as icd10Code, illness.dfstatus as dfstatus, collect(distinct s.code) as symptoms " )
	 List<DFIllnessSymptom> getIcd10CodesWithSymptomsFromAllSources();
*/

     
     @Query("MATCH(ill:Illness) where ill.dfstatus = 'APPROVED' AND  ill.source='MICA'  with ill.icd10Code as icd10Code,  collect(ill.version) as versions unwind versions as version_list with icd10Code,  max(version_list) as version   MATCH(illness:Illness{icd10Code:icd10Code, version:version,source:'MICA',dfstatus:'APPROVED'})-[ic]-(c:Category)-[cs]-(s:Symptom)   return illness.version as version, illness.icd10Code as icd10Code, illness.dfstatus as dfstatus, collect(distinct s.code) as symptoms" )
	 List<DFIllnessSymptom> getIcd10CodesWithSymptomsFromAllSources();
       
     
     @Query("match(s:SymptomConfig{type:'DATAFRAME'}) return s.ignore as ignoresymptoms")
     List<String> getIgnoreSymptoms();
     
     @Query("match(dk:DataKeys{code:'DK430'})-[]-(ds:DataStore) return collect(ds.name) as timePeriods ")
     DataframeQueryResultEntity getTimeBuckets();
     
 	@Query("WITH $dataStores AS pairs UNWIND pairs AS p  MATCH(ds:DataStore) where ds.code =p.code  SET ds.m_antithesis=p.m_antithesis  return ds") 
 	List<DataStore> updateDataFrameDescriptors(Collection<Map<String, Object>> dataStores);
 	
	@Query("MATCH (st:SymptomTemplate)-[dkr]-(lsg:LogicalSymptomGroups) with st,lsg match(sc:LogicalSymptomGroupsRef{groupID:lsg.groupID})   return st.code  as symptomID, collect(sc.name) as logicalGroupNames,sc.type as type")  
	List<DataframeQueryResultEntity> getLogicalGroups();
	

	@Query("MATCH(st:SymptomTemplate) where st.active= true SET st.active=false return st") 
	Set<SymptomTemplate> updateDFSymptomsToFalse();
	
	@Query("MATCH(st:SymptomTemplate) where st.code IN $symptomIDs SET st.active=true return st") 
	Set<SymptomTemplate> updateDataFrameSymptoms( Set<String> symptomIDs);
	
	@Query("MATCH(st:SymptomTemplate) where st.active=true return DISTINCT  st.code as symptoms") 
	Set<String> getDataFrameSymptoms();
	
	@Query("WITH $symptomIDs AS pairs UNWIND pairs AS p  MATCH(st:SymptomTemplate) where st.code =p.code  SET st.antithesis=p.antithesis  return st") 
	List<SymptomTemplate> updateDataFrameSymptoms(Collection<Map<String, Object>> symptoms);
	
	@Query("MATCH(st:SymptomTemplate) with st optional  match(dk:DataKeys{name:st.multipleValues})-[src]-(ds:DataStore) return st.code as symptomID, ds.code AS descriptorID, CASE WHEN ds.code<>''  THEN ds.m_antithesis  ELSE  st.antithesis   END AS dcl,ds.name as descriptorName")  
	List<DataframeQueryResultEntity> getSymptomsDescriptosDcls();
	
	

	@Query("match(dk:DataKeys{name:'Timebucket'})-[src]-(ds:DataStore) return ds.name order by ds.displayOrder") 
	List<String> getTimePeriodsMasterList();
	

	
	
}