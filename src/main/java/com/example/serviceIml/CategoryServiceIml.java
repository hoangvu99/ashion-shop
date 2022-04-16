package com.example.serviceIml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.CategoryDao;
import com.example.model.category.Category;
import com.example.service.CategoryService;

@Service
public class CategoryServiceIml implements CategoryService{

	@Autowired
	private CategoryDao categoryDao;
	@Override
	public List<Category> listCategories() {
		return categoryDao.findAll();
	}

}
