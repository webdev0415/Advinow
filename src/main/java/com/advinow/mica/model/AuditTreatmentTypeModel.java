/**
 * 
 */
package com.advinow.mica.model;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Developer
 *
 */
public @Data class AuditTreatmentTypeModel {
	
	@JsonInclude(Include.NON_NULL)
	private String drugName;
	
	@JsonInclude(Include.NON_NULL)
	private Integer typeDescID;	
	
	@JsonInclude(Include.NON_EMPTY)
	private List<AuditSourceModel> sourceInfo;


}
