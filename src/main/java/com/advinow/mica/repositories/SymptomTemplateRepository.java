package com.advinow.mica.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;
import com.advinow.mica.domain.queryresult.SymptomGroupResult;
import com.advinow.mica.domain.queryresult.SymptomValidatorResultEnitity;
import com.advinow.mica.domain.queryresult.SymptomsLabOrders;
/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomTemplateRepository extends Neo4jRepository<SymptomTemplate,Long>{
	
	SymptomTemplate findByCode(String code, @Depth int depth);
	
	@Query("MATCH(st:SymptomTemplate)  WHERE toUpper(st.code)=$code RETURN st")
	SymptomTemplate findBySymptomCode(String code);
	
	@Query("MATCH (st:SymptomTemplate)  WHERE (st.es_questionText='' OR st. es_questionText is null OR   st.questionText is null OR st.questionText = '' OR st.criticality is null OR st.timeType is null  OR st.treatable is null OR NOT (st.criticality IN [0,1,2,3,4,5,6,7,8,9]) OR  st.antithesis is null OR  st.displaySymptom is null OR (st.range = true AND (st.minRange is null OR  st.maxRange is null )) )  AND (st.multipleValues is null)   return st")
	 Iterable<SymptomTemplate> getInvalidAtrForNonMultiplier();
	
	@Query("MATCH (st:SymptomTemplate)  WHERE (st.es_questionText='' OR st. es_questionText is null OR   st.questionText is null OR st.questionText = '' OR st.criticality is null OR st.timeType is null OR  st.treatable is null OR NOT (st.criticality IN [0,1,2,3,4,5,6,7,8,9]) OR  st.antithesis is null OR  st.displaySymptom is null OR st.cardinality is null OR (st.range = true AND (st.minRange is null OR  st.maxRange is null )) ) AND (st.multipleValues is not  null)    return st")
	 Iterable<SymptomTemplate> getInvalidAtrForMultiplier();
	
	@Query("MATCH(st:SymptomTemplate) return st")
	Set<SymptomTemplate> getAllSymptomTemplates();
	
	
	@Query("MATCH(st:SymptomTemplate)  with st   MATCH p= (st)-[src]-(sc:SnomedCodes)   return p")
	Set<SymptomTemplate> getSnomedSymptomTemplates();
	
	
	@Query("MATCH(st:SymptomTemplate) with st   MATCH p=  (st)-[src]-(rds:RCodeDataStore)   return p")
	Set<SymptomTemplate> getRcodeSymptomTemplates();
	
	
	@Query("MATCH(st:SymptomTemplate) WHERE st.code IN $symptoms  return st")
	Set<SymptomTemplate> getPatientBioSymptoms( List<String> symptoms);
	

	@Query("MATCH(sg:SymptomGroup{groupID:$groupID})-[sgr]->(sc:SymptomCategory )-[scr] -(st:SymptomTemplate) RETURN st")
	Set<SymptomTemplate> getAllSymptomTemplatesForGroup(String groupID);
	
	@Query("MATCH(sg:SymptomGroup{groupID:$groupID})-[sgr]-(se:Section)-[ser]->(sc:SymptomCategory )-[scr] -(st:SymptomTemplate) RETURN st")
	Set<SymptomTemplate> getAllSymptomTemplatesForPainGroup(String groupID);
		
	@Query("MATCH(sg:SymptomGroup)-[sgr]->(sc:SymptomCategory )-[scr] -(st:SymptomTemplate) where sc.code IN $catetories AND NOT(st.code IN $ignoreSymptoms)  RETURN st")
	Set<SymptomTemplate> getFamilySocialHistorySmptoms( List<String> catetories, List<String> ignoreSymptoms);
	
	@Query("match(st:SymptomTemplate) where st.definition is not null return st")
	List<SymptomTemplate> getAllSymptomDefinitions();
	
	@Query("MATCH (st:SymptomTemplate {code:$code})-[dkr]-(ds:SnomedCodes) detach delete ds")
	void deleteSnomdedCodes(String code);
	
	@Query("MATCH (st:SymptomTemplate {code:$code})-[dkr]-(ds:RCodeDataStore) detach delete ds")
	void deleteRCodeDataStore(String code);
	
	// Dr app
	
	@Query("match(sg:SymptomGroup)-[scg]-(s:Section)-[src]-(sc:SymptomCategory)-[r]->(st:SymptomTemplate)  return st.code as symptomID,  collect({symptomGroup:sg.name,categoryName:sc.name,symptomName:st.name}) as literalMap UNION match(sg1:SymptomGroup)-[src1]-(sc1:SymptomCategory)-[r1]->(st1:SymptomTemplate)  return st1.code as symptomID,  collect({symptomGroup:sg1.name,categoryName:sc1.name,symptomName:st1.name}) as literalMap")
	List<GenericQueryResultEntity>  findSymptomGroups();
	
	@Query("match(sg:SymptomGroup)-[scg]-(s:Section)-[src]-(sc:SymptomCategory)-[r]->(st:SymptomTemplate)  return st.code as symptomID,  collect({symptomGroup:sg.name,categoryName:sc.name,symptomName:st.name}) as literalMap")
	List<GenericQueryResultEntity>  findPainSwellingGroups();
	
	@Query("match(sg:SymptomGroup) return  collect({groupName:sg.name,updatedDate:sg.updatedDate}) as literalMap")
	GenericQueryResultEntity  findUpdatedTimeForGroups();
	
	
	@Query("MATCH (st:SymptomTemplate) with st  optional  MATCH p= (st)-[src]-(sc:SnomedCodes) with st,sc  optional match(ds:DataStore{code:sc.listValueCode}) return st.code as symptomID  ,ds.name As listValue ,sc.name as snomedName")
	List<GenericQueryResultEntity>  findSymptomSnomedGroups();

/*	@Query("MATCH(st:SymptomTemplate) where st.active= true SET st.active=false return st") 
	Set<SymptomTemplate> updateDFSymptomsToFalse();
	
	@Query("MATCH(st:SymptomTemplate) where st.code IN {0} SET st.active=true return st") 
	Set<SymptomTemplate> updateDataFrameSymptoms( Set<String> symptomIDs);
	
	@Query("MATCH(st:SymptomTemplate) where st.active=true return collect(st.code) as symptoms") 
	GenericQueryResultEntity getDataFrameSymptoms();
	
	@Query("WITH {0} AS pairs UNWIND pairs AS p  MATCH(st:SymptomTemplate) where st.code =p.code  SET st.antithesis=p.antithesis  return st") 
	List<SymptomTemplate> updateDataFrameSymptoms(Collection<Map<String, Object>> symptoms);
	
	@Query("MATCH(st:SymptomTemplate) with st optional  match(dk:DataKeys{name:st.multipleValues})-[src]-(ds:DataStore) return st.code as symptomID, ds.code AS descriptorID, CASE WHEN ds.code<>''  THEN ds.m_antithesis  ELSE  st.antithesis   END AS dcl,ds.name as descriptorName")  
	List<GenericQueryResultEntity> getSymptomsDescriptosDcls();
*/
	// pagination starts herer
	// group name, symptom name,symptom code for dataframe
	
	@Depth(value = 0)
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Page<SymptomTemplate> findPainSymptomsBySourceAndCodeAndNameAndGroup(String source, String group, String code,String name, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findPainSymptomsCountBySourceAndCodeAndNameAndGroup(String source, String group, String code,String name);
	
	@Depth(value = 0)
	@Query(value ="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndCodeAndNameAndGroup(String source, String group, String code,String name, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndCodeAndNameAndGroup(String source, String group, String code,String name);
	
	
	// group name, symptom code
	@Query(value ="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Page<SymptomTemplate> findPainSymptomsBySourceAndCodeAndGroup(String source, String group, String code, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findPainSymptomsCountBySourceAndCodeAndGroup(String source, String group, String code);
	
	
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndCodeAndGroup(String source, String group, String code, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndCodeAndGroup(String source, String group, String code);
	
	// group name, symptom name
	
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)    return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Page<SymptomTemplate> findPainSymptomsBySourceAndNameAndGroup(String source, String group, String name, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Integer findPainSymptomsCountBySourceAndNameAndGroup(String source, String group,String name);
	
	
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)  return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndNameAndGroup(String source, String group,String name, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndNameAndGroup(String source, String group, String name);
	
	
	
	// symptom name, symptom code
	
	
	@Query(value="match(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(sg:SymptomGroup})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndCodeAndName(String source, String code,String name, Pageable pageable);
	
	@Query("match(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name) AND toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndCodeAndName(String source, String code,String name);
	
	
	// group name
	
	
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)   return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)   return COUNT(st)")  
	Page<SymptomTemplate> findPainSymptomsBySourceAndGroup(String source, String group, Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(s:Section)-[sr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)   return COUNT(st)")  
	Integer findPainSymptomsCountBySourceAndGroup(String source, String group);
	
	
	@Query(value="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)    return st",
			countQuery="match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndGroup(String source, String group,  Pageable pageable);
	
	@Query("match(sg:SymptomGroup{groupID:$group})-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndGroup(String source, String group);

	
	// symptom code
	
	
	@Query(value="match(st:SymptomTemplate) where  toUpper(st.code) CONTAINS($code)   return st",
			countQuery="match(st:SymptomTemplate) where toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndCode(String source, String code, Pageable pageable);
	
	@Query("match(st:SymptomTemplate) where toUpper(st.code) CONTAINS($code)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndCode(String source, String code);
		
	
	// symptom name
	
	@Query(value="match(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return st",
			countQuery="match(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Page<SymptomTemplate> findSymptomsBySourceAndName(String source, String name, Pageable pageable);
	
	@Query("match(st:SymptomTemplate) where toUpper(st.name) CONTAINS($name)   return COUNT(st)")  
	Integer findSymptomsCountBySourceAndName(String source, String name);
	
	@Query("match(st:SymptomTemplate)  return COUNT(st.code)")
	Integer geSymptomsCountBySource(String source);
	
	@Query("match(sc:SymptomCategory)-[src]-(st:SymptomTemplate) where toUpper(st.name) CONTAINS($symptomName)  RETURN DISTINCT st.code")
	List<String>  findAllSymptomIDForGivenName(String symptomName);
	
	// pgination for templates
	
	@Depth(value = 0)
	@Query("MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes  RETURN count(DISTINCT st)")
	Integer findByCategoryCount(List <String> codes);
	
	/*@Depth(value = 0)
	@Query(value = "MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes  RETURN DISTINCT st",countQuery= "MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes  RETURN count(DISTINCT st)")
	Page<SymptomTemplate> findByCategory(List <String> codes, Pageable pageable);
	*/
	
	@Depth(value = 0)
	@Query(value = "MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes  RETURN DISTINCT st ORDER BY st.code ASC SKIP $skip LIMIT $limit")
	List<SymptomTemplate> findByCategory(List <String> codes, Integer skip,Integer limit );
	
	
	
	@Depth(value = 0)
	@Query(value="MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes AND toUpper(st.name) CONTAINS($symptomName) RETURN DISTINCT st  ORDER BY st.code ASC SKIP $skip LIMIT $limit")
	List<SymptomTemplate> findByCategoryAndName(List <String> codes,String symptomName,Integer skip, Integer limit );
	
	
	/*@Depth(value = 0)
	@Query(value="MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes AND toUpper(st.name) CONTAINS($symptomName) RETURN DISTINCT st",countQuery= "MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes AND toUpper(st.name) CONTAINS($symptomName) RETURN count(DISTINCT st)")
	Page<SymptomTemplate> findByCategoryAndName(List <String> codes,String symptomName, Pageable pageable);
	*/
	
	@Depth(value = 0)
	@Query("MATCH(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where sc.code IN $codes AND toUpper(st.name) CONTAINS($name) RETURN count(DISTINCT st)")
	Integer findByCategoryAndNameCount(List <String> codes,String name);
	
	@Query("match(sg:SymptomGroup)-[sgr]-(sc:SymptomCategory)  where toUpper(sg.groupID) CONTAINS($groupName)  return sc.code")
	List<String>  getCategoriesByGroup(String groupName);
	
	@Query("MATCH (st:SymptomTemplate) with st  optional  MATCH p= (st)-[src]-(scs:SnomedCodes) optional  MATCH  (st)-[src1]-(lg:LogicalSymptomGroups)     return st.code as symptomID, st.name as symptomName, st.optionKey as optionKey, st.multipleValues as multipleValues, st.descriptorFile as imageName,st.dataStoreTemplates as referenceTypes, st.subGroups as subGroups,collect(distinct lg.groupID) as logicalGroupIDList,  collect({snomedName: scs.name,snomedCode:scs.conceptID,optionCode:scs.listValueCode}) as literalMap")
	List<GenericQueryResultEntity>  getSymptomsWithSnomedCodes();
	
	
	@Query("match(st:SymptomTemplate)  return st")
	List<SymptomTemplate> getAllQuestions();

	@Query("match(st:SymptomTemplate)  where toUpper(st.code) = $symptomID   return st")
	SymptomTemplate getSymptomQuestion(String symptomID);
	
	
	/*@Depth(value = 0)
	@Query(" Call db.index.fulltext.queryNodes('templates', {0}) YIELD node as node1  with node1   match(node1)-[]-(sc1:SymptomCategory) where sc1.code <>'SYMPTCG33'  return node1 as st UNION CALL db.index.fulltext.queryNodes('snomedcodes', {0}) YIELD node  with node   match(node)-[]-(st:SymptomTemplate)-[]-(sc:SymptomCategory) where sc.code <>'SYMPTCG33' return st  ")  
	Page<SymptomTemplate> searchSymptoms(String name, Pageable pageable);
	
	@Depth(value = 0)
	@Query("Call db.index.fulltext.queryNodes('templates', {0}) YIELD node as node1  with node1   match(node1)-[]-(sc1:SymptomCategory) where sc1.code <>'SYMPTCG33'   return node1 as st  UNION  CALL db.index.fulltext.queryNodes('snomedcodes', {0}) YIELD node  with node   match(node)-[]-(st:SymptomTemplate)-[]-(sc:SymptomCategory) where sc.code <>'SYMPTCG33' return st ")  
	Set<SymptomTemplate> searchSymptomsCount(String name);
*/
			
	@Depth(value = 0)
	@Query("Call db.index.fulltext.queryNodes('templates', $name) YIELD node as node1,score as score1    with node1,score1   match(node1)-[]-(sc1:SymptomCategory) where sc1.code <>'SYMPTCG33'   return  node1.code as symptomID,node1.name as symptomName, score1 as score UNION CALL db.index.fulltext.queryNodes('snomedcodes', $name) YIELD node,score  with node,score   match(node)-[]-(st:SymptomTemplate)-[]-(sc:SymptomCategory) where sc.code <>'SYMPTCG33' return st.code as symptomID, st.name as symptomName,score")  
	List<GenericQueryResultEntity> searchSymptoms(String name);
	
	
	@Query("match(st:SymptomTemplate)-[src]-(gr:LogicalSymptomGroups) where  gr.groupID = $groupID  return DISTINCT st.code as symptomID, gr.groupFlag as groupFlag")
	List<SymptomGroupResult>  getSymptomsForGivenGroup(Integer groupID);
	
	@Query("match(st:SymptomTemplate)-[src]-(gr:LogicalSymptomGroups) where  gr.groupID = $groupID   detach delete gr  return DISTINCT st.code as symptoms ")
	List<String>   deleteLogicalSymptomGroups(Integer groupID);
	
	/*@Query("match(sg:SymptomGroup)-[scg]-(s:Section)-[src]-(sc:SymptomCategory)-[r]->(st:SymptomTemplate)  return st.code as symptomID,  collect({symptomGroup:sg.name,categoryName:sc.name,symptomName:st.name}) as literalMap")
	List<GenericQueryResultEntity>  findPainSwellingGroups();*/
	
	
/*	@Query("match(st:SymptomTemplate) where  st.groupID = {0}  set st.groupID = null  return st.code")
	 List<String>  resetSymptomsGroupID(Integer groupID);
	
	@Query("match(st:SymptomTemplate) where  st.code IN  {1}   set st.groupID = {0}  return st.code")
	 List<String>  updateSymptomsGroupID(Integer groupID,List<String> symptoms);
	*/
	
	@Depth(value = 2)
	@Query("match(st:SymptomTemplate) where  st.code IN  $symptoms  return st ")
	List<SymptomTemplate>  getSymptomTemaplesByGroups(List<String> symptoms);
	
	
	
	@Query("CALL getSymptomTypes('ALL')  YIELD code,name,symptomGroup,basicSymptom where code IN $symptoms and symptomGroup is not null   RETURN   collect({symptomID:code,groupName:symptomGroup}) as literalMap")	  
	GenericQueryResultEntity getSymptomGroups(Set<String> symptoms); 
	

	@Query("CALL getSymptomTypes('ALL')  YIELD active,groupName,code,categoryName,subGroups,painSwellingID,symptomGroup where code IN $symptoms  and subGroups is not null   RETURN  groupName as symptomGroupName ,code as symptomID,painSwellingID,symptomGroup,subGroups,categoryName")	  
	List<GenericQueryResultEntity> getSubGroupSymptoms(Set<String> symptoms); 
	
	
	// Logical symptom groups
	
	@Query("MATCH (st:SymptomTemplate {code:$code})-[dkr]-(lsg:LogicalSymptomGroups) detach delete lsg")
	void deleteLogicalSymptomsGroups(String code);
	
	
	@Query("MATCH (st:SymptomTemplate)-[dkr]-(lsg:LogicalSymptomGroups) with st,lsg match(sc:LogicalSymptomGroupsRef{groupID:lsg.groupID,type:'ROS'})   return st.code  as symptomID, collect(sc.type+ '-'+ lsg.groupFlag +'-' + sc.logicalGroupName) as logicalGroupNames UNION  MATCH (st1:SymptomTemplate)-[dkr1]-(lsg1:LogicalSymptomGroups) with st1,lsg1 match(sc1:LogicalSymptomGroupsRef{groupID:lsg1.groupID,type:'LOGICAL'})   return st1.code  as symptomID, collect(sc1.name) as logicalGroupNames")  
	List<GenericQueryResultEntity> getLogicalGroups();
	
	@Query("MATCH (st:SymptomTemplate)-[dkr]-(lsg:LogicalSymptomGroups) with st,lsg match(sc:LogicalSymptomGroupsRef{groupID:lsg.groupID,type:'DE'})   return st.code  as symptomID, collect(sc.type+ '-'+ lsg.groupFlag +'-' + sc.logicalGroupName) as deGroupNames")  
	List<GenericQueryResultEntity> getDEGroups();


	@Query("MATCH (sg:SymptomGroup{groupID:'labs'})-[src]-(sc:SymptomCategory)-[rsc]-(st:SymptomTemplate{code:$symptomID} ) return st;")  
	SymptomTemplate findLabSymptoms(String symptomID);
	
	
	@Query("MATCH (sg:SymptomGroup{groupID:'labs'})-[src]-(sc:SymptomCategory)-[rsc]-(st:SymptomTemplate{code:$symptomID})-[stc]-(sl:SymptomLabOrders) detach delete stc,sl")  
	void deleteSymptomLabOrder(String symptomID);

	@Query("MATCH (sg:SymptomGroup{groupID:'labs'})-[src]-(sc:SymptomCategory)-[rsc]-(st:SymptomTemplate{code:$symptomID})-[stc]-(sl:SymptomLabOrders) return st.code as symptomID, collect(st.name) as labsOrdered")  
    SymptomsLabOrders getLabOrdersBySymptomID(String symptomID);
	
	@Query("MATCH (sg:SymptomGroup{groupID:'labs'})-[src]-(sc:SymptomCategory)-[rsc]-(st:SymptomTemplate)-[stc]-(sl:SymptomLabOrders) return st.code as symptomID, collect(st.name) as labsOrdered")  
	List<SymptomsLabOrders> getAllLabOrders();
	
	
 @Query("match(sg:SymptomGroup)-[sgr]-(sc:SymptomCategory)-[scr]-(st:SymptomTemplate) where st.code in $symptoms  with st,sc,sg optional MATCH (dk:DataKeys{name:st.multipleValues})-[dkds]-(ds:DataStore)  return sg.groupID as group,  sc.code as categoryID,sc.bodyLocations as bodyPartLocations, st.code AS code, collect(ds.name) as descriptors, st.symptomType as symptomType" +
    " UNION " +
    "  match(sg1:SymptomGroup)-[sgr1]-(sec:Section)-[secr]-(sc1:SymptomCategory)-[scr1]-(st1:SymptomTemplate) where st1.code in ['SYMPT0003174','SYMPT0000884','SYMPT0000001','SYMPT0000002']  with st1,sc1,sg1 optional MATCH (dk1:DataKeys{name:st1.multipleValues})-[dkds1]-(ds1:DataStore)  return sg1.groupID as group,  sc1.code as categoryID,sc1.bodyLocations as bodyPartLocations, st1.code AS code, collect(ds1.name) as descriptors, st1.symptomType as symptomType")	  
	List<SymptomValidatorResultEnitity> getSymptomSettings(List<String> symptoms);

  @Query("match(st:SymptomTemplate)-[src]-(tp:TreatmentPolicy) where  tp.policyID = $policyID  detach delete tp  return DISTINCT st.code as symptoms ")
  List<String> deletePolicies(Integer policyID); 
  
     @Depth(value = 2)
	@Query("match(st:SymptomTemplate) where  st.code IN  $symptoms  return st ")
	List<SymptomTemplate>  getSymptomTemaplesByPolicies(List<String> symptoms);
     
     @Query("match(st:SymptomTemplate)-[src]-(tp:TreatmentPolicy) where  tp.policyID = $symptoms  return DISTINCT  st.code as symptoms ")
     List<String>  getSymptomsForGivenPolicy(Integer policyID);
 	
	
	
}
