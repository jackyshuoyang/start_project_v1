package com.demo.dao;

import java.util.List;

import com.demo.bean.EventLog;

public interface EventLogDAO {
	
	public List<EventLog> getOrderEventLog(Integer id); 

	public int save(EventLog p);
	
	public int delete(int id);
	
	public int updateEventLog(EventLog p);
	
}
