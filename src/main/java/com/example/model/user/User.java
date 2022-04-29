package com.example.model.user;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.example.model.order.Order;
import com.example.model.order.Orders;

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
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<UserAddress> userAddresses;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<UserReview> userReviews;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<Orders>orders;
	
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<UserRole>userRoles;
	
	private String createdAt;
	
	private String updatedAt;
	
	private String deletedAt;
	
	private String avatarURL;
	
	private int active;
}
