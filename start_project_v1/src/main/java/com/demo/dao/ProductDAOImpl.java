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
	public int deleteProduct(Integer id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createSQLQuery("update products set valid=false where productid=:identifier");
		query.setParameter("identifier", id);
		int result = query.executeUpdate();
		tx.commit();
		return result;
	}
	
	@Override
	public Product getProductById(int id){
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from Product where valid=true and id=:pid");
		q.setParameter("pid", id);
		List<Product>plist = q.list();
		session.close();
		if(plist.size()==1)
			return plist.get(0);
		else
			return null;
	}

}
