package com.advinow.mica.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.LabOrdersRef;
import com.advinow.mica.domain.SymptomLabOrders;
import com.advinow.mica.domain.SymptomTemplate;
import com.advinow.mica.domain.queryresult.SymptomsLabOrders;
import com.advinow.mica.exception.DataNotFoundException;
import com.advinow.mica.repositories.LabOrdersRefRepository;
import com.advinow.mica.repositories.SymptomTemplateRepository;
import com.advinow.mica.services.SymptomLabsService;

@Service
@Retry(name = "neo4j")
public class SymptomLabsServiceImpl implements SymptomLabsService {

	@Autowired
	SymptomTemplateRepository symptomTemplateRepository;
	
	@Autowired
	LabOrdersRefRepository labOrdersRefRepository;

	@Override
	public List<LabOrdersRef> getAllLabOrders() {		
	return	labOrdersRefRepository.getAllLabOrders();
	}

	@Override
	public SymptomsLabOrders addOrUpdate(SymptomsLabOrders labOrders) {
		// TODO Auto-generated method stub
		SymptomsLabOrders  orders = null;
		String symptomID = labOrders.getSymptomID();
		if(symptomID ==  null) {
			throw new DataNotFoundException("Symptom ID is null");
		}
		
		Set<SymptomLabOrders>  labsOrderedModel = new HashSet<SymptomLabOrders>();

		if( labOrders.getLabsOrdered() != null && ! labOrders.getLabsOrdered().isEmpty()) {

			for ( String labOrderName : labOrders.getLabsOrdered() ) {
				SymptomLabOrders labOrder = new SymptomLabOrders();
				labOrder.setName(labOrderName);
				labsOrderedModel.add(labOrder);
			}
		}
		SymptomTemplate st = symptomTemplateRepository.findLabSymptoms(symptomID);
		if(st==null) {
			throw new DataNotFoundException(labOrders.getSymptomID() + " is not a lab symptom. ");
		} else {
			SymptomTemplate dbSymptomTemplate=symptomTemplateRepository.findByCode(symptomID,2);
			if(dbSymptomTemplate ==null ){
				throw new DataNotFoundException("No symptom data found for the SymptomID:"+ symptomID);
			} else {
				symptomTemplateRepository.deleteSymptomLabOrder(symptomID);
				dbSymptomTemplate.setLabsOrdered(labsOrderedModel);
				SymptomTemplate finalSt = symptomTemplateRepository.save(dbSymptomTemplate);
				if(finalSt != null) {
					orders = new SymptomsLabOrders();
					orders.setSymptomID(finalSt.getCode());
					List<String> symptoms = finalSt.getLabsOrdered().stream().filter(s->s.getName() != null).map(SymptomLabOrders::getName).collect(Collectors.toList());
					orders.setLabsOrdered(symptoms);
				}
			}

		}
		return orders;
	}

	@Override
	public SymptomsLabOrders getLabsOrdered(String symptomID) {
		return symptomTemplateRepository.getLabOrdersBySymptomID(symptomID);
	}

	@Override
	public void deleteLabOrder(String symptomID) {
		symptomTemplateRepository.deleteSymptomLabOrder(symptomID);
		
	}

}
