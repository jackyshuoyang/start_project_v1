package com.demo.dao;

import java.io.File;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Document;

public class DocumentDAOImpl implements DocumentDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public int save(Document d) {
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(d);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		return d.getId();
	}

	@Override
	public String getFileNameById(Integer docId) {
		Session session  = this.sessionFactory.openSession();
		Query q = session.createQuery("from Document where id=:inputId");
		q.setParameter("inputId", docId);
		List<Document> doc = q.list();
		session.close();
		return doc.get(0).getFileName();
	}

	@Override
	public Document getFileById(Integer docId) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from Document where id=:inputId");
		q.setParameter("inputId", docId);
		List<Document> docs = q.list();
		return docs.get(0);
	}
	
	

}
