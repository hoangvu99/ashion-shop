package com.example.dto;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	
	private List<CartItemDTO>cartItemDTOs;
	private int counter;
	private String cartTotal;
	
	public void calculatorCartTotal(NumberFormat numberFormat) {
		String cartTotalText = "";
		long cartTotalNum =0;
		for (int i = 0; i < this.cartItemDTOs.size(); i++) {
			String[] arr = this.cartItemDTOs.get(i).getTotal().split(",");
			String t = "";
			for (int j = 0; j < arr.length; j++) {
				t +=arr[j];
			} 
			cartTotalNum += Long.valueOf(t);
		}
		this.cartTotal = numberFormat.format(cartTotalNum);
		
	}
}
