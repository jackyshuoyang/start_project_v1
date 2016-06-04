package com.demo.dao;

import java.util.List;

import com.demo.bean.Product;

public interface ProductDAO {
	
	public int save(Product p);
	
	public List<Product> list();
	
	public int deleteProduct(Integer id);

}
