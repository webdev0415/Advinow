/**
 * 
 */
package com.advinow.mica.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import io.github.resilience4j.retry.annotation.Retry;

import com.advinow.mica.domain.RawSymptomTemplate;
import com.advinow.mica.exception.DataInvalidException;
import com.advinow.mica.model.GenericSymptomModel;
import com.advinow.mica.model.GenericSymptomPagination;
import com.advinow.mica.repositories.RawSymptomTemplateRepository;
import com.advinow.mica.services.GenericRawSymptomService;

/**
 * @author Govinda Reddy
 *
 */
@Service
@Retry(name = "neo4j")
public class GenericRawSymptomServiceImpl implements GenericRawSymptomService {

	@Autowired
	RawSymptomTemplateRepository  rawSymptomTemplateRepository;

	@Override
	public GenericSymptomPagination getRawSymptomsWithPaging(String source,
			String name, Integer skip, Integer limit) {
		
		GenericSymptomPagination  symptomsPages = new GenericSymptomPagination();
		
		Integer totalRecords = null;
		List<RawSymptomTemplate> symptomsDbPages  = null;
	
	//	PageRequest pageable = null;

		if(skip != null &&  limit !=  null) {
		//	pageable =  PageRequest.of(skip - 1, limit, Sort.Direction.ASC, "st.code");		
		} else{
			skip =0;
			limit = 25;
		//	pageable =  PageRequest.of(0, 25, Sort.Direction.ASC, "st.code");	
		}
			if(name != null) {
			if(name.length() < 3) {
				throw new  DataInvalidException("Symptom name should be greater than 3 characters.");
			}
			
			symptomsDbPages = rawSymptomTemplateRepository.findBySourceAndName(source.toUpperCase(), name.toUpperCase(),skip,limit);
			totalRecords = rawSymptomTemplateRepository.findBySourceAndNameCount(source.toUpperCase(), name.toUpperCase());
		} else{
			symptomsDbPages = rawSymptomTemplateRepository.findBySource(source.toUpperCase(),skip,limit);
			totalRecords = rawSymptomTemplateRepository.findBySourceCount(source.toUpperCase());
		}
		
	
		
		
     	if(symptomsDbPages != null) {
			//List<RawSymptomTemplate> slices = symptomsDbPages.getContent();
			//if(slices != null ){
				List <GenericSymptomModel> content = new ArrayList<GenericSymptomModel>();
				for (RawSymptomTemplate slice : symptomsDbPages) {
					GenericSymptomModel genericSymptomModel = new GenericSymptomModel();	
					genericSymptomModel.setName(slice.getName());
					genericSymptomModel.setCode(slice.getCode());
					genericSymptomModel.setBodyPart(slice.getBodyPart());
					genericSymptomModel.setSource(slice.getSource());
					genericSymptomModel.setName(slice.getName());
					genericSymptomModel.setDataStoreTemplates(slice.getDataStoreTemplates());
					genericSymptomModel.setCriticality(slice.getCriticality());
					genericSymptomModel.setTreatable(slice.getTreatable());
				//	genericSymptomModel.setQuestionText(slice.getQuestionText());
					genericSymptomModel.setQuestion(slice.getQuestionText());
					genericSymptomModel.setAntithesis(slice.getAntithesis());
					genericSymptomModel.setDisplayOrder(slice.getDisplayOrder());
					genericSymptomModel.setDisplaySymptom(slice.getDisplaySymptom());
					genericSymptomModel.setDisplayDrApp(slice.getDisplayDrApp());
					
            		content.add(genericSymptomModel);
				}
				symptomsPages.setContent(content);
			//}
				
			if(symptomsPages.getContent() != null && ! symptomsPages.getContent().isEmpty()) {
			symptomsPages.setFirst(true);
			symptomsPages.setLast(false);
			symptomsPages.setPagenumber(skip);
			symptomsPages.setElementsInPage(limit);
			symptomsPages.setPageSize(limit);
			symptomsPages.setTotalElements(totalRecords);
				}
		}
     	
     	    	
		return symptomsPages;

	}

}
