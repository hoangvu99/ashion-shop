package com.example.serviceIml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ProductSizeDao;
import com.example.model.size.ProductSize;
import com.example.service.ProductSizeService;

@Service
public class ProductSizeServiceiml implements ProductSizeService{
	
	@Autowired
	ProductSizeDao productSizeDao;
	
	@Override
	public List<ProductSize> findProductSizeByProductId(long id) {
		// TODO Auto-generated method stub
		return productSizeDao.findProductSizeByProductId(id);
	}
	
	@Override
	public ProductSize findProductSizeByProductId(long userId, int sizeId) {
		// TODO Auto-generated method stub
		return productSizeDao.findProductSize(userId, sizeId);
	}
}
