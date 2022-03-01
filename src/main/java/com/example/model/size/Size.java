package com.example.model.size;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "size")
public class Size {
	
	private int id;
	private String sizeName;
	private String createdAt;
	private String updatedAt;
	private String deletedAt;
	private Set<ProductSize>productSizes;
}
