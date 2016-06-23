package com.demo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="Jobs")
public class Job {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	private String name;
	
	private String company;
	
	private int est_duration;
	
	private int status;
	
	private Date start_date;
	
	private Date est_finish_date;
	
	private Date actual_finish_date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getEst_duration() {
		return est_duration;
	}

	public void setEst_duration(int est_duration) {
		this.est_duration = est_duration;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
