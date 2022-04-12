package com.example.model.promotion;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
public class Promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private double discount;
	private String urlBanner;
	private String beginDate;
	private String endDate;
	private int    status;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	
	@OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
	private Collection<ProductPromotion> productPromotions;
}
