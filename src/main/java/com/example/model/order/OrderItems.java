package com.example.model.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.example.model.product.Product;
import com.example.model.size.ProductSize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name ="orders_id")
	private Orders orders;
	
	@OneToOne
	@JoinColumn(name="product_size_id")
	ProductSize productSize;

	
	private int quantity;
	private long total;
	private long price;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
}
