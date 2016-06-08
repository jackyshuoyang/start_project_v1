package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.ProcurementOrder;

public class ProcurementOrderDAOImpl implements ProcurementOrderDAO {

	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int save(ProcurementOrder p) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
		return p.getId();
	}

	@Override
	public List<ProcurementOrder> list() {
		Session session = this.sessionFactory.openSession();
		List<ProcurementOrder>orderList = session.createQuery("from ProcurementOrder where valid=true order by id desc").list();
		session.close();
		return orderList;
	}

	@Override
	public int deleteProcurementOrder(Integer id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createSQLQuery("update procurement_order set valid=false where id=:identifier");
		query.setParameter("identifier", id);
		int result = query.executeUpdate();
		tx.commit();
		return result;
	}

	@Override
	public int updateProcurementOrder(ProcurementOrder p) {
		int returnId=-1;
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(p);
		tx.commit();
		session.close();
		return p.getId();
	}

}
