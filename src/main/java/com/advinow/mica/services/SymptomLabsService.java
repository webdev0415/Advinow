package com.advinow.mica.services;

import java.util.List;

import com.advinow.mica.domain.LabOrdersRef;
import com.advinow.mica.domain.queryresult.SymptomsLabOrders;

public interface SymptomLabsService {

	List<LabOrdersRef> getAllLabOrders();

	SymptomsLabOrders addOrUpdate(SymptomsLabOrders labOrders);

	SymptomsLabOrders getLabsOrdered(String symptomID);

	void deleteLabOrder(String symptomID);

}
