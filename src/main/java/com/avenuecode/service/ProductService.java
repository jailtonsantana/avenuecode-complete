package com.avenuecode.service;

import java.util.List;

import com.avenuecode.model.Product;
import com.avenuecode.model.ProductImage;

public interface ProductService {

	Product getProductById(long id, boolean child, boolean images);
	
	Product getProductByName(String name);
	
	List<Product> getAllProducts(boolean child, boolean images);
	
	List<ProductImage> getImageByProductId(long id);
	
	List<Product> getChildProducts(long id);
	
	boolean isProductExists(Product name);
	
	void saveProduct(Product p);

	boolean isImageExists(String type);

	void saveImage(ProductImage image);

	void deleteProduct(Product p);

	ProductImage getImageById(long id);

	void deleteImage(ProductImage image);

	boolean isProductExists(long id);

	void updateProduct(Product prod);
}
