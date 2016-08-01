package com.demo.bean;

public class DisplayShipment extends Shipment {
	public float cost; 
	public DisplayShipment(Shipment s){
		this.setId(s.getId());
		this.setStatus(s.getStatus());
		this.setActual_finish_date(s.getActual_finish_date());
		this.setDestination(s.getDestination());
		this.setOrigin(s.getOrigin());
		this.setStart_date(s.getStart_date());
		this.setEst_finish_date(s.getEst_finish_date());
	}
	
	public DisplayShipment(){
		
	}
}
