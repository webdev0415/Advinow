package com.advinow.mica.model.cache;

import java.io.Serializable;
import java.util.Map;


@SuppressWarnings("serial")
public class CacheGroupsTime  implements Serializable {
	
	 Map<String, Long> groupsTimeMap;

	/**
	 * @return the groupsTimeMap
	 */
	public Map<String, Long> getGroupsTimeMap() {
		return groupsTimeMap;
	}

	/**
	 * @param groupsTimeMap the groupsTimeMap to set
	 */
	public void setGroupsTimeMap(Map<String, Long> groupsTimeMap) {
		this.groupsTimeMap = groupsTimeMap;
	}

}
