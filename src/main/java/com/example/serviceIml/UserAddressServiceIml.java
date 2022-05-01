package com.example.serviceIml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.UserAddressDao;
import com.example.model.user.UserAddress;
import com.example.service.UserAddressService;

@Service
public class UserAddressServiceIml implements UserAddressService{
	
	@Autowired
	UserAddressDao userAddressDao;
	@Override
	public UserAddress findUserAddressByUserId(long userId) {
	
		return userAddressDao.findUserAddressByUserId(userId);
	}
	
	@Override
	public void saveAddress(UserAddress userAddress) {
		// TODO Auto-generated method stub
		userAddressDao.saveAndFlush(userAddress);
	}
	
	
}
