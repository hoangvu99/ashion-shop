package com.example.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String addressDetail;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String district;
	private String province;
	private String commune;
	
	
	private String createdAt;
	
	private String updatedAt;
	
	private String deletedAt;
}
