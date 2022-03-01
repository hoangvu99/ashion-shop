package com.example.model.cart;

import com.example.model.product.Product;

public class CartItem {
	
	private long id;
	private Product product;
	private Cart cart;
	private int quantity;
	private double total;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
