package com.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.demo.bean.Cost;
import com.demo.bean.JobCostMap;

public class JobCostMapDAOImpl implements JobCostMapDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<JobCostMap> getCostForJob(int jobId) {
		Session session = this.sessionFactory.openSession();
		Query q = session.createQuery("from JobCostMap where job_id=:jId");
		q.setParameter("jId", jobId);
		List<JobCostMap>returnL = q.list();
		session.close();
		return returnL;
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

}
