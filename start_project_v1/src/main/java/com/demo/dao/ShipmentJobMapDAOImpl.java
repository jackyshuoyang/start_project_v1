package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.ShipmentJobMap;

public class ShipmentJobMapDAOImpl implements ShipmentJobMapDAO {

	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int save(ShipmentJobMap s) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(s);
			List<ShipmentJobMap>allMap = this.getJobShipmentMapByShipmentId(s.getShipment_id());
			invalidateJobMapsOrder(allMap);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return s.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShipmentJobMap> getJobShipmentMapByShipmentId(int shipmentId) {
		Session session = this.sessionFactory.openSession();
		List<ShipmentJobMap> returnList = null;
		Query q = session.createQuery("from ShipmentJobMap where shipment_id=:sid");
		q.setParameter("sid", shipmentId);
		returnList = q.list();
		session.close();
		return returnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShipmentJobMap getShipmentJobMapByJobAndShipment(int shipmentId, int jobId) {
		Session session = this.sessionFactory.openSession();
		List<ShipmentJobMap> returnList = null;
		Query q = session.createQuery("from ShipmentJobMap where shipment_id=:sid and job_id=:jid");
		q.setParameter("sid", shipmentId);
		q.setParameter("jid", jobId);
		returnList = q.list();
		session.close();
		if(returnList==null||returnList.size()==0)return null;
		return returnList.get(0);
	}
	

	@Override
	public ShipmentJobMap getShipmentJobMapById(int mapId) {
		Session session = this.sessionFactory.openSession();
		
		Query q = session.createQuery("from ShipmentJobMap where id=:iid");
		q.setParameter("iid", mapId);
		List<ShipmentJobMap> resultL = q.list();
		session.close();
		if(resultL!=null||resultL.size()==1){
			return resultL.get(0);
		}
		return null;
	}
	

	private void invalidateJobMapsOrder(List<ShipmentJobMap> list){
		if(list==null||list.size()==0)return;
		list.sort((map1,map2)->map1.getOrder()-map2.getOrder());
		for(int i=0;i<list.size();i++){
			list.get(i).setOrder(i);
		}
	}
	
	@Override
	public void updateShipmentJobOrder(int mapId, int newOrder) {
		
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			//1, get ship order map based on input map id.
			ShipmentJobMap map = getShipmentJobMapById(mapId);
			if(map==null) throw new Exception("ShipmentJobMapDAOImpl->updateShipmentOrder there is no such shipment job map.");
			List<ShipmentJobMap>allMap = getJobShipmentMapByShipmentId(map.getShipment_id());
			invalidateJobMapsOrder(allMap);
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
	}

}
