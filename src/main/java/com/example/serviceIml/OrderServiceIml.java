package com.example.serviceIml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.OrderDao;

import com.example.model.order.Orders;
import com.example.service.OrderService;
@Service
public class OrderServiceIml implements OrderService{

	@Autowired 
	OrderDao orderDao;
	@Override
	public void saveOrder(Orders order) {
		orderDao.save(order);
		
	}

	@Override
	public void updateStatusOrder(int i) {
		
		
	}

	@Override
	public List<Orders> findAllOrders() {
		
		return orderDao.findAll();
	}

	@Override
	public Orders findOrderById(long orderId) {
		return orderDao.findOrderByOrderId(orderId);
		
	}
	@Override
	public void acceptOrder(long orderId) {
		Orders orders = findOrderById(orderId);
		orders.setStatus(1);
		orderDao.save(orders);
		
	}
	
	@Override
	public List<Orders> acceptedOrders() {
		// TODO Auto-generated method stub
		return orderDao.acceptedOrders();
	}
	
	@Override
	public List<Orders> newOrders() {
		// TODO Auto-generated method stub
		return orderDao.newOrders();
	}
	@Override
	public List<Orders> successOrders() {
		// TODO Auto-generated method stub
		return orderDao.successOrders();
	}
	@Override
	public List<Orders> refusedOrders() {
		// TODO Auto-generated method stub
		return orderDao.refusedOrders();
	}
	@Override
	public List<Orders> listUserOrder(long userId) {
		// TODO Auto-generated method stub
		return orderDao.listUserOrder(userId);
	}

}
