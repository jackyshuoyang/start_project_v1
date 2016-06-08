package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Product;
import com.demo.bean.ProductOrderMapping;

public class ProductOrderMappingDAOImpl implements ProductOrderMappingDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveProductAndOrderMaps(ProductOrderMapping map){
		Session session = this.sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		try{
			session.persist(map);
			invalidateFobForOrder(map.orderId, session);
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
	}
	
	@Override
	public void updateMapping(ProductOrderMapping p){
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		if(p.qty==0){
			deleteProductFromOrder(p.productId, p.orderId);
		}else{
			try{
				Query q =session.createSQLQuery("update order_product_maps set qty=:pQty where order_id=:pOrderId and product_id=:pProductId");
				q.setParameter("pQty", p.qty);
				q.setParameter("pOrderId", p.orderId);
				q.setParameter("pProductId", p.productId);
				q.executeUpdate();
				invalidateFobForOrder(p.orderId, session);
				tx.commit();
			}catch(Exception e){
				tx.rollback();
			}finally{
				session.close();
			}
		}
		
	}

	@Override
	public List<ProductOrderMapping> getProductMappingForOrder(int orderId){
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from ProductOrderMapping where valid=true and orderId=:porderId");
		q.setParameter("porderId", orderId);
		List<ProductOrderMapping>mappingList = q.list();
		session.close();
		return mappingList;
	}
	
	@Override
	public void deleteMappingBasedOnOrderId(int orderId){
		Session session = this.sessionFactory.openSession();
		Query q = session.createSQLQuery("update order_product_maps set valid = false where order_id=:porderId");
		q.setParameter("porderId", orderId);
		q.executeUpdate();
		session.close();
	}
	
	private void invalidateFobForOrder(int orderId, Session session)
	{
		Query q = session.createQuery("from ProductOrderMapping where valid=true and orderId=:pOrderId");
		q.setParameter("pOrderId", orderId);
		List<ProductOrderMapping>mappingList = q.list();
		List<Product> productList;
		double totalFob=0;
		for(int i=0;i<mappingList.size();i++){
			q = session.createQuery("from Product where valid=true and id=:pid");
			q.setParameter("pid", mappingList.get(i).productId);
			productList = q.list();
			totalFob += productList.get(0).getFob() * mappingList.get(i).qty;
		}
		
		q = session.createSQLQuery("update procurement_order set fob=:qFob where id=:qId");
		q.setParameter("qFob", totalFob);
		q.setParameter("qId", orderId);
		q.executeUpdate();
	}
	
	@Override
	public void deleteProductFromOrder(int productId,int orderId){
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			
				Query q = session.createSQLQuery("update order_product_maps set valid = false where order_id=:qorderId and product_id=:qproductID");
				
				q.setParameter("qorderId", orderId);
				q.setParameter("qproductID", productId);
				q.executeUpdate();
				
				invalidateFobForOrder(orderId, session);
				
				tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			
			session.close();
		}
		
		
	}


}
