package com.advinow.mica.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.advinow.mica.domain.PVMaster;
import com.advinow.mica.domain.PVMasterQueryResult;

public interface PVMasterRepository extends Neo4jRepository<PVMaster,Long> {
/*	
	@Query("Call db.index.fulltext.queryNodes('pvmaster', {0}) ")  
	List<PVQueryResult> searchPvName(String name);
*/
	@Query("Call db.index.fulltext.queryNodes('pvmaster', $fuzzySearch) YIELD node as node, score as score optional match(rxm1:MICARxNorms{rxcui:node.rxcui})-[]-(grx:IngredientRxcui)    return  distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,  score as score, collect(distinct grx.ingredientRxcui) as genericRxcui")  
	List<PVMasterQueryResult> searchPvDruName(String fuzzySearch);

	@Query("Call db.index.fulltext.queryNodes('pvmaster', $fuzzySearch) YIELD node as node, score as score  where node.marketing_status= $status   optional match(rxm1:MICARxNorms{rxcui:node.rxcui})-[]-(grx:IngredientRxcui)    return  distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,  score as score, collect(distinct grx.ingredientRxcui) as genericRxcui ")  
	List<PVMasterQueryResult> searchPvDruNameWithParameters(String fuzzySearch, String status);

	/*@Query("match(pv:PVMaster)-[ss]-(rx:PVGenericRx)  where pv.pvid IN {0}   with  collect( distinct  rx.rxcui) as rxcuis,pv  match(node:PVMaster)-[ss1]-(rx1:PVGenericRx)   where rx1.rxcui in rxcuis and node.name <> pv.name  and node.marketing_status= {1}   with node,collect(distinct rx1.rxcui) as genericRxcui,rxcuis where size(rxcuis)=size(genericRxcui)   return distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,genericRxcui")  
	List<PVMasterQueryResult> getCombinationDrugWithParameters(List<Long> pvid,String status);
*/
	/*@Query("match(pv:PVMaster)-[ss]-(rx:PVGenericRx)  where pv.pvid IN {0}  with  collect( distinct  rx.rxcui) as rxcuis,pv  match(node:PVMaster)-[ss1]-(rx1:PVGenericRx)   where rx1.rxcui in rxcuis and node.name <> pv.name    with node,collect(distinct rx1.rxcui) as genericRxcui,rxcuis where size(rxcuis)=size(genericRxcui)   return distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,genericRxcui")  
	List<PVMasterQueryResult> getCombinationDrugs(List<Long> pvid);
*/	
	
	
	
	@Query("match(pv:PVMaster) where pv.pvid IN $pvid  "   
			   + "match(rxm:MICARxNorms{rxcui:pv.rxcui})-[]-(rx:IngredientRxcui)  with  collect( distinct  rx.ingredientRxcui)  as rxcuis,pv "
			   + "match(node:PVMaster)  where   node.name <> pv.name and node.marketing_status= $status "
			   + " match(rxm1:MICARxNorms{rxcui: node.rxcui})-[]-(rx1:IngredientRxcui)  with  collect( distinct  rx1.ingredientRxcui)  as genericRxcui ,node,rxcuis where size(rxcuis)=size(genericRxcui) and apoc.coll.disjunction(rxcuis, genericRxcui)= []   return distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,genericRxcui")  
		List<PVMasterQueryResult> getCombinationDrugWithParameters(List<Long> pvid,String status);

	

	
	@Query("match(pv:PVMaster) where pv.pvid IN $pvid  "   
			   + "match(rxm:MICARxNorms{rxcui:pv.rxcui})-[]-(rx:IngredientRxcui)  with  collect( distinct  rx.ingredientRxcui)  as rxcuis,pv "
			   + "match(node:PVMaster)  where   node.name <> pv.name "
			   + " match(rxm1:MICARxNorms{rxcui: node.rxcui})-[]-(rx1:IngredientRxcui)  with  collect( distinct  rx1.ingredientRxcui)  as genericRxcui ,node,rxcuis where size(rxcuis)=size(genericRxcui) and apoc.coll.disjunction(rxcuis, genericRxcui)= []   return distinct  node.pvid as pvid , node.name as name, node.description as description, node.strength as strength,node.strength_units as strength_units,node.route as route,node.dosage as dosage,node.rxcui as rxcui, node.marketing_status as marketing_status,genericRxcui")  
				List<PVMasterQueryResult> getCombinationDrugs(List<Long> pvid);

	



}
