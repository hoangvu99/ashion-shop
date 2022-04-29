package com.example.service;

import java.util.List;

import com.example.model.order.Order;
import com.example.model.order.Orders;

public interface OrderService {
	
	public void saveOrder(Orders order);
	
	public void updateStatusOrder(int i);
	
	List<Orders> findAllOrders();
	
	public Orders findOrderById(long orderId);
	
	public void acceptOrder(long orderId);
}	
