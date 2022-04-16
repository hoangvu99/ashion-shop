package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.user.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer>{
	
	@Query(value = "select role_name from role where id = ?1", nativeQuery = true)
	public String getRoleNameById(int id);
}
