package com.demo;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.bean.Product;
import com.demo.dao.ProductDAO;

public class HibernateIntegrationTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		ProductDAO productDAO = context.getBean(ProductDAO.class);
		Product product = new Product();
		product.setName("wardrobe suite A");
		product.setImageUrl("jfkas.jpg");
		product.setFob(new Float(174432.23));
		productDAO.save(product);
		
		
		List<Product> list = productDAO.list();
		for(Product p : list ){
			System.out.println(p.toString());
			
		}
		
	}

}
