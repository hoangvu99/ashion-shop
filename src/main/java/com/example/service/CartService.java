package com.example.service;

import com.example.model.cart.Cart;

public interface CartService {
	
	public Cart findUserCart(long userId);
	
	public void saveCart(Cart cart);
	
}
