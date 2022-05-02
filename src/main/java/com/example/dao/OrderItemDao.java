package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.order.OrderItems;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItems, Long>{
	
	@Modifying
	@Query(value ="DELETE FROM order_items  WHERE id = ?1", nativeQuery = true)
	@Transactional
	public void deleteOrderItem(long orderItemId);
}
