package com.example.dao;

import javax.persistence.criteria.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.order.Orders;

@Repository
public interface OrderDao extends JpaRepository<com.example.model.order.Orders, Long>{
	
	@Query(nativeQuery = true, value = "select * from orders where id= ?1")
	public Orders findOrderByOrderId(long id);
	
	@Query(nativeQuery = true, value = "update orders set status = 1 where id = ?1")
	public void acceptOrder(Long orderId);
}
