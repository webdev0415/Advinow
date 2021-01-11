/**
 * 
 */
package com.advinow.mica.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DataStore;
import com.advinow.mica.domain.queryresult.GenericQueryResultEntity;

/**
 * @author Govinda Reddy
 *
 */
@Repository
public interface DataStoreRepository extends Neo4jRepository<DataStore,Long>{
					
	@Query("MATCH (st:SymptomTemplate{code:$symptomID}),(dk:DataKeys{name:st.multipleValues})-[dkr]-(ds:DataStore)  where ds.m_antithesis is null  or ds.displayListValue is null  return  ds.name")
	List<String> findBadMultiplier(String symptomID);
	
	@Query("MATCH(dk:DataKeys{name:$multiplier})-[dkr]-(ds:DataStore)  return  ds.name")
	List<String> findListValues(String multiplier);
	
	@Query("MATCH(ds:DataStore)  return  ds")
	Set<DataStore> findAllDataStores();
	
/*	@Query("WITH {0} AS pairs UNWIND pairs AS p  MATCH(ds:DataStore) where ds.code =p.code  SET ds.m_antithesis=p.m_antithesis  return ds") 
	List<DataStore> updateDataFrameDescriptors(Collection<Map<String, Object>> dataStores);*/
	
	
	@Query("match(dk:DataKeys)-[dkr]-(ds:DataStore)  return dk.code as optionKey, collect({code:ds.code,name:ds.name}) as literalMap")
	List<GenericQueryResultEntity>  getAllMultipliers();

	
	@Query("MATCH (st:SymptomTemplate{code:$symptomID}), (dk:DataKeys{name:st.multipleValues})-[dkr]-(ds:DataStore)  where ds.m_antithesis is null  or ds.displayListValue is null  or ds.lowerLimit is null or ds.lowerLimit is null  OR  ds.lowerLimitCondition is null   return  ds.name")
	List<String> findBadRangeMultiplier(String symptomID);
	
			
}
