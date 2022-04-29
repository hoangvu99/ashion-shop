package com.example.model.product;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.model.category.Category;
import com.example.model.order.OrderItem;
import com.example.model.promotion.ProductPromotion;
import com.example.model.size.ProductSize;
import com.example.model.user.UserReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String description;
	private String thumnail;
	private int status;
	private String createdAt;
	private String createdBy;				
	private String updatedAt;
	private String deletedAt;
	private Long priceInNum;
	@Column(columnDefinition = "varchar(2000)")
	private String detailsContent;
	
	private String price;
	private String oldPrice;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Collection<ProductPromotion> productPromotions;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Collection<ProductSize>productSizes;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Collection<ProductImages> productImages;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Collection<UserReview>userReviews;
	
	@OneToMany(mappedBy =  "product", cascade = CascadeType.ALL)
	private Collection<OrderItem>orderItems;
	
	@ManyToOne()
	@JoinColumn(name = "category_Id")
	private Category category;
	

}
