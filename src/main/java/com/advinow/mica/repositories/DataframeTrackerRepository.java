package com.advinow.mica.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.advinow.mica.domain.DataframeTracker;

/**
 * 
 * @author Govinda Reddy
 *
 */

@Repository
public interface DataframeTrackerRepository extends Neo4jRepository<DataframeTracker,Long> {

	 
	@Query("Match (df:DataframeTracker) Return df Order by ID(df) desc Limit 1")
	DataframeTracker findMostRecentDataframe();
	
}
