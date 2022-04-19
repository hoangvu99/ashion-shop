package com.example.serviceIml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ProductDao;
import com.example.model.product.Product;
import com.example.service.ProductService;

@Service
public class ProductServiceIml implements ProductService{
	
	@Autowired
	ProductDao productDao;
	
	@Override
	public Product findProductById(int id) {
		Product product = productDao.findProductById(id);
		return product;
		
	}

	@Override
	public void saveProduct(Product product) {
		if(product != null) {
			productDao.save(product);
		}
		
	}
	

}
