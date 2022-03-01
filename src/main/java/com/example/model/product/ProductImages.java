package com.example.model.product;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product_image")
public class ProductImages {
	
	private long id;
	private String url;
	private Product product;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
		
}
