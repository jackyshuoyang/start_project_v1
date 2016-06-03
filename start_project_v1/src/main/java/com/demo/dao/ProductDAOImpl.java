package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Product;

public class ProductDAOImpl implements ProductDAO {
	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int save(Product p) {
		int returnId=-1;
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
		return p.getId();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> list() {
		Session session = this.sessionFactory.openSession();
		List<Product>productList = session.createQuery("from Product where valid=true").list();
		session.close();
		return productList;
	}

	@Override
	public String deleteProduct(int id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createSQLQuery("update products set valid=false where id=:identifier");
		query.setParameter("identifier", id);
		int result = query.executeUpdate();
		tx.commit();
		return result>0?"success":"fail";
	}

}
