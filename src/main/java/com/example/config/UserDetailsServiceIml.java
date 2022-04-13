package com.example.config;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.dao.UserDao;
import com.example.model.user.User;

@Service
public class UserDetailsServiceIml implements UserDetailsService{
	
	@Autowired
	UserDao userDao;
	
	@Override	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// dùng email để đăng nhập
		System.out.println("?");
		User user = userDao.findUserByEmail(username);
		
		if(user == null) {
			
			throw new UsernameNotFoundException("User Not found");
		}
		System.out.println("dang nhap thanh cong");
		MyUserDetails myUserDetails = new MyUserDetails(user);
		return myUserDetails;
		
		
	}

}
