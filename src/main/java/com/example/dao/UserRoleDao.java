package com.example.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.user.UserRole;

@Repository
@Transactional
public interface UserRoleDao extends JpaRepository<UserRole, Integer>{
	
	@Query(value = "select role_id from user_role where user_id= ?1", nativeQuery = true)
	public List<Integer> listRoleID(long userID);
}
