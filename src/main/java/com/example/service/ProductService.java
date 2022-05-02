package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.dto.ProductDTO;
import com.example.model.product.Product;


public interface ProductService {
	
	Product findProductById(long id);
	
	void saveProduct(ProductDTO productDTO);
	
	void updateProduct(ProductDTO productDTO, long id); 
		
	List<Product>getListProduct();
	
	List<Product> getPageProduct(int limit,int page);
	
	List<Product> searchProductByName(String name);
	
	void saveProduct(Product p);
	
	List<Product> lastestProducts();
	
	public void deleteProduct(long id);
	
	public List<Product> listProductByCategory(int category, int limit, int offset);
	public List<Product> listProductByName(String s, int limit, int offset);
}
