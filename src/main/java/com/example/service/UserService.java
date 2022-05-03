package com.example.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.example.dto.UserDTO;
import com.example.model.user.User;

public interface UserService {
	
	public void saveUser(UserDTO userDTO);
	
	public void sendVerifyEmail(String email) throws MessagingException;
	
	public void updateUserConfirmEmail(String email);
	
	public User findUserByEmail(String email);
	
	public void sendResetPassToMail(String email) throws MessagingException;
	
	public void resetPass(String email, String pass);
	
	public void updateUserInfo(User user);
	
	public void saveUserAvatar(MultipartFile file, long userId) ;
	
	public List<User> getListUser(int limit, int offset);
	
	
}
