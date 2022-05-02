package com.example.service;

import com.example.model.cart.CartItem;

public interface CartItemService {
	
	CartItem findCartItem(long productId, long productSizeId);
	
	void deleteCartItem(long cartItemId);
	
	
}
