package com.example.config;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.dao.UserRoleDao;
import com.example.model.user.Role;
import com.example.model.user.User;

@Service
public class UserDetailsServiceIml implements UserDetailsService{
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Override	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// dùng email để đăng nhập
		
		User user = userDao.findUserByEmail(username);
		
		if(user == null) {
			
			throw new UsernameNotFoundException("User Not found");
		}
		
		
		
		
		List<Integer>roleIds = userRoleDao.listRoleID(user.getId());
		List<String>roleNames = new ArrayList<String>();
		for( int i : roleIds) {
			String roleName = roleDao.getRoleNameById(i);
			roleNames.add(roleName);
			
		}
		
		MyUserDetails myUserDetails = new MyUserDetails();
		myUserDetails.setUser(user);
		myUserDetails.setRoleNames(roleNames);
		return myUserDetails;
		
		
	}

}
