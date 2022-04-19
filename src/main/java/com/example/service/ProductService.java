package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.product.Product;


public interface ProductService {
	
	Product findProductById(int id);
	
	void saveProduct(Product product);
		
	

}
