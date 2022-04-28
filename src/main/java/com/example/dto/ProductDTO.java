package com.example.dto;

import java.io.File;
import java.util.List;

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
	private List<MultipartFile> images;
	private String price;
	private String detail;
	private List<SizeDTO>sizes;
	
	
	
}
