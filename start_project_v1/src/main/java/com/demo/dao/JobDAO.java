package com.demo.dao;

import com.demo.bean.Job;

public interface JobDAO {
	
	public Job getJobById(int id);
	
	public int save(Job b);
	
	

}
