package com.example.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.product.Product;
import com.example.model.user.User;

@Repository

public interface UserDao extends JpaRepository<User, Long>{
	
	@Query(value = "SELECT * FROM User u WHERE u.email = ?1", nativeQuery = true)
	User findUserByEmail(String email);
	
	@Modifying
	@Query(value=" update user u set u.active = 1 where u.id = :id" , nativeQuery =  true)
	@org.springframework.transaction.annotation.Transactional
	public void setActiveUser(@Param("id")long userId);
	
	@Modifying
	@Query(value=" update user u set u.password = :pass where u.id = :id" , nativeQuery =  true)
	@org.springframework.transaction.annotation.Transactional
	public void resetPassword(@Param(value ="id")long userId,@Param(value = "pass") String pass);
	
	@Query(value ="select * from user order by id desc limit ?1 offset ?2", nativeQuery = true)
	public List<User> getPageUser(int limit, int offset);
	
	@Query(value = "select * from user where user_name like %?1% ", nativeQuery = true)
	public List<User> findUserByName(String name);
	
	
	@Query(value=" select * from user where id = ?1", nativeQuery = true)
	public User findUserById(long id);
	
	
}
