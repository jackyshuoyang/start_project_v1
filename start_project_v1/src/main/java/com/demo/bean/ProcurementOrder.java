package com.demo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="procurement_order")
public class ProcurementOrder {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	private String summary;
	
	@Column(name="manufactory_order_id")
	private String manufactoryOrderId;
	
	private int status;
	
	@Column(name="manufactory_name")
	private String manufactoryName;
	private double fob;
	
	@Column(name="creation_date",columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Column(name="start_date",columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name="end_date",columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	
	public String getManufactoryOrderId() {
		return manufactoryOrderId;
	}
	
	
	public void setManufactoryOrderId(String manufactoryOrderId) {
		this.manufactoryOrderId = manufactoryOrderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	public String getManufactoryName() {
		return manufactoryName;
	}
	public void setManufactoryName(String manufactoryName) {
		this.manufactoryName = manufactoryName;
	}
	public double getFob() {
		return fob;
	}
	public void setFob(double fob) {
		this.fob = fob;
	}
	
	
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	private boolean valid=true;
	
	public boolean getValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	
	
}
