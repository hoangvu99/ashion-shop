package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.cart.Cart;

@Repository
public interface CartDao extends JpaRepository<Cart	, Long>{
	
	@Query(value =" select * from cart where user_id = ?1", nativeQuery = true)
	public Cart findUserCart(long userId);

}
