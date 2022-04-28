package com.example.model.cart;

import java.text.NumberFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.example.model.product.Product;
import com.example.model.size.ProductSize;
import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;
	private int quantity;
	private String subTotal;
	private String createdAt;
	private String updatedAt;
	
	@OneToOne
	@JoinColumn(name="product_size_id")
	ProductSize productSize;
	
	private String price;
	
	public void convertTotal( NumberFormat numberFormat) {
		String[] priceArr = this.price.split(",");
		String priceText="";
		for (int i = 0; i < priceArr.length; i++) {
			priceText+=priceArr[i];
		}
		long priceNum = Long.valueOf(priceText);
		long totalNum = priceNum*this.quantity;
		String totalText = numberFormat.format(totalNum);
		
		this.subTotal = totalText;
	}

	
	
}
