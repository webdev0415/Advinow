/**
 * 
 */
package com.advinow.mica.model.dataframe;

import java.util.List;

import lombok.Data;

/**
 * @author Govinda Reddy
 *
 */
public @Data class DataframeBaddata {

String icd10Code;	
String	code;
String type;
List<String> invalidmultiplier;
List<String> multiplier;
Integer number;
String error;
String timeFrame;
public Long  dsId;

}
