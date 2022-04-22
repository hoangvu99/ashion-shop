package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.size.ProductSize;

@Repository
public interface ProductSizeDao extends JpaRepository<ProductSize, Long>{
	
	
	@Query(value =" select * from product_size where product_id = ?1", nativeQuery  = true)
	public List<ProductSize> findProductSizeByProductId(long productId);
}
