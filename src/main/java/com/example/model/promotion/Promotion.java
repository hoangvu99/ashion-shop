package com.example.model.promotion;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Table;

@Entity
@Table(name = "promotion")
public class Promotion {
	
	private long id;
	private String name;
	private double discount;
	private int beginDate;
	private int endDate;
	private int status;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	private Set<ProductPromotion> productPromotions;
}
