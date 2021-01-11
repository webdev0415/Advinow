package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.model.BodyParts;

public interface MICAGenericService {
	   
	/**
	 * 
	 * @param keyName
	 * @return
	 */
	public DataKeys getDataKeysByName(String keyName) ;
	
	/**
	 * 
	 * 
	 * @return
	 */
	public BodyParts getBodyParts(String LanguageCode);

	/**
	 * 
	 * @return
	 */
	public String resetCollectorIllnessData(Integer userId);

	/**
	 * 
	 * @return
	 */
	public String resetReviewerIllnessData(Integer userId);
	

	public List<BodyParts> getPainSwellingBodyParts(String string);
	

	public BodyParts getAllCategoriesBodyparts(String string);

}
