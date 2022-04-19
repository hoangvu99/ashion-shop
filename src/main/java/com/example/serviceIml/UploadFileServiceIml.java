package com.example.serviceIml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.UploadFileService;
@Service
public class UploadFileServiceIml implements UploadFileService{

	@Override
	public void saveUploadFile(MultipartFile file) throws IOException {
		
		try {
			String fileLocation = new File("src\\main\\resources\\static\\img").getAbsolutePath() + "\\" + file.getOriginalFilename();
			System.out.println(fileLocation);
			FileOutputStream output = new FileOutputStream(fileLocation);

			output.write(file.getBytes());

			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
