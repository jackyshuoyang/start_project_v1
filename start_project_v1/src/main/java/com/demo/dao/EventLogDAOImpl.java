package com.demo.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.demo.bean.EventLog;

public class EventLogDAOImpl implements EventLogDAO {

	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<EventLog> getOrderEventLog(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(EventLog p) {
		// TODO Auto-generated method stub
		return 0;
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
