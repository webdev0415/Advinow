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

import com.advinow.mica.model.RxNorm;
import com.advinow.mica.services.RXCUIService;

/**
 * @author Developer
 *
 */
@Service
@Retry(name = "neo4j")
public class RXCUIServiceImpl implements RXCUIService {

	protected Logger logger = LoggerFactory.getLogger(getClass());


	/* (non-Javadoc)
	 * @see com.advinow.mica.services.RCUXIService#download()
	 */
	@Override
	public void downloadrx() {
		// TODO Auto-generated method stub
		callExternalRxcuiIngredientService();
	}

	 @SuppressWarnings({"rawtypes", "unchecked"})
	   private static ColumnPositionMappingStrategy setColumMapping()
	   {
	      ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
	      strategy.setType(RxNorm.class);
	      String[] columns = new String[] { "rxcui", "ingredientRxcui","load"};
	      strategy.setColumnMapping(columns);
	      return strategy;
	   }
	 
	 
	 
		private void callExternalRxcuiIngredientService(){

			RestTemplate restTemplate = new RestTemplate();
			try {
				// String csv_result = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp_result.csv";
				 String csv_result = "C:\\advinow\\mica_new\\development\\mica_resources\\neo4jcsv\\rx\\rxcui_treatments_v4_result.csv";
				CSVWriter writer = new CSVWriter(new FileWriter(csv_result, true));
			      String[] headerRecord = new String[] { "rxcui", "ingredientRxcui"};
			      writer.writeNext(headerRecord);

			      @SuppressWarnings("rawtypes")
				CsvToBean csv = new CsvToBean();
			       
			   //   String csvFilename = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp.csv";
				 String csvFilename = "C:\\advinow\\mica_new\\development\\mica_resources\\neo4jcsv\\rx\\rxcui_treatments_v4.csv";
			     
				 CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			       
			      //Set column mapping strategy
			      @SuppressWarnings("unchecked")
				List<RxNorm> list = csv.parse(setColumMapping(), csvReader);
			      for (RxNorm rxNorm : list) {
			    	  String rxcui=  rxNorm.getRxcui();
		
			    		logger.info("rxcui :::" +  rxcui);
			    		
			    		// https://rxnav.nlm.nih.gov/REST/rxclass/class/similarByRxcuis?rxcuis=5640+5640&top=1
						
				String baseUrl = "https://rxnav.nlm.nih.gov/REST/rxclass/class/similarByRxcuis?rxcuis=";

				
				StringBuffer base_url =  new StringBuffer(baseUrl).append(rxcui + "+" + rxcui).append("&top=1");
				if(rxcui !=  null) {
					String	 response = restTemplate.getForObject(base_url.toString(), String.class);
					JSONObject jsonObject= new JSONObject(response);
					 boolean key = jsonObject.isNull("similarityMember");
					 if(!key) {
					JSONObject results = jsonObject.getJSONObject("similarityMember");
					if(results != null) {
						 Boolean keyIn= results.isNull("ingredientRxcui");
						if(!keyIn) {
					JSONArray ingredients = results.getJSONArray("ingredientRxcui");
					if(ingredients != null) {
					logger.info("Active Ingredients :::" +  ingredients.getInt(0));
				
					for(int i=0;i<ingredients.length();i++) {
						writer.writeNext(new String[]{rxcui, ingredients.getString(i)});
					}
						}
						} else {
							logger.info("No Active Ingredients found for  :::" +  rxcui);
						}
					}
					 }/* else {

							logger.info("No Active Ingredients found for  :::" +  rxcui);
						
					 }*/
				}
			      }
			      
			      writer.close();
			      
			} catch (Exception e) {

				e.printStackTrace();

			}
		
		
		}
	 
	 
		 @SuppressWarnings({"rawtypes", "unchecked"})
		   private static ColumnPositionMappingStrategy setColumMappingForNDCResults()
		   {
		      ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
		      strategy.setType(RxNorm.class);
		      String[] columns = new String[] { "rxcui", "ndc"};
		      strategy.setColumnMapping(columns);
		      return strategy;
		   }

		@Override
		public void downloadNDCforRxcui() {
			callExternalDownloadNDCService();
			
		}
		 
		
		
		
		private void callExternalDownloadNDCService(){

			RestTemplate restTemplate = new RestTemplate();
			try {
				// String csv_result = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp_result.csv";
				 String csv_result = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\neo4jcsv\\rx\\rxcui_pv_ndc_result.csv";
				CSVWriter writer = new CSVWriter(new FileWriter(csv_result, true));
			      String[] headerRecord = new String[] { "rxcui", "ndc"};
			      writer.writeNext(headerRecord);

			      @SuppressWarnings("rawtypes")
				CsvToBean csv = new CsvToBean();
			       
			   //   String csvFilename = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\src\\main\\resources\\rxcui_nlp.csv";
				 String csvFilename = "C:\\advinow\\mica\\Dataframe_1_7\\2070Services\\neo4jcsv\\rx\\rxcui_pv_v1.csv";
			     
				 CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
			       
			      //Set column mapping strategy
			      @SuppressWarnings("unchecked")
				List<RxNorm> list = csv.parse(setColumMapping(), csvReader);
			      for (RxNorm rxNorm : list) {
			    	  String rxcui=  rxNorm.getRxcui();
		
			    		// https://rxnav.nlm.nih.gov/REST/rxcui/1668240/ndcs.json
			    		
				String baseUrl = "https://rxnav.nlm.nih.gov/REST/rxcui/";

				
				StringBuffer base_url =  new StringBuffer(baseUrl).append(rxcui).append("/ndcs.json");
				
				
				
				if(rxcui !=  null) {
					String	 response = restTemplate.getForObject(base_url.toString(), String.class);
					JSONObject jsonObject= new JSONObject(response);
					
					JSONObject ndcGroup = jsonObject.getJSONObject("ndcGroup");
					
					
					
					
					 boolean key = ndcGroup.isNull("ndcList");
					 
					 if(!key) {
					JSONObject results = ndcGroup.getJSONObject("ndcList");
					if(results != null) {
						 Boolean keyIn= results.isNull("ndc");
						if(!keyIn) {
					JSONArray ndcCodes = results.getJSONArray("ndc");
					logger.info("ndcCodes " + ndcCodes);
					if(ndcCodes != null) {
					logger.info("NDC found fo :::" +  ndcCodes.getInt(0));
				
					for(int i=0;i<ndcCodes.length();i++) {
						writer.writeNext(new String[]{rxcui, ndcCodes.getString(i)});
					}
						}
						} else {
							logger.info("No NDC found for  :::" +  rxcui);
						}
					}
					 }/* else {

							logger.info("No Active Ingredients found for  :::" +  rxcui);
						
					 }*/
				}
			      }
			      
			      writer.close();
			      
			} catch (Exception e) {

				e.printStackTrace();

			}
		}	
				
		
	
}