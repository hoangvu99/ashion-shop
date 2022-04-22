package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.product.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>{
	
	@Query(value =" select * from product where id = ?1", nativeQuery = true)
	public Product findProductById(long id);
	
	@Query(value = "select * from product where name like %?1% ", nativeQuery = true)
	public List<Product>searchProductByName(String name);
}
