package com.avenuecode;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avenuecode.ProductsApplication;
import com.avenuecode.model.Product;
import com.avenuecode.model.ProductImage;
import com.avenuecode.repository.ImageRepository;
import com.avenuecode.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * @author Jailton Santana
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductsApplication.class)
@WebAppConfiguration
public class ProductControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
									MediaType.APPLICATION_JSON.getSubtype(),
									Charset.forName("utf8"));

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);
		assertNotNull("O conversor de mensagem JSON não deve ser nulo", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
		
		this.imageRepository.deleteAllInBatch();
		this.productRepository.deleteAllInBatch();
		
		productRepository.save(new Product("Mug", "Star wars mug"));
		Product p = productRepository.findProductByName("Mug");
		imageRepository.save(new ProductImage("mug-front", p.getId()));
		imageRepository.save(new ProductImage("mug-several", p.getId()));
		productRepository.save(new Product("Car", "Hatchback car"));
		p = productRepository.findProductByName("Car");
		productRepository.save(new Product("Tire", "Bridgestone tire", p.getId()));
		productRepository.save(new Product("Steering wheel", "Steering wheel in leather", p.getId()));
		productRepository.save(new Product("Couch", "Steering wheel in leather", p.getId()));

		
		printProductRepository();
		printImageRepository();
	}

	@Test
	public void testProductNotFound() throws Exception {
		mockMvc.perform(get("/products/5"))
//				.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testGetAllProductsNoChildNeitherImages() throws Exception {
		mockMvc.perform(get("/products"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$", hasSize(5)))
			.andExpect(jsonPath("$[0].child", hasSize(0)))
        	.andExpect(jsonPath("$[0].images", hasSize(0)))
			.andExpect(jsonPath("$[1].child", hasSize(0)))
        	.andExpect(jsonPath("$[1].images", hasSize(0)))
			.andExpect(jsonPath("$[2].child", hasSize(0)))
        	.andExpect(jsonPath("$[2].images", hasSize(0)))
			.andExpect(jsonPath("$[3].child", hasSize(0)))
        	.andExpect(jsonPath("$[3].images", hasSize(0)))
			.andExpect(jsonPath("$[4].child", hasSize(0)))
        	.andExpect(jsonPath("$[4].images", hasSize(0)));
	}
	
	@Test
	public void testGetAllProductsWithChild() throws Exception {
		mockMvc.perform(get("/products?child=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$", hasSize(5)))
			.andExpect(jsonPath("$[0].child", hasSize(0)))
        	.andExpect(jsonPath("$[0].images", hasSize(0)))
			.andExpect(jsonPath("$[1].child", hasSize(3)))
        	.andExpect(jsonPath("$[1].images", hasSize(0)))
			.andExpect(jsonPath("$[2].child", hasSize(0)))
        	.andExpect(jsonPath("$[2].images", hasSize(0)))
			.andExpect(jsonPath("$[3].child", hasSize(0)))
        	.andExpect(jsonPath("$[3].images", hasSize(0)))
			.andExpect(jsonPath("$[4].child", hasSize(0)))
        	.andExpect(jsonPath("$[4].images", hasSize(0)));
	}
	
	@Test
	public void testGetAllProductsWithImages() throws Exception {
		mockMvc.perform(get("/products?images=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$", hasSize(5)))
			.andExpect(jsonPath("$[0].child", hasSize(0)))
        	.andExpect(jsonPath("$[0].images", hasSize(2)))
			.andExpect(jsonPath("$[1].child", hasSize(0)))
        	.andExpect(jsonPath("$[1].images", hasSize(0)))
			.andExpect(jsonPath("$[2].child", hasSize(0)))
        	.andExpect(jsonPath("$[2].images", hasSize(0)))
			.andExpect(jsonPath("$[3].child", hasSize(0)))
        	.andExpect(jsonPath("$[3].images", hasSize(0)))
			.andExpect(jsonPath("$[4].child", hasSize(0)))
        	.andExpect(jsonPath("$[4].images", hasSize(0)));
	}
	
	@Test
	public void testGetAllProductsWithChildAndImages() throws Exception {
		mockMvc.perform(get("/products?child=true&images=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$", hasSize(5)))
			.andExpect(jsonPath("$[0].child", hasSize(0)))
        	.andExpect(jsonPath("$[0].images", hasSize(2)))
			.andExpect(jsonPath("$[1].child", hasSize(3)))
        	.andExpect(jsonPath("$[1].images", hasSize(0)))
			.andExpect(jsonPath("$[2].child", hasSize(0)))
        	.andExpect(jsonPath("$[2].images", hasSize(0)))
			.andExpect(jsonPath("$[3].child", hasSize(0)))
        	.andExpect(jsonPath("$[3].images", hasSize(0)))
			.andExpect(jsonPath("$[4].child", hasSize(0)))
        	.andExpect(jsonPath("$[4].images", hasSize(0)));
	}
	
	@Test
	public void testGetOneProductsNoChildNeitherImages() throws Exception {
		productRepository.save(new Product("Computer", "Hi-tech computer"));
		Product p = productRepository.findProductByName("Computer");
		productRepository.save(new Product("Monitor", "21 in monitor", p.getId()));
		productRepository.save(new Product("Keyboard", "Wireless keyboard", p.getId()));
		productRepository.save(new Product("Mouse", "Wireless mouse", p.getId()));
		imageRepository.save(new ProductImage("cpu-front", p.getId()));
		imageRepository.save(new ProductImage("kit-image", p.getId()));
		imageRepository.save(new ProductImage("peripherals-image", p.getId()));
		imageRepository.save(new ProductImage("monitor-image", p.getId()));
		mockMvc.perform(get("/products/" + p.getId()))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$.child", hasSize(0)))
        	.andExpect(jsonPath("$.images", hasSize(0)));
	}
	
	@Test
	public void testGetOneProductsWithChildNoImages() throws Exception {
		productRepository.save(new Product("Computer", "Hi-tech computer"));
		Product p = productRepository.findProductByName("Computer");
		productRepository.save(new Product("Monitor", "21 in monitor", p.getId()));
		productRepository.save(new Product("Keyboard", "Wireless keyboard", p.getId()));
		productRepository.save(new Product("Mouse", "Wireless mouse", p.getId()));
		imageRepository.save(new ProductImage("cpu-front", p.getId()));
		imageRepository.save(new ProductImage("kit-image", p.getId()));
		imageRepository.save(new ProductImage("peripherals-image", p.getId()));
		imageRepository.save(new ProductImage("monitor-image", p.getId()));
		mockMvc.perform(get("/products/" + p.getId() + "?child=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$.child", hasSize(3)))
        	.andExpect(jsonPath("$.images", hasSize(0)));
	}
	
	@Test
	public void testGetOneProductsNoChildWithImages() throws Exception {
		productRepository.save(new Product("Computer", "Hi-tech computer"));
		Product p = productRepository.findProductByName("Computer");
		productRepository.save(new Product("Monitor", "21 in monitor", p.getId()));
		productRepository.save(new Product("Keyboard", "Wireless keyboard", p.getId()));
		productRepository.save(new Product("Mouse", "Wireless mouse", p.getId()));
		imageRepository.save(new ProductImage("cpu-front", p.getId()));
		imageRepository.save(new ProductImage("kit-image", p.getId()));
		imageRepository.save(new ProductImage("peripherals-image", p.getId()));
		imageRepository.save(new ProductImage("monitor-image", p.getId()));
		mockMvc.perform(get("/products/" + p.getId() + "?images=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$.child", hasSize(0)))
        	.andExpect(jsonPath("$.images", hasSize(4)));
	}
	
	@Test
	public void testGetOneProductsWithChildAndImages() throws Exception {
		productRepository.save(new Product("Computer", "Hi-tech computer"));
		Product p = productRepository.findProductByName("Computer");
		productRepository.save(new Product("Monitor", "21 in monitor", p.getId()));
		productRepository.save(new Product("Keyboard", "Wireless keyboard", p.getId()));
		productRepository.save(new Product("Mouse", "Wireless mouse", p.getId()));
		imageRepository.save(new ProductImage("cpu-front", p.getId()));
		imageRepository.save(new ProductImage("kit-image", p.getId()));
		imageRepository.save(new ProductImage("peripherals-image", p.getId()));
		imageRepository.save(new ProductImage("monitor-image", p.getId()));
		mockMvc.perform(get("/products/" + p.getId() + "?child=true&images=true"))
//    		.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(contentType))
        	.andExpect(jsonPath("$.child", hasSize(3)))
        	.andExpect(jsonPath("$.images", hasSize(4)));
	}

	@Test
	public void testGetProduct() throws Exception {
		Product p = productRepository.findProductByName("Couch");
		mockMvc.perform(get("/products/" + p.getId()))
//				.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) p.getId())))
                .andExpect(jsonPath("$.name", is(p.getName())));
	}

	@Test
	public void testGetChildProducts() throws Exception {
		Product p = productRepository.findProductByName("Car");
		mockMvc.perform(get("/products/" + p.getId() + "/products"))
//        		.andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(contentType))
		        .andExpect(jsonPath("$[0].name", is("Tire")))
		        .andExpect(jsonPath("$[1].name", is("Steering wheel")))
		        .andExpect(jsonPath("$[2].name", is("Couch")));
	}

	@Test
	public void testGetProductImages() throws Exception {
		Product p = productRepository.findProductByName("Mug");
		mockMvc.perform(get("/products/" + p.getId() + "/images"))
//        		.andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(contentType))
		        .andExpect(jsonPath("$[0].type", is("mug-front")))
		        .andExpect(jsonPath("$[1].type", is("mug-several")));
	}

	@Test
	public void testCreateProductNoChildNoImage() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = new Product("Coffee maker", "Wallita Coffee maker");
		mockMvc.perform(
				post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(p)))
//					.andDo(print())
					.andExpect(status().isCreated());
		p = productRepository.findProductByName("Coffee maker");
		assertThat(p).isNotNull();
	}

	@Test
	public void testCreateProductWithChildNoImage() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = new Product("Computer", "Hi-tech computer");
		p.addProduct(new Product("Monitor", "21 in monitor"));
		p.addProduct(new Product("Keyboard", "Wireless keyboard"));
		p.addProduct(new Product("Mouse", "Wireless mouse"));
		mockMvc.perform(
				post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(p)))
