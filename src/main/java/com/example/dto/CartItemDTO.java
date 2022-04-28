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
	private String price;
	private int quantity;
	private String total;

	
	
	public void convertTotal(String price, NumberFormat numberFormat) {
		String[] priceArr = price.split(",");
		String priceText="";
		for (int i = 0; i < priceArr.length; i++) {
			priceText+=priceArr[i];
		}
		long priceNum = Long.valueOf(priceText);
		long totalNum = priceNum*this.quantity;
		String totalText = numberFormat.format(totalNum);
		
		this.total = totalText;
	}
	
	
}
