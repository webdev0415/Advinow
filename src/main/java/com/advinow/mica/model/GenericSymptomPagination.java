package com.advinow.mica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Govinda Reddy
 *
 */
public class GenericSymptomPagination {

	@JsonInclude(Include.NON_NULL)
	List <GenericSymptomModel> content;	
		
	
	/**
	 * Returns whether the current Slice is the first one.
	 */
	@JsonInclude(Include.NON_NULL)
    Boolean first;
	
	/**
	 * Returns the number of the current Slice. Is always non-negative.
	 */
	@JsonInclude(Include.NON_NULL)
	Integer pagenumber;
	
	/**
	 * Returns the number of total pages.
	 */
	@JsonInclude(Include.NON_NULL)
	Integer totalPages;
	
	
	/**
	 * Returns the size of the Slice.
	 */
	@JsonInclude(Include.NON_NULL)
	Integer pageSize;
	
	/**
	 * Returns the number of records.
	 */
	@JsonInclude(Include.NON_NULL)
	Integer totalElements;
	
	/**
	 * Returns the total amount of elements in the page or slice.
	 */
	@JsonInclude(Include.NON_NULL)
	Integer elementsInPage;
	
	
	/**
	 * Returns whether the current Slice is the last one.
	 * 
	 */
	@JsonInclude(Include.NON_NULL)
    Boolean last;


	public List<GenericSymptomModel> getContent() {
		return content;
	}


	public void setContent(List<GenericSymptomModel> content) {
		this.content = content;
	}


	public Boolean getFirst() {
		return first;
	}


	public void setFirst(Boolean first) {
		this.first = first;
	}



	public Integer getTotalPages() {
		return totalPages;
	}


	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getTotalElements() {
		return totalElements;
	}


	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}


	public Integer getElementsInPage() {
		return elementsInPage;
	}


	public void setElementsInPage(Integer elementsInPage) {
		this.elementsInPage = elementsInPage;
	}


	public Boolean getLast() {
		return last;
	}


	public void setLast(Boolean last) {
		this.last = last;
	}


	public Integer getPagenumber() {
		return pagenumber;
	}


	public void setPagenumber(Integer pagenumber) {
		this.pagenumber = pagenumber;
	}
	
}
