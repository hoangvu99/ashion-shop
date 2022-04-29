package com.example.serviceIml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.CartItemDao;
import com.example.model.cart.CartItem;
import com.example.service.CartItemService;

@Service
public class CartItemServiceIml implements CartItemService{
	@Autowired
	CartItemDao cartItemDao;
	@Override
	public CartItem findCartItem(long productId, long productSizeId) {
		// TODO Auto-generated method stub
		return cartItemDao.findCartItem(productId, productSizeId);
	}
	
	@Override
	public void deleteCartItem(long cartItemId) {
		
		cartItemDao.deleteById(cartItemId);
		
	}
	
}
