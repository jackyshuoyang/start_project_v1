package com.demo.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="order_product_maps")
public class ProductOrderMapping {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="order_id")
	public int orderId;
	
	@Column(name="product_id")
	public int productId;
	
	public int qty;
}
