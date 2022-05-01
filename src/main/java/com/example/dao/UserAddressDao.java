package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.user.UserAddress;

@Repository
public interface UserAddressDao extends JpaRepository<UserAddress, Long>{
	
	
	@Query(value ="select * from user_address where user_id = ?1", nativeQuery = true)
	public UserAddress findUserAddressByUserId(long userId);
}
