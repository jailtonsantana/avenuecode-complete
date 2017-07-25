package com.avenuecode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avenuecode.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("select p from Product p where parentId = ?1")
	List<Product> findChildByParentId(long id);
	
	@Query("select p from Product p where name = ?1")
	Product findProductByName(String name);
}
