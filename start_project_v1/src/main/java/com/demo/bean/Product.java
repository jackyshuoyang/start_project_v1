package com.demo.bean;

public class Product {
	public Product() {
		super();
		// TODO Auto-generated constructor stub
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
	private int id;
	private String name;
	private String imageUrl;
	private float fob;
}
