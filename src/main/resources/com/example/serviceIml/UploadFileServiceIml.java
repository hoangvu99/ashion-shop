package com.example.serviceIml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.UploadFileService;
@Service
public class UploadFileServiceIml implements UploadFileService{
	
	@Value("${app.upload.path}")
	private String uploadPath;

	@Override
	public void saveUploadFile(MultipartFile file,String name) throws IOException {
		
		
		
		
		
		
		try {
			if(file != null) {
				
				Path rootLocation = Paths.get(uploadPath+"products/"+name);
				
				Path f = Files.createFile(rootLocation);
				Files.copy(file.getInputStream(),f, 
		                StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
