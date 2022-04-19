package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.product.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>{
	
	@Query(value =" select * from product where id == ?!", nativeQuery = true)
	public Product findProductById(int id);
}
