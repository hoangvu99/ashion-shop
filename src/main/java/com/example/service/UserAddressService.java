package com.example.service;

import com.example.model.user.UserAddress;

public interface UserAddressService {
	
	UserAddress findUserAddressByUserId(long userId);
	
	void saveAddress(UserAddress userAddress);
}
