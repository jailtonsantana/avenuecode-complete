package com.avenuecode.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.avenuecode.model.Product;
import com.avenuecode.model.ProductImage;
import com.avenuecode.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	ProductService productService;
	
	/**
	 * 
	 * @param includeChild
	 * @param includeImages
	 * @return
	 */
	@RequestMapping(value="/products", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> getAllProducts(
			@RequestParam(value="child", defaultValue="false") boolean includeChild, 
			@RequestParam(value="images", defaultValue="false") boolean includeImages) {
		List<Product> products = productService.getAllProducts(includeChild, includeImages);
		if(products.isEmpty()) {
            return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @param includeChild
	 * @param includeImages
	 * @return
	 */
	@RequestMapping(value="/products/{id}", method=RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable("id") long id, 
			@RequestParam(value="child", defaultValue="false") boolean includeChild, 
			@RequestParam(value="images", defaultValue="false") boolean includeImages) {
		System.out.println("ProductController.getProduct() -> buscando produto pelo id " + id);
		Product p = productService.getProductById(id, includeChild, includeImages);
		if (p == null) {
            System.out.println("Produto com id " + id + " nao encontrado");
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(p, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/products/{id}/products", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> getChildProducts(@PathVariable("id") long id) {
		List<Product> products = productService.getChildProducts(id);
		if(products.isEmpty()) {
			System.out.println("ProductController.getAllProducts() -> empty");
            return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/products/{id}/images", method=RequestMethod.GET)
	public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable("id") long id) {
		List<ProductImage> images = productService.getImageByProductId(id);
//		System.out.println("ProductController.getProductImages() -> " + images);
		if(images.isEmpty()) {
			System.out.println("ProductController.getAllProducts() -> empty");
            return new ResponseEntity<List<ProductImage>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<ProductImage>>(images, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param prod
	 * @param uriBuilder
	 * @return
	 */
	@RequestMapping(value="/products", method=RequestMethod.POST)
	public ResponseEntity<Void> createProduct(@RequestBody Product prod) {
		System.out.println("ProductController.createProduct() -> criando produto " + prod.getName());
		 
        if (productService.isProductExists(prod)) {
            System.out.println("A product with name " + prod.getName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        productService.saveProduct(prod);

//        List<Product> all = productService.getAllProducts(true, true);
//        for (Product product : all) {
//        	System.out.println("ProductController.createProduct() -> produto=" + product.getStringTrace());
//		}
 
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(uriBuilder.path("/products/{id}").buildAndExpand(prod.getId()).toUri());
//        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	/**
	 * @param image
	 * @return
	 */
	@RequestMapping(value="/images", method=RequestMethod.POST)
	public ResponseEntity<Void> createImage(@RequestBody ProductImage image) {
		System.out.println("ProductController.createImage() -> criando imagem " + image.getType());
		
		if(productService.isImageExists(image.getType())) {
			System.out.println("A image with name " + image.getType() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		productService.saveImage(image);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
		
		Product p = productService.getProductById(id, true, true);
		if (p == null) {
			System.out.println("Nao foi possivel remover produto. Id " + id + " nao encontrado");
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		
		productService.deleteProduct(p);
		
		return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/images/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<ProductImage> deleteImage(@PathVariable("id") long id) {
		
		ProductImage image = productService.getImageById(id);
		if (image == null) {
			System.out.println("Nao foi possivel remover imagem. Id " + id + " nao encontrado");
            return new ResponseEntity<ProductImage>(HttpStatus.NOT_FOUND);
		}
		
		productService.deleteImage(image);
		
		return new ResponseEntity<ProductImage>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, 
												 @RequestBody Product prod) {
		System.out.println("ProductController.updateProduct()");
		if (!productService.isProductExists(prod.getId())) {
            System.out.println("A product with id " + prod.getId() + " does not exist");
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
 
        productService.updateProduct(prod);
        Product p = productService.getProductById(prod.getId(), true, true);
        List<Product> child = prod.getChild();
        for (Product pc : child) {
        	System.out.println("--->>> " + pc.getName());
        	if (productService.isProductExists(pc.getId())) {
        		productService.updateProduct(pc);
        	} else {
        		productService.saveProduct(pc);
        	}
		}

        return new ResponseEntity<Product>(p, HttpStatus.OK);
	}
}
