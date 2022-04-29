package com.example.model.order;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.model.cart.CartItem;
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
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	private int status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Collection<OrderItem> orderItems;
	private String createdAt;
	private String updatedAt;
	private String orderTotal;
	
	
	
	private String deliveryAddress;
	private long phoneNumber;
	
	
}
