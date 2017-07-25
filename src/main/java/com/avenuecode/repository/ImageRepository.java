package com.avenuecode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avenuecode.model.ProductImage;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Long> {

	@Query("select i from ProductImage i where productId = ?1")
	List<ProductImage> findByProductId(long prodid);

	@Query("select i from ProductImage i where type = ?1")
	ProductImage findByType(String type);
}
