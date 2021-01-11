package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.TreatmentSourceInfo;
import com.advinow.mica.model.IllnessSourcesModel;
import com.advinow.mica.model.TreatmentAuditSourcesModel;
import com.advinow.mica.model.TreatmentMainDocModel;
import com.advinow.mica.model.TreatmentMainModel;
import com.advinow.mica.model.TreatmentStatusModel;

/**
 * 
 * @author Govinda Reddy
 *
 */
public interface TreatmentService {
	
	/**
     *  
     * @param illnessTreatmentModel
     * @return
     */
	TreatmentStatusModel creatreatTrementsForIllness(TreatmentMainModel illnessTreatmentModel);

	/**
	 *  
	 * @param treatmentMainModel
	 * @return
	 */
	TreatmentStatusModel creatreatTrementsForSymptom(TreatmentMainModel treatmentMainModel);
   
	/**
	 * 
	 * @return
	 */
	List<TreatmentMainModel> getIllnessTreatments();
	
	List<TreatmentMainModel> getSymptomsTreatments();
	
	TreatmentMainModel getTreatmentBySymptomID(String symptomID);

	TreatmentMainModel getTreatmentByICD10Code(String icd10Code);

	void deleteSymptomBycode(String symptomID);

	void deleteTreatmentIllnessByCode(String icd10Code);

	TreatmentMainModel getTreatmentByIcd10CodeWithNoDesc(String icd10Code);

	TreatmentMainModel getTreatmentBySymptomIDWithNoDesc(String symptomID);	
	
	TreatmentMainDocModel getTreatmentTypesByIcd10Code(String icd10Code);

	TreatmentMainDocModel getTreatmentTypesBySymptomID(String symptomID);

	List<TreatmentSourceInfo> getTreatmentSourcesForGivenIllness(String icd10Code);

	String updateTreatmentSources(TreatmentAuditSourcesModel illnessSourcesModel);

	List<TreatmentSourceInfo> getTreatmentSourcesForGivenSymptom(String upperCase);
}
