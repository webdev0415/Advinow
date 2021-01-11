package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SnomedCodeModel {
	
	@JsonInclude(Include.NON_NULL)
	private List<Long> snomedCodes;
	
	@JsonInclude(Include.NON_NULL)
	private String snomedName;

	/**
	 * @return the snomedName
	 */
	public String getSnomedName() {
		return snomedName;
	}

	/**
	 * @param snomedName the snomedName to set
	 */
	public void setSnomedName(String snomedName) {
		this.snomedName = snomedName;
	}

	/**
	 * @return the snomedCodes
	 */
	public List<Long> getSnomedCodes() {
		return snomedCodes;
	}

	/**
	 * @param snomedCodes the snomedCodes to set
	 */
	public void setSnomedCodes(List<Long> snomedCodes) {
		this.snomedCodes = snomedCodes;
	}
	

}
