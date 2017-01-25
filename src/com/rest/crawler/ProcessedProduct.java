package com.rest.crawler;

import java.util.Vector;

public class ProcessedProduct {
	
	private String title;
	private String image;
	private Vector<Review> reviews;
	
	
	public ProcessedProduct(){}
	
	public ProcessedProduct(String t, String i, Vector<Review> v) {
		
		title = t;
		image = i;
		reviews = v;
	}
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Vector<Review> getReviews() {
		return reviews;
	}
	public void setReviews(Vector<Review> reviews) {
		this.reviews = reviews;
	}
	

}
