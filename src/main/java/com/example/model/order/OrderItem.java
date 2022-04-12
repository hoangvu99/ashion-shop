package com.example.model.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.model.cart.Cart;
import com.example.model.product.Product;
import com.example.model.promotion.Promotion;
import com.example.model.size.Size;
import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name ="order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "size_id")
	private Size size;

	
	private int quantity;
	private double total;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
