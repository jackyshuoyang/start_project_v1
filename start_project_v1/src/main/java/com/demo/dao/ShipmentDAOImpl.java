package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Job;
import com.demo.bean.Shipment;

public class ShipmentDAOImpl implements ShipmentDAO {
	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int save(Shipment shipment) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(shipment);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return shipment.getId();
	}

	@Override
	public List<Shipment> getShipmentList() {
		Session session = this.sessionFactory.openSession();
		
		Query q = session.createQuery("From Shipment ");
		List<Shipment>returnList = q.list();
		session.close();
		return returnList;
	}

	@Override
	public Shipment getShipmentById(int id) {
		
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("From Shipment where id=:inputId");
		q.setParameter("inputId", id);
		List<Shipment> l = q.list();
		session.close();
		if(l==null||l.size()==0)return null;
		return l.get(0);
	}

	@Override
	public void updateShipment(Shipment s) {
		Session session = this.sessionFactory.openSession();
		session.saveOrUpdate(s);
		session.close();
	}

}