//					.andDo(print())
					.andExpect(status().isCreated());
		p = productRepository.findProductByName("Computer");
		assertThat(p).isNotNull();
		List<Product> child = productRepository.findChildByParentId(p.getId());
		assertThat(child).isNotNull();
		assertThat(child.size()).isEqualTo(3);
		for (Product pc : child) {
			assertThat(pc.getParentId()).isEqualTo(p.getId());
		}
	}

	@Test
	public void testCreateProductWithChildAndImage() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = new Product("Computer", "Hi-tech computer");
		p.addProduct(new Product("Monitor", "21 in monitor"));
		p.addProduct(new Product("Keyboard", "Wireless keyboard"));
		p.addProduct(new Product("Mouse", "Wireless mouse"));
		p.addImages(new ProductImage("cpu-front"));
		p.addImages(new ProductImage("kit-image"));
		p.addImages(new ProductImage("peripherals-image"));
		p.addImages(new ProductImage("monitor-image"));
		mockMvc.perform(
				post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(p)))
//					.andDo(print())
					.andExpect(status().isCreated());
		p = productRepository.findProductByName("Computer");
		assertThat(p).isNotNull();
		List<Product> child = productRepository.findChildByParentId(p.getId());
		assertThat(child).isNotNull();
		assertThat(child.size()).isEqualTo(3);
		for (Product pc : child) {
			assertThat(pc.getParentId()).isEqualTo(p.getId());
		}
		List<ProductImage> i = imageRepository.findByProductId(p.getId());
		assertThat(i).isNotNull();
		assertThat(i.size()).isEqualTo(4);
		for (ProductImage pi : i) {
			assertThat(pi.getProductId()).isEqualTo(p.getId());
		}
	}

	@Test
	public void testDeleteParentProductCascade() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = productRepository.findProductByName("Car");
		mockMvc.perform(delete("/products/" + p.getId()))
