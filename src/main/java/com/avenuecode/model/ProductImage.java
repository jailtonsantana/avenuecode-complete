package com.avenuecode.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProductImage {

	@Id
	@GeneratedValue
	private long id;
	private String type;
	private long productId;
	
	public ProductImage() {
		super();
	}

	public ProductImage(String t, long pid) {
		super();
		type = t;
		productId = pid;
	}

	public ProductImage(String t, Product p) {
		super();
		type = t;
		productId = p.getId();
	}

	public ProductImage(String t) {
		super();
		type = t;
		productId = 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
}
