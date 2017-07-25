package com.avenuecode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenuecode.model.Product;
import com.avenuecode.model.ProductImage;
import com.avenuecode.repository.ImageRepository;
import com.avenuecode.repository.ProductRepository;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Override
	public Product getProductById(long id, boolean child, boolean images) {
		Product one = productRepository.findOne(id);
		if (one != null) {
			if (child) {
				List<Product> subprod = productRepository.findChildByParentId(one.getId());
				one.setChild(subprod);
			}
			if (images) {
				List<ProductImage> prodImages = imageRepository.findByProductId(one.getId());
				one.setImages(prodImages);
			}
		}
		return one;
	}

	@Override
	public List<Product> getAllProducts(boolean child, boolean images) {
		List<Product> all = productRepository.findAll();
		if (child || images) {
			for (Product p : all) {
				if (child) {
					List<Product> subprod = productRepository.findChildByParentId(p.getId());
					p.setChild(subprod);
				}
				if (images) {
					List<ProductImage> prodImages = imageRepository.findByProductId(p.getId());
					p.setImages(prodImages);
				}
			}
		}
		return all;
	}

	@Override
	public List<ProductImage> getImageByProductId(long id) {
		List<ProductImage> images = imageRepository.findByProductId(id);
		return images;
	}

	@Override
	public List<Product> getChildProducts(long id) {
		return productRepository.findChildByParentId(id);
	}

	@Override
	public boolean isProductExists(Product p) {
		return getProductByName(p.getName()) != null;
	}

	@Override
	public Product getProductByName(String name) {
		return productRepository.findProductByName(name);
	}

	@Override
	public void saveProduct(Product p) {
		productRepository.save(p);
		Product parent = productRepository.findProductByName(p.getName());
		List<Product> child = p.getChild();
		if (child != null) {
			for (Product pc : child) {
				pc.setParentId(parent.getId());
				productRepository.save(pc);
			}
		}
		List<ProductImage> images = p.getImages();
		if (images != null) {
			for (ProductImage im : images) {
				ProductImage found = imageRepository.findByType(im.getType());
				if (found != null) {
					im.setId(found.getId());
				}
				im.setProductId(parent.getId());
				imageRepository.save(im);
				
			}
		}
	}

	@Override
	public boolean isImageExists(String type) {
		return imageRepository.findByType(type) != null;
	}

	@Override
	public void saveImage(ProductImage im) {
		ProductImage found = imageRepository.findByType(im.getType());
		if (found != null) {
			im.setId(found.getId());
		}
		imageRepository.save(im);
	}

	@Override
	public void deleteProduct(Product p) {
		List<ProductImage> images = p.getImages();
		for (ProductImage pi : images) {
			imageRepository.delete(pi.getId());
		}
		List<Product> child = p.getChild();
		for (Product pc : child) {
			productRepository.delete(pc.getId());
		}
		productRepository.delete(p.getId());
	}

	@Override
	public ProductImage getImageById(long id) {
		return imageRepository.findOne(id);
	}

	@Override
	public void deleteImage(ProductImage image) {
		imageRepository.delete(image.getId());
	}

	@Override
	public boolean isProductExists(long id) {
		return productRepository.findOne(id) != null;
	}

	@Override
	public void updateProduct(Product prod) {
		productRepository.save(prod);
	}

}
