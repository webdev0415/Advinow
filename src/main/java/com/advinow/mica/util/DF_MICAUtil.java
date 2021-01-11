package com.advinow.mica.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advinow.mica.domain.DataKeys;
import com.advinow.mica.domain.DataStore;

public class DF_MICAUtil {



	public static final String SIMPLE = "Simple";

	public static final String SIMPLE_TIME = "SimpleTime";

	public static final String LIST  = "List";

	public static final String LIST_TIME  = "ListTime";

	public static final String IMAGE  = "Image";

	//<1D should be saved a 1-24 hours
	//2-3D should be saved as 25-72 hours
	//<1W should be saved as 1-7 days
	//2-3W should be saved as 8-21 days
	//<1M should be saved as 1-31 days
	//2-3M should be saved as 32-90 days
	//Longer should be saved as 91-1200

	// Less than 1 day
	//2-3 Days
	//Less than 1 week
	//2-3 Weeks
	//Less than 1 month
	//2-3 Months
	//More than 3 months
	//Longer



	private static final String DAY="Less than 1 day";

	private static final String TWO_TO_THREE_DAYS="2-3 Days";

	private static final String WEEK="Less than 1 week";

	private static final String TWO_TO_THREE_WEEKS="2-3 Weeks";

	private static final String MONTH="Less than 1 month";

	private static final String TWO_TO_THREE_MONTHS="2-3 Months";

	private static final String Longer="Longer";


	public static final List<Long> BASIC_SYMPTOMS = Arrays.asList(new Long[]{600L,8L,800L});

	public static final List<String> TIME_PERIODS = Arrays.asList(new String[]{DAY,TWO_TO_THREE_DAYS,WEEK,TWO_TO_THREE_WEEKS,MONTH,TWO_TO_THREE_MONTHS,Longer});

	public static final String HOURS="PT1H";
	public static final String DAYS="PT24H";
	public static final String WEEKS="PT168H";
	public static final String MONTHS="PT730H29M6S";
	public static final String YEARS="PT8765H49M12S";

	public static final String PAIN_THROAT_CODE="PAIN_BODYPART04";

	public static final String PAIN_BACK_OF_THROAT_CODE="PAIN_BODYPART58";


	protected  static Logger logger = LoggerFactory.getLogger(DF_MICAUtil.class);

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




public static String getCommaSeparatedString(List<String> items) {
String citiesCommaSeparated = items.stream()
.map(String::trim)
.collect(Collectors.joining(","));
return citiesCommaSeparated;
}
}
