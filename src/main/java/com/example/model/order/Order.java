package com.example.model.order;

import java.util.Set;

import com.example.model.cart.CartItem;
import com.example.model.user.User;

public class Order {
	
	private long id;
	private User user;
	private int status;
	private Set<OrderItem> orderItems;
	private String createdAt;
	private String updatedAt;
	private double orderTotal;
}
