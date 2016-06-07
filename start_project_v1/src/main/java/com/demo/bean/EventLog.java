package com.demo.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="event_log")
public class EventLog {
	@Id
	@GeneratedValue
	@Column(name="id")
	public int id;
	@Column(name="date_of_log")
	public Date dateOfLogging;
	
	@Column(name="date_of_event")
	public Date dateOfEvent;
	
	public String log;
	public String logger;
	
	@Column(name="log_type")
	public int type;//refering to procurement order or to shipment. 1,procurement order 2,shipment.
	@Column(name="referral_id")
	public int referralId;// either be procurement id or shipment id.
	
	@Column(name="level_of_emergency")
	public int levelOfEmergency;//1,super urgent. 2,urgent. 3, normal.
	
	@Column(name="document_url")
	public String documentUrl;
}
