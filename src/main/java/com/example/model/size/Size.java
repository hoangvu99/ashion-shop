package com.example.model.size;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.model.order.Order;
import com.example.model.order.OrderItem;
import com.example.model.promotion.Promotion;
import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Size {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	private String sizeName;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	
	@OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
	private Collection<ProductSize>productSizes;
	
	@OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
	private Collection<OrderItem>orderItems;
}
