package com.demo.dao;

import java.util.List;

import com.demo.bean.Product;

public interface ProductDAO {
	
	public void save(Product p);
	
	public List<Product> list();

}
