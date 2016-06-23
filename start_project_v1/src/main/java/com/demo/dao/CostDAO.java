package com.demo.dao;

import com.demo.bean.Cost;

public interface CostDAO {
	
	public int save(Cost c);
	
	public Cost getCostById(int id);

}
