/**
 * 
 */
package com.advinow.mica.services;

import com.advinow.mica.model.GenericSymptomPagination;

/**
 * @author govind
 *
 */
public interface GenericRawSymptomService {

	GenericSymptomPagination getRawSymptomsWithPaging(String source,
			String name, Integer page, Integer size);

}
