package com.demo.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Cost;

public class CostDAOImpl implements CostDAO {

	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int save(Cost c) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(c);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return c.getId();
	}

	@Override
	public Cost getCostById(int id) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("From Cost where id=:inputId");
		q.setParameter("inputId", id);
		List<Cost> l = q.list();
		session.close();
		if(l==null||l.size()==0)return null;
		return l.get(0);
	}

}
