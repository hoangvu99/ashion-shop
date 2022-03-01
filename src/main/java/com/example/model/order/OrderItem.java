package com.example.model.order;

import com.example.model.cart.Cart;
import com.example.model.product.Product;
import com.example.model.promotion.Promotion;

public class OrderItem {
	
	private long id;
	private Product product;
	private Order order;
	private Promotion promotion;
	private int quantity;
	private double total;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
