package com.example.dto;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.example.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private String category;
	private String productName;
	private String description;
	private MultipartFile thumnail;
	private MultipartFile[] images;
	private int price;
	private String detail;
	
	
}
