package com.avenuecode.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@NamedQuery(name="Product.findChildByParentId",
//		query="selec p from Product where p.parent.id = ?1")
public class Product {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;

	private String description;
	private long parentId;
	@Transient
	private List<ProductImage> images;
	@Transient
	private List<Product> child;
	
	public Product() {
		super();
		images = new ArrayList<ProductImage>();
		child = new ArrayList<Product>();
	}
	
	public Product(String n, String d) {
		super();
		name = n;
		description = d;
		images = new ArrayList<ProductImage>();
		child = new ArrayList<Product>();
	}

	public Product(String n, String d, long pid) {
		super();
		name = n;
		description = d;
		parentId = pid;
		images = new ArrayList<ProductImage>();
		child = new ArrayList<Product>();
	}

	public Product(String n, String d, Product p) {
		super();
		name = n;
		description = d;
		parentId = p.getId();
		images = new ArrayList<ProductImage>();
		child = new ArrayList<Product>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public void addImages(ProductImage image) {
		images.add(image);
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<Product> getChild() {
		return child;
	}

	public void setChild(List<Product> child) {
		this.child = child;
	}

	public void addProduct(Product child) {
		this.child.add(child);
		child.setParentId(getId());
	}

	@JsonIgnore
	public String getStringTrace() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(id);
		sb.append("|");
		sb.append(name);
		sb.append("|");
		sb.append(parentId);
		sb.append("|");
		for (Product p : child) {
			sb.append("<");
			sb.append(p.getId());
			sb.append(",");
			sb.append(p.getName());
			sb.append(">");
		}
		
		sb.append("}");
		return sb.toString();
	}
}
