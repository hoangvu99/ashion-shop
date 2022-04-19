package com.example.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
	
	 void saveUploadFile(MultipartFile file) throws IOException;
		
	
}
