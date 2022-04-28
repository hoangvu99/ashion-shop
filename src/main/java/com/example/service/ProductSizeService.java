package com.example.service;

import java.util.List;

import com.example.model.size.ProductSize;

public interface ProductSizeService {
	
	public List<ProductSize> findProductSizeByProductId(long id);
	
	public ProductSize findProductSizeByProductId(long userId, int sizeId);
}
