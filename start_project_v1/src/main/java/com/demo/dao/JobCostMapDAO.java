package com.demo.dao;

import java.util.List;

import com.demo.bean.Cost;
import com.demo.bean.JobCostMap;

public interface JobCostMapDAO {
	
	public List<JobCostMap> getCostForJob(int jobId);
	
	public int save(Cost c);
	
	

}
