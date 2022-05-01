package com.example.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;



	

public interface UploadFileService {
	
	
	
	 void saveUploadFile(MultipartFile file,String folder,String name) throws IOException;
		
	
}
