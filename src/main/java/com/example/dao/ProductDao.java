package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.product.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>{
	
	@Query(value =" select * from product where id = ?1", nativeQuery = true)
	public Product findProductById(long id);
	
	@Query(value = "select * from product where name like %?1% and is_deleted = 0", nativeQuery = true)
	public List<Product>searchProductByName(String name);
	
	@Query(value = "select * from product where is_deleted = 0 order by id desc limit 8", nativeQuery = true)
	public List<Product> listLastestProduct();
	
	@Modifying
	@Query(value=" update product u set u.is_deleted = 1 where u.id = :id" , nativeQuery =  true)
	@org.springframework.transaction.annotation.Transactional
	public void deleteProduct(@Param(value = "id")long id);
	
	@Query(value ="select * from product where is_deleted = 0 order by id desc  limit ?1 offset ?2", nativeQuery = true)
	public List<Product>getPageProduct(int limit,int page);
	
	@Query(value =" select * from product where category_id = ?1 and is_deleted = 0 limit ?2 offset ?3", nativeQuery = true)
	public List<Product> productsByCategory(int categoryId,int limit, int offset);
	
	@Query(value =" select * from product where name like %?1% and is_deleted = 0 limit ?2 offset ?3", nativeQuery = true)
	public List<Product> productsByName(String s,int limit, int offset);
}

