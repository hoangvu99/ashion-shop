package com.example.model.user;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.model.order.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String userName;
	private String email;
	private String password;
	private long phone;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<UserAddress> userAddresses;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<UserReview> userReviews;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<Order>orders;
	
	private String createdAt;
	
	private String updatedAt;
	
	private String deletedAt;
}
