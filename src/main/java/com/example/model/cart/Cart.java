package com.example.model.cart;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.example.model.product.Product;
import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private int status;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private Collection<CartItem> cartItems;
	
	private String total;
	private String createdAt;
	private String updatedAt;
	
	private int counter;
	
	public void calculatorCartTotal(NumberFormat numberFormat) {
		String cartTotalText = "";
		long cartTotalNum =0;
		for (int i = 0; i < this.cartItems.size(); i++) {
			String[] arr = ((List<CartItem>) this.cartItems).get(i).getSubTotal().split(",");
			String t = "";
			for (int j = 0; j < arr.length; j++) {
				t +=arr[j];
			} 
			cartTotalNum += Long.valueOf(t);
		}
		this.total = numberFormat.format(cartTotalNum);
		
	}
}
