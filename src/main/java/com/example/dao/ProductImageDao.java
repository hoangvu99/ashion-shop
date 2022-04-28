package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.product.ProductImages;

@Repository
public interface ProductImageDao extends JpaRepository<ProductImages, Long>{
	
	@Query(value="select * from product_images where product_id = ?1", nativeQuery = true)
	public List<ProductImages> findByProductId(long id);
}
