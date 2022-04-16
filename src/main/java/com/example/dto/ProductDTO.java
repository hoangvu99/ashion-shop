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
	private User user;
	private MultipartFile file;
	private MultipartFile[] multipartFile;
	
	
}
