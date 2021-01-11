/**
 * 
 */
package com.advinow.mica.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Govinda Reddy
 *
 */

public class ObjectConcept  {

	@JsonInclude(Include.NON_NULL)
	String active;

	@JsonInclude(Include.NON_NULL)
	String nodetype;


	@JsonInclude(Include.NON_NULL)
	String effectiveTime;

	@JsonInclude(Include.NON_NULL)
	String FSN;

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private String sctid;
		
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || id == null || getClass() != o.getClass())
			return false;
	    
		ObjectConcept entity = (ObjectConcept) o;

		if (!id.equals(entity.id))
			return false;
			
		return true;
	}

	@Override
	public int hashCode() {
		return (id == null) ? -1 : id.hashCode();
	}

	/**
	 * @return the sctid
	 */
	public String getSctid() {
		return sctid;
	}

	/**
	 * @param sctid the sctid to set
	 */
	public void setSctid(String sctid) {
		this.sctid = sctid;
	}


	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the nodetype
	 */
	public String getNodetype() {
		return nodetype;
	}

	/**
	 * @param nodetype
	 *            the nodetype to set
	 */
	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	

	

	/**
	 * @return the effectiveTime
	 */
	public String getEffectiveTime() {
		return effectiveTime;
	}

	/**
	 * @param effectiveTime
	 *            the effectiveTime to set
	 */
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	/**
	 * @return the fSN
	 */
	public String getFSN() {
		int firstIndex = FSN.indexOf('(');
		int lastIndex = FSN.lastIndexOf('(');
		if (firstIndex != lastIndex) {
			return FSN.substring(0, lastIndex);
		}
		return FSN;
	}

	/**
	 * @param fSN
	 *            the fSN to set
	 */
	public void setFSN(String fSN) {
		FSN = fSN;
	}

	/**
	 * @return the objectConceptId
	 */
	/**
	 * @return the objectConceptId
	 */
	
}