//        		.andDo(print())
		        .andExpect(status().isNoContent());
		p = productRepository.findProductByName("Car");
		assertThat(p).isNull();
		p = productRepository.findProductByName("Tire");
		assertThat(p).isNull();
		p = productRepository.findProductByName("Steering wheel");
		assertThat(p).isNull();
		p = productRepository.findProductByName("Couch");
		assertThat(p).isNull();
	}

	@Test
	public void testDeleteChildProduct() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = productRepository.findProductByName("Tire");
		mockMvc.perform(delete("/products/" + p.getId()))
//        		.andDo(print())
		        .andExpect(status().isNoContent());
		p = productRepository.findProductByName("Tire");
		assertThat(p).isNull();
	}

	@Test
	public void testDeleteImage() throws HttpMessageNotWritableException, IOException, Exception {
		ProductImage p = imageRepository.findByType("mug-front");
		mockMvc.perform(delete("/images/" + p.getId()))
//        		.andDo(print())
		        .andExpect(status().isNoContent());
		p = imageRepository.findByType("mug-front");
		assertThat(p).isNull();
	}
	
	@Test
	public void testCreateImage() throws HttpMessageNotWritableException, IOException, Exception {
		ProductImage image = new ProductImage("cover", 0);
		mockMvc.perform(
				post("/images")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(image)))
