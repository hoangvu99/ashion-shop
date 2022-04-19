package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.category.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer>{
	
	@Query(value = "select * from category where id = ?1", nativeQuery = true)
	public Category findCategoryById(int id);
}
