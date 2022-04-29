package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.cart.CartItem;

@Repository
public interface CartItemDao extends JpaRepository<CartItem, Long>{
	
	@Query(value =" select * from cart_item where product_id = ?1 and product_size_id = ?2", nativeQuery = true)
	CartItem findCartItem(long productId, long productSizeId);

}
