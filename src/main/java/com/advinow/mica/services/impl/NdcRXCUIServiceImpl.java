/**
 * 
 */
package com.advinow.mica.services.impl;


import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import com.advinow.mica.model.NdcRxNorm;
import com.advinow.mica.model.RxNorm;
import com.advinow.mica.services.NdcRXCUIService;
import com.advinow.mica.services.RXCUIService;

/**
 * @author Developer
 *
 */
@Service
@Retry(name = "neo4j")
public class NdcRXCUIServiceImpl implements NdcRXCUIService {

	protected Logger logger = LoggerFactory.getLogger(getClass());


	/* (non-Javadoc)
	 * @see com.advinow.mica.services.RCUXIService#download()
	 */
	@Override
	public void downloadrxforndc() {
		// TODO Auto-generated method stub
		callExternalService();
	}

	 @SuppressWarnings({"rawtypes", "unchecked"})
	   private static ColumnPositionMappingStrategy setColumMapping()
	   {
		  ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
	      strategy.setType(NdcRxNorm.class);
	      String[] columns = new String[] { "productndc", "rxcui","load"};
	      strategy.setColumnMapping(columns);
	      return strategy;
	   }
	 
	 
	 
		@SuppressWarnings("unchecked")
		private void callExternalService(){

			RestTemplate restTemplate = new RestTemplate();
			try {
				// String csv_result = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp_result.csv";
				 String csv_result = "C:\\advinow\\mica\\Dataframe_1_7\\FDASearch\\conf\\ndc\\ndc_rxcodes_v1_result.csv";
				CSVWriter writer = new CSVWriter(new FileWriter(csv_result, true));
			      String[] headerRecord = new String[] { "rxcui", "ingredientRxcui"};
			      writer.writeNext(headerRecord);

			      @SuppressWarnings("rawtypes")
				CsvToBean csv = new CsvToBean();
			       
			   //   String csvFilename = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp.csv";
				 String csvFilename = "C:\\advinow\\mica\\Dataframe_1_7\\FDASearch\\conf\\ndc\\ndc_rxcodes.csv";
			     
				 CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			       
			      //Set column mapping strategy
			      List<NdcRxNorm> list = csv.parse(setColumMapping(), csvReader);
			      for (NdcRxNorm rxNorm : list) {
			    	  String productNdc=  rxNorm.getProductndc();
					if(productNdc !=  null) {
		    			  logger.info("productNdc :::" +  productNdc);
		    				String baseUrl = "https://rxnav.nlm.nih.gov/REST/rxcui.json?idtype=NDC&id=";
							StringBuffer base_url =  new StringBuffer(baseUrl).append(productNdc.trim());
					String	 response = restTemplate.getForObject(base_url.toString(), String.class);
					JSONObject jsonObject= new JSONObject(response);
					 boolean key = jsonObject.isNull("idGroup");
					 if(!key) {
					JSONObject results = jsonObject.getJSONObject("idGroup");
			
					if(results != null) {
						 Boolean keyIn= results.isNull("rxnormId");
						if(!keyIn) {
					JSONArray rxnormId = results.getJSONArray("rxnormId");
					if(rxnormId != null) {
					logger.info("rxcui :::" +  rxnormId.getInt(0));
				
					for(int i=0;i<rxnormId.length();i++) {
						writer.writeNext(new String[]{productNdc, rxnormId.getString(i)});
					}
					}
					}
					else {
						logger.info("No rxcui  found for  :::" +  productNdc);
						writer.writeNext(new String[]{productNdc, "NONDC"});
						}
					}
					 } else {
							writer.writeNext(new String[]{productNdc, "NONDC"});
							logger.info("No rxcui  found for  :::" +  productNdc);
						
					 }
				}
			      }
			      
			      writer.close();
			      
			} catch (Exception e) {

				e.printStackTrace();

			}
		
		
		}

	 
	
	
}