package com.example.dto;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
	
	private Long productId;
	private int sizeId;
	private String sizeName;
	private String imageName;
	private String productName;
	private long price;
	private int quantity;
	private long total;

	public void calSubTotal() {
		this.total = this.quantity*this.price;
	}
	
	
	
	
}
