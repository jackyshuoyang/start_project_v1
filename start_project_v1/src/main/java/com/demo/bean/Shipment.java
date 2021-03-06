package com.demo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shipments")
public class Shipment {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	
	private Integer status;
	
	private String origin;
	
	private String destination;
	
	private Date start_date;
	
	private Date est_finish_date;
	
	private Date actual_finish_date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}



	public Date getEst_finish_date() {
		return est_finish_date;
	}

	public void setEst_finish_date(Date est_finish_date) {
		this.est_finish_date = est_finish_date;
	}

	public Date getActual_finish_date() {
		return actual_finish_date;
	}

	public void setActual_finish_date(Date actual_finish_date) {
		this.actual_finish_date = actual_finish_date;
	}
	

}
