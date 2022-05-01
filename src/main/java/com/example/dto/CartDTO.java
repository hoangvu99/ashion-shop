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
	private long cartTotal;
	
	public void calTotal() {
		long total = 0;
		for (int i = 0; i < this.cartItemDTOs.size(); i++) {
			total += this.cartItemDTOs.get(i).getTotal();
		}
		
		this.cartTotal= total;
	}
}
