package com.example.service;

import javax.mail.MessagingException;

import com.example.model.user.User;

public interface UserService {
	
	public void saveUser(User user);
	
	public void sendVerifyEmail(String email) throws MessagingException;
	
	public void updateUserConfirmEmail(String email);
}
