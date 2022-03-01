package com.example.model.user;

import java.util.Set;

public class User {
	
	private long id;
	private String userName;
	private String email;
	private String password;
	private long phone;
	private Set<UserAddress> userAddresses;
	private Set<UserReview> userReviews;
}
