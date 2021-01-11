package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.SymptomRule;
import com.advinow.mica.domain.queryresult.SymptomRuleModel;

/**
 * 
 * @author Govinda Reddy
 *
 */
@Repository
public interface SymptomRuleRepository extends Neo4jRepository<SymptomRule,Long> {
	
	
	@Query("Match(s:SymptomRule) return max(s.ruleID)")
	Integer getMaxRoleID();
		
	 @Query("match(sr:SymptomRule{ruleID:$ruleID})  "
	 		+ "  match(sst:SymptomTemplate)-[slr]-(ls:LinkStart) where id(ls)=sr.startID  optional match(ls)-[llr]-(lis:LinkListItem)  with sr,sst,ls,lis  "
	 		+ "  match(est:SymptomTemplate)-[tle]-(le:LinkEnd) where id(le)=sr.endID    optional match(le)-[llr]-(lie:LinkListItem)                   "
	 		+ "  return sr.ruleID as ruleID,sr.name as tag,sr.direction as direction,sr.lastUpdated as lastUpdated,sr.relation as relation_s_to_t,    sst.code as s_symptom_id, ls.simpleTrigger as s_trigger,ls.selection as s_rule,collect(distinct lis.code) as s_list_items,est.code as t_symptom_id,collect(distinct lie.code) as t_list_items")
	 SymptomRuleModel getSymptomRuleByRuleID(Integer ruleID);
	
	 @Query("match(sr:SymptomRule)  "
	 		+ "  match(sst:SymptomTemplate)-[slr]-(ls:LinkStart) where id(ls)=sr.startID  optional match(ls)-[llr]-(lis:LinkListItem)  with sr,sst,ls,lis"
	 		+ "  match(est:SymptomTemplate)-[tle]-(le:LinkEnd) where id(le)=sr.endID   optional match(le)-[llr]-(lie:LinkListItem)"
	 		+ "  return sr.ruleID as ruleID,sr.name as tag,sr.direction as direction,sr.lastUpdated as lastUpdated,sr.relation as relation_s_to_t,    sst.code as s_symptom_id, ls.simpleTrigger as s_trigger,ls.selection as s_rule,collect(distinct lis.code) as s_list_items,est.code as t_symptom_id,collect(distinct lie.code) as t_list_items")
	 List<SymptomRuleModel> getAllRules();

	 
	 SymptomRule findByRuleID(Integer ruleID);
	
	@Query("match(sr:SymptomRule{ruleID:$ruleID}) "
			+ " match(sst:SymptomTemplate)-[slr]-(ls:LinkStart) where id(ls)=sr.startID  optional match(ls)-[llr]-(lis:LinkListItem)  with sr,sst,ls,lis "
			+ " match(est:SymptomTemplate)-[tle]-(le:LinkEnd) where id(le)=sr.endID     optional match(le)-[llr]-(lie:LinkListItem)                    "
			+ "detach delete lie,le,lis,ls,sr")
	void deleteByRuleID(Integer ruleID);

	@Query("match(sr:SymptomRule{ruleID:$ruleID}) "
			+ " match(sst:SymptomTemplate)-[slr]-(ls:LinkStart) where id(ls)=sr.startID  optional match(ls)-[llr]-(lis:LinkListItem)  with sr,sst,ls,lis "
			+ " match(est:SymptomTemplate)-[tle]-(le:LinkEnd) where id(le)=sr.endID     optional match(le)-[llr]-(lie:LinkListItem)                    "
			+ "detach delete lie,le,lis,ls")
	void deleteSymptomLinks(Integer ruleID);
	
	
	
	
}
