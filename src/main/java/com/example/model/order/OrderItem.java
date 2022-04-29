package com.example.model.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.model.cart.Cart;
import com.example.model.product.Product;
import com.example.model.promotion.Promotion;
import com.example.model.size.ProductSize;
import com.example.model.size.Size;
import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	
	private long id;
	
	
	
	private Product product;
	
//	@ManyToOne
//	@JoinColumn(name ="order_id")
//	private Orders orders;
	
	
	ProductSize productSize;

	
	private int quantity;
	private String total;
	private String price;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
