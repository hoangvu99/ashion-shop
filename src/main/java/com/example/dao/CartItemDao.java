package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.cart.CartItem;

@Repository
public interface CartItemDao extends JpaRepository<CartItem, Long>{
	
	@Query(value =" select * from cart_item where product_id = ?1 and product_size_id = ?2 ", nativeQuery = true)
	CartItem findCartItem(long productId, long productSizeId);
	
	@Modifying
	@Query(value ="DELETE FROM cart_item  WHERE id = ?1", nativeQuery = true)
	@Transactional
	public void deleteCartItem(long cartItemId);

}
