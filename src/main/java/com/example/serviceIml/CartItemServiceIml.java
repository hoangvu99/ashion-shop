package com.example.serviceIml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void deleteCartItem(long cartItemId) {
		
		cartItemDao.deleteCartItem(cartItemId);
		
	}
	
}
