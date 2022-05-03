package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.size.ProductSize;

@Repository
public interface ProductSizeDao extends JpaRepository<ProductSize, Long>{
	
	
	@Query(value =" select * from product_size where product_id = ?1", nativeQuery  = true)
	public List<ProductSize> findProductSizeByProductId(long productId);
	
	@Query(value ="select * from product_size where product_id = ?1 and size_id = ?2", nativeQuery = true)
	public ProductSize findProductSize(long productId, int sizeId);
	
	@Modifying
	@Query(value=" update product_size p set p.quantity = :quantity where p.id = :id" , nativeQuery =  true)
	@org.springframework.transaction.annotation.Transactional
	public void updateQuantity(@Param(value="id") long id,@Param(value="quantity") int quantity);
}
