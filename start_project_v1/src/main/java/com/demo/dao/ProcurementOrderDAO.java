package com.demo.dao;

import java.util.List;

import com.demo.bean.ProcurementOrder;


public interface ProcurementOrderDAO {
	
	public int save(ProcurementOrder p);
	
	public List<ProcurementOrder> list();
	
	public int deleteProcurementOrder(Integer id);
	
	public int updateProcurementOrder(ProcurementOrder p);

}
