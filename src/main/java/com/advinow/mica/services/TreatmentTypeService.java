/**
 * 
 */
package com.advinow.mica.services;

import com.advinow.mica.model.TreatmentStatusModel;
import com.advinow.mica.model.TreatmentTypeRefGroups;
import com.advinow.mica.model.TreatmentTypeRefModel;

/**
 * @author Govinda Reddy
 *
 */
public interface TreatmentTypeService {
    /**
     * 
     * @return
     */
	TreatmentTypeRefGroups getAllTreatmentTypes();
	/**
	 * 
	 * @param typeID
	 * @return
	 */
	TreatmentTypeRefModel	getTreatmentByType(Integer typeID);
    /**
     * 
     * @param treatmentTypeModel
     * @return
     */
	TreatmentStatusModel createtreatTrementType(TreatmentTypeRefModel treatmentTypeModel);
    

}
