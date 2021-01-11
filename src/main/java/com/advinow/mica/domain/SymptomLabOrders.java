package com.advinow.mica.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class SymptomLabOrders extends Neo4jEntity {
			
			@JsonInclude(Include.NON_NULL)
			private Integer orderID;

			public Integer getOrderID() {
				return orderID;
			}

			public void setOrderID(Integer orderID) {
				this.orderID = orderID;
			}


}
