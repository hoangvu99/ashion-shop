package com.example.serviceIml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
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
	String rootLocation;
	
	@Override
	public void saveUploadFile(MultipartFile file,String folder, String name) throws IOException {
		try {
			Path path = Paths.get(rootLocation+folder+"/"+name);
			Files.createFile(path);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
