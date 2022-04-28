package com.example.serviceIml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.CartDao;
import com.example.model.cart.Cart;
import com.example.service.CartService;
@Service
public class CartServiceIml implements CartService{
	
	@Autowired
	CartDao cartDao;

	@Override
	public Cart findUserCart(long userId) {
		
		return cartDao.findUserCart(userId);
	}
	
	@Override
	public void saveCart(Cart cart) {
		cartDao.save(cart);
		
	}

}
