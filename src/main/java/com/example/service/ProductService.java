package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.dto.ProductDTO;
import com.example.model.product.Product;


public interface ProductService {
	
	Product findProductById(long id);
	
	void saveProduct(ProductDTO productDTO);
		
	List<Product>getListProduct();
	
	Page<Product> getPageProduct(int page);
	
	List<Product> searchProductByName(String name);

}
