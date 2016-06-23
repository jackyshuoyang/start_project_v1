package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.ShipmentJobMap;
import com.demo.bean.ShipmentOrderMap;

public class ShipmentOrderMapDAOImpl implements ShipmentOrderMapDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	@Override
	public int save(ShipmentOrderMap map) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(map);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return map.getId();
	}

	@Override
	public List<ShipmentOrderMap> getShipmentOrderMapByShipmentId(int shipmentId) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from ShipmentOrderMap where shipment_id=:sid");
		q.setParameter("sid", shipmentId);
		List<ShipmentOrderMap>returnL = q.list();
		session.close();
		return returnL;
	}

	@Override
	public ShipmentOrderMap getShipmentOrderMapBySIdandOrderId(int shipmentId, int orderId) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from ShipmentOrderMap where shipment_id=:sid and order_id=:oid");
		q.setParameter("sid", shipmentId);
		q.setParameter("oid", orderId);
		List<ShipmentOrderMap>list = q.list();
		if(list==null||list.size()==0)return null;
		return list.get(0);
	}

}
