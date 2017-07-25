package com.avenuecode;

import static org.junit.Assert.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.avenuecode.model.Product;
import com.avenuecode.model.ProductImage;
import com.avenuecode.repository.ImageRepository;
import com.avenuecode.repository.ProductRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Test
	public void testFindChildByParentId() {
		this.entityManager.persist(new Product("Computador", "Um computador"));
		Product prod = productRepository.findProductByName("Computador");
		this.entityManager.persist(new Product("Mouse", "Um mouse", prod.getId()));
		this.entityManager.persist(new Product("Teclado", "Um teclado", prod.getId()));
		this.entityManager.persist(new Product("Monitor", "Um monitor", prod.getId()));
		this.entityManager.persist(new Product("Câmera", "Uma câmera de video USB", prod.getId()));
		List<Product> childs = productRepository.findChildByParentId(prod.getId());
		for (int i = 0; i < childs.size(); i++) {
			printProduct(childs.get(i));
		}
	}

	@Test
	public void testFindProductByName() {
		this.entityManager.persist(new Product("Lapis", "Um lapis"));
		Product prod = productRepository.findProductByName("Lapis");
		printProduct(prod);
		assertThat(prod.getName()).isEqualTo("Lapis");
		assertThat(prod.getDescription()).isEqualTo("Um lapis");
	}

	@Test
	public void testFindAllProducts() {
		this.entityManager.persist(new Product("Console Xbox One 500GB + 1 Controle + Jogo Assassin's Creed Unity", "Console Xbox One 500GB + 1 Controle + Jogo Assassin's Creed Unity"));
		Product parent = productRepository.findProductByName("Console Xbox One 500GB + 1 Controle + Jogo Assassin's Creed Unity");
		this.entityManager.persist(new Product("For Honor para Xbox One - Ubisoft", "Mergulhe em um campo de batalha junte-se a destemidos cavaleiros, vikings violentos e samurais mortais em mapas de batalhas exuberantes e desafiadoras.", parent.getId()));
		this.entityManager.persist(new Product("Final Fantasy XV para Xbox One - Square Enix", "Final Fantasy XV para Xbox One - Square Enix", parent.getId()));
		List<Product> all = productRepository.findAll();
		for (Product p : all) {
			printProduct(p);
		}
		assertThat(all.size()).isEqualTo(3);
	}

	@Test
	public void testSaveS() {
		Product p = new Product("Borracha", "Uma borracha");
		productRepository.save(p);
		Product prod = productRepository.findProductByName("Borracha");
		printProduct(prod);
		assertThat(prod.getName()).isEqualTo("Borracha");
		assertThat(prod.getDescription()).isEqualTo("Uma borracha");
	}

	@Test
	public void testCount() {
		productRepository.save(new Product("Micro-ondas Panasonic ST354", "Micro-ondas Panasonic Piccolo ST354 25 Litros Branco 110V"));
		productRepository.save(new Product("Fogão Electrolux 76SRB", "Fogão de Piso 5 Bocas Branco Electrolux 76SRB - Bivolt"));
		assertThat(productRepository.count()).isEqualTo(2);
	}

	@Test
	public void testFindAllImages() {
		this.entityManager.persist(new Product("The Last Guardian para PlayStation 4", "Prepare-se para embarcar na jornada de uma vida nesse enredo de amizade comovente e emocionante."));
		Product parent = productRepository.findProductByName("The Last Guardian para PlayStation 4");
		this.entityManager.persist(new ProductImage("the-last-guardian-ps4-cover", parent.getId()));
		this.entityManager.persist(new ProductImage("the-last-guardian-ps4-back", parent.getId()));
		List<ProductImage> all = imageRepository.findAll();
		for (ProductImage p : all) {
			printProductImage(p);
		}
		assertThat(all.size()).isEqualTo(2);
	}

	private void printProduct(Product p) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(p.getId());
		sb.append(",");
		sb.append(p.getName());
		sb.append(",");
		sb.append(p.getDescription());
		sb.append(",");
		sb.append(p.getParentId());
		sb.append(",");
		sb.append(p.getImages());
		sb.append("}");
		System.out.println(sb.toString());
	}

	private void printProductImage(ProductImage pi) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(pi.getId());
		sb.append(",");
		sb.append(pi.getType());
		sb.append(",");
		sb.append(pi.getProductId());
		sb.append("}");
		System.out.println(sb.toString());
	}
}
