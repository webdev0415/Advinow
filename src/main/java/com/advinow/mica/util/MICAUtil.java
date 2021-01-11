package com.advinow.mica.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;

public class MICAUtil {
	
	public static final String SIMPLE = "Simple";

	public static final String SIMPLE_TIME = "SimpleTime";

	public static final String LIST  = "List";

	public static final String LIST_TIME  = "ListTime";

	public static final String IMAGE  = "Image";
		
	public static final String DAY="Less than 1 Day";
	
	public static final String ONE_TO_THREE_DAYS="1-3 Days";
	
	public static final String WEEK="Less than 1 week";
	
	public static final String ONE_TO_THREE_WEEKS="1-3 Weeks";
	
	public static final String MONTH="Less than 1 Month";
	
	public static final String ONE_TO_THREE_MONTHS="1-3 Months";	
	
	public static final String Longer="Longer";
	
	public static final List<String> DATA_STORE_SIMPLE = Arrays.asList(new String[]{"Likelihood"});

	public static final List<String> DATA_STORE_SIMPLE_TIME = Arrays.asList(new String[]{"Likelihood","TimeUnit"});

	public static final List<Long> BASIC_SYMPTOMS = Arrays.asList(new Long[]{600L,8L,800L});
	
	protected  static Logger logger = LoggerFactory.getLogger(MICAUtil.class);
	
		
 public static  String  getAdvinowDateFormat()
  {
	SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmm");
	return localDateFormat.format(new Date());
  }
	
public  static  Map<String,Map<String,List<String>>>  populateDataStore(Iterable<DataKeys> dataKeys){
	
	Map<String,Map<String,List<String>>> dataStore = new Hashtable<String, Map<String,List<String>>>();	
	Iterator<DataKeys> dataKeyItr = dataKeys.iterator();
	while(dataKeyItr.hasNext()){		
		DataKeys dbDataKeys = dataKeyItr.next();	
		Set<DataStore> dataStoreList = dbDataKeys.getDataStoreList();
		Map<String,List<String>> multiplier = new Hashtable<String,List<String>>();	
		List<String> values = new ArrayList<String>();
		if(dataStoreList != null &&  !dataStoreList.isEmpty() ){
			values.addAll(getMultiplierValues(dataStoreList));				
		}
		multiplier.put(dbDataKeys.getTitle(), values);
		dataStore.put(dbDataKeys.getName(), multiplier);
		
	}
	return dataStore;
	
	
}


private static List<String> getMultiplierValues(Set<DataStore> dataStoreList) {
	List<String> values = new ArrayList<String>();
       Iterator<DataStore> dbDataStoreItr = dataStoreList.iterator();
       while(dbDataStoreItr.hasNext()){
    	  DataStore dbDataSore = dbDataStoreItr.next();
    	  values.add(dbDataSore.getName());    	 
       }
       
    		
	return values;
}

public static double round(Double d, int decimalPlace) {
    return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).doubleValue();
}


}
