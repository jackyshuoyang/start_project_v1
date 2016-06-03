package com.demo.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * Entity Bean with JPA annotations
 * Hibernate provides JPA implementation
 * @author yang
 *
 */

@Entity
@Table(name="Products")
public class Product {
	public Product() {
		super();
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public float getFob() {
		return fob;
	}
	public void setFob(float fob) {
		this.fob = fob;
	}
	
	@Id
	@GeneratedValue
	@Column(name="productid")
	private int id;
	
	
	private String name;
	private String imageUrl;
	private float fob;
	private boolean valid=true;
	@Override
	public String toString(){
		return "id"+id+", name="+name+", imageUrl="+imageUrl+", fob="+fob;
	}
	public boolean getValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
