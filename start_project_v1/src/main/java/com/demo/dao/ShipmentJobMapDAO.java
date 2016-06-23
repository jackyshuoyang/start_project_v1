package com.demo.dao;

import com.demo.bean.Job;
import com.demo.bean.ShipmentJobMap;
import java.util.List;


public interface ShipmentJobMapDAO {
	
	public int save(ShipmentJobMap s);
	
	public List<ShipmentJobMap> getJobShipmentMapByShipmentId(int shipmentId);
	
	public ShipmentJobMap getShipmentJobMapByJobAndShipment(int shipmentId,int jobId);
	
	public void updateShipmentJobOrder(int mapId,int newOrder);
	
	public ShipmentJobMap getShipmentJobMapById(int mapId);

}