//					.andDo(print())
					.andExpect(status().isCreated());
		image = imageRepository.findByType("cover");
		assertThat(image).isNotNull();
	}

	@Test
	public void testUpdateParentProduct() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = productRepository.findProductByName("Car");
		p.setDescription(p.getDescription() + " Chevrolet");
		mockMvc.perform(
				put("/products/" + p.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(p)))
//					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.description", endsWith("Chevrolet")));
		printProductRepository();
	}

	@Test
	public void testUpdateProductAddChild() throws HttpMessageNotWritableException, IOException, Exception {
		Product p = productRepository.findProductByName("Car");
		p.addProduct(new Product("Rear light", "Rear light"));
		p.setDescription(p.getDescription() + " Chevrolet");
		mockMvc.perform(
				put("/products/" + p.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(json(p)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.description", endsWith("Chevrolet")));
//					.andExpect(jsonPath("$.child", hasSize(4))); // TODO: verificar motivo da nao insercao do child
		printProductRepository();
	}

	private String json(Object o) throws HttpMessageNotWritableException, IOException {
		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, outputMessage);
		String body = outputMessage.getBodyAsString();
		System.out.println(body);
		return body;
	}

	private void printImageRepository() {
		System.out.println("-------------------- REPOSITORY: IMAGE ----------------------");
		List<ProductImage> images = imageRepository.findAll();
		for (ProductImage im : images) {
			System.out.printf("-> [imagem:{%d|%s|%d}]\n", im.getId(), im.getType(), im.getProductId());
		}
	}

	private void printProductRepository() {
		System.out.println("-------------------- REPOSITORY: PRODUCT --------------------");
		List<Product> all = productRepository.findAll();
		for (Product prod : all) {
			System.out.printf("-> [product:{%d|%s|%d}]\n", prod.getId(), prod.getName(), prod.getParentId());
		}
	}

}
