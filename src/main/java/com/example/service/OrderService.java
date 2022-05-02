package com.example.service;

import java.util.List;


import com.example.model.order.Orders;

public interface OrderService {
	
	public void saveOrder(Orders order);
	
	public void updateStatusOrder(int i);
	
	List<Orders> findAllOrders();
	
	public Orders findOrderById(long orderId);
	
	public void acceptOrder(long orderId);
	
	public List<Orders> newOrders();
	public List<Orders> acceptedOrders();
	public List<Orders> successOrders();
	public List<Orders> refusedOrders();
	
	public List<Orders> listUserOrder(long userId);
	
	public void deleteOrder(long orderId);
	
	public int countNewOrders();
	
	public int countAcceptedOrders();
}	
