package com.demo.dao;

import java.util.List;

import com.demo.bean.Shipment;

public interface ShipmentDAO {
	
	public int save(Shipment shipment);
	
	public List<Shipment> getShipmentList();
	
	public Shipment getShipmentById(int id);
	
	public void updateShipment(Shipment s);

}
