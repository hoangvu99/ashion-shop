package com.example.model.cart;

import java.util.Set;

import com.example.model.product.Product;
import com.example.model.user.User;

public class Cart {
	
	private long id;
	private User user;
	private int status;
	private Set<CartItem> cartItems;
	private String createdAt;
	private String updatedAt;
	private double cartTotal;
	
}
