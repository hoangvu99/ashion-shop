package com.example.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.user.User;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Long>{
	
	@Query(value = "SELECT * FROM User u WHERE u.email = ?1", nativeQuery = true)
	User findUserByEmail(String email);
	
	@Query(value=" update user set active = 1 where id = ?1" , nativeQuery =  true)
	public void setActiveUser(long userId);
}
