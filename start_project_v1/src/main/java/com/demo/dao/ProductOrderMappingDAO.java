package com.demo.dao;

import java.util.List;

import com.demo.bean.ProductOrderMapping;

public interface ProductOrderMappingDAO {
	
	public void saveProductAndOrderMaps(ProductOrderMapping map);
	
	public List<ProductOrderMapping> getProductMappingForOrder(int orderId);
	
	public void deleteMappingBasedOnOrderId(int orderId);
	
	public void deleteProductFromOrder(int productId,int orderId);
	
	public void updateMapping(ProductOrderMapping p);
}
