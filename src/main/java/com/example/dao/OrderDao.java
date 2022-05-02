package com.example.dao;

import java.util.List;

import javax.persistence.criteria.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.order.Orders;

@Repository
public interface OrderDao extends JpaRepository<com.example.model.order.Orders, Long>{
	
	@Query(nativeQuery = true, value = "select * from orders where id= ?1")
	public Orders findOrderByOrderId(long id);
	
	@Query(nativeQuery = true, value = "update orders set status = 1 where id = ?1")
	public void acceptOrder(Long orderId);
	
	@Query(value = "select * from orders where status = 0", nativeQuery = true)
	public List<Orders>newOrders();
	@Query(value = "select * from orders where status = 1", nativeQuery = true)
	public List<Orders>acceptedOrders();
	
	@Query(value = "select * from orders where status = 2", nativeQuery = true)
	public List<Orders>successOrders();
	
	@Query(value = "select  * from orders where status = 3", nativeQuery = true)
	public List<Orders>refusedOrders();
	
	@Query(value ="select * from orders where user_id = ?1", nativeQuery = true)
	public List<Orders> listUserOrder(long userId);
	
	@Modifying
	@Query(value ="DELETE FROM orders  WHERE id = ?1", nativeQuery = true)
	@Transactional
	public void deleteOrder(long orderId);
	
	@Query(value ="select count(*) from orders where status = 0", nativeQuery = true)
	public int countNewOrders();
	
	@Query(value ="select count(*) from orders where status = 1", nativeQuery = true)
	public int countAcceptedOrders();
	
	
}
