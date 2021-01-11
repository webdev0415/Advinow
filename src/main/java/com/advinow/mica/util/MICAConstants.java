package com.advinow.mica.util;

import java.util.Arrays;
import java.util.List;

public interface MICAConstants {

	static final String APPROVED = "APPROVED";
	
	static final String COMPLETE = "COMPLETE";
	
	static final String READYFORREVIEW = "READY-FOR-REVIEW";
	
	static final String PENDING = "PENDING";
	
	static final String REJECTED = "REJECTED";
	
	static final String PROTECTED = "PROTECTED";

	static final String SIMPLE = "Simple";

	static final String SIMPLE_TIME = "SimpleTime";

	static final String LIST = "List";

	static final String LIST_TIME = "ListTime";

	static final String IMAGE = "Image";
	
	
	// MITA CONSTANTS
	static final String MITA_COLLECTOR = "collector";
	
	static final String MITA_REVIEWER ="reviewer";
	
	static final String MITA_APPROVED = "approved";
	
	static final String MITA_COMPLETE = "complete";
	
	static final String MITA_REJECTED = "rejected";
	
	static final String MITA_PENDING =   "pending";
	
	static final String MITA_READY_FOR_REVIEW = "assignedreview";
	
	// END MITA CONSTANTS
	
	
	static final List<String> NO_MITA_CALL = Arrays
			.asList(new String[] { "PENDING", "READY-FOR-REVIEW"});
	
	

	static final List<String> DATA_STORE_SIMPLE = Arrays
			.asList(new String[] { "Likelihood" });

	static final List<String> DATA_STORE_SIMPLE_TIME = Arrays
			.asList(new String[] { "Likelihood", "TimeUnit" });

	static final String SOURCE = "MICA";
	
	

	static final List<String> LANGUAGE_CODES = Arrays
			.asList(new String[] { "en", "es"});
	

	static final String ENGLISH =   "en";
	
	static final String SPANISH =   "es";
	
	static final Integer sourceSize = 5;
	
}
