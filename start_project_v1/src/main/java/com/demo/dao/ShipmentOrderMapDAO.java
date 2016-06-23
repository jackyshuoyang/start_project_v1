package com.demo.dao;

import java.util.List;

import com.demo.bean.ShipmentOrderMap;

public interface ShipmentOrderMapDAO {
	
	public int save(ShipmentOrderMap map);
	
	public List<ShipmentOrderMap> getShipmentOrderMapByShipmentId(int shipmentId);
	
	public ShipmentOrderMap getShipmentOrderMapBySIdandOrderId(int shipmentId, int orderId);

}
