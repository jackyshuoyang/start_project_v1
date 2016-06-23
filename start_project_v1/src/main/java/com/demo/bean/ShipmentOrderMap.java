package com.demo.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shipment_order_maps")
public class ShipmentOrderMap {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	private int shipment_id;
	
	private int order_id;
	
	private double space_taken_percentage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShipment_id() {
		return shipment_id;
	}

	public void setShipment_id(int shipment_id) {
		this.shipment_id = shipment_id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public double getSpace_taken_percentage() {
		return space_taken_percentage;
	}

	public void setSpace_taken_percentage(double space_taken_percentage) {
		this.space_taken_percentage = space_taken_percentage;
	}
	
	

}
