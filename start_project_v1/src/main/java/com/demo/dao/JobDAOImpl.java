package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Cost;
import com.demo.bean.Job;

public class JobDAOImpl implements JobDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Job getJobById(int id) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("From Job where id=:inputId");
		q.setParameter("inputId", id);
		List<Job> l = q.list();
		session.close();
		if(l==null||l.size()==0)return null;
		return l.get(0);
	}

	@Override
	public int save(Job b) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(b);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return b.getId();
	}

}
