package com.example.model.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.model.user.User;
import com.example.model.user.UserAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String url;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
		
}
