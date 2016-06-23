package com.demo.dao;

import java.util.List;

import com.demo.bean.Cost;

public interface JobCostMapDAO {
	
	public List<Cost> getCostForJob(int jobId);
	
	public int save(Cost c);
	
	

}
