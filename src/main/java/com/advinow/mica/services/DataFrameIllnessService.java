package com.advinow.mica.services;


import com.advinow.mica.model.IllnessStatusModel;
import com.advinow.mica.model.dataframe.DataFrameModel;
/**
 * 
 * @author Govinda Reddy
 *
 */
public interface DataFrameIllnessService {
	
	public IllnessStatusModel updateDataFrameInfo(DataFrameModel dataFrameModel);

	public DataFrameModel getSymptomsIllnesses();

	public DataFrameModel getAllAciveIllnesses();
	
	public DataFrameModel getAllAciveSymptoms();
	
	
	

}
