package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.EventLog;

public class EventLogDAOImpl implements EventLogDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<EventLog> getOrderEventLog(Integer id) {
		Session session = sessionFactory.openSession();
		Query q = session.createQuery("from EventLog where type=1 and valid=true and referralId=:orderId order by dateOfEvent");
		q.setParameter("orderId", id);
		List<EventLog> list = q.list();
		return list;
	}

	@Override
	public int save(EventLog p) {
		p.valid=true;
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			session.persist(p);
			if(p.type==EnumLogType.OrderType)
			{
				if(p.actionType==EnumLogActionType.CONTRACT_SIGN_OFF){
					//update order status to be in progress and start date to be eventLog.dateOfEvent;
					Query q = session.createSQLQuery("update procurement_order set status=:inProgress , start_date=:evtDate where id=:orderId");
					q.setParameter("inProgress", EnumOrderStatus.IN_PROGRESS);
					q.setParameter("evtDate", p.dateOfEvent);
					q.setParameter("orderId", p.referralId);
					q.executeUpdate();
				}
				
			}else if(p.type==EnumLogType.ShipmentType)
			{
				//todo
			}
			
			
			
			tx.commit();
		}catch(Exception e){
			tx.rollback();
		}finally{
			session.close();
		}
		
		
		return p.id;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEventLog(EventLog p) {
		// TODO Auto-generated method stub
		return 0;
	}

}
