package com.example.service;

import java.util.List;

import com.example.model.size.Size;


public interface SizeService {
	
	public List<String> listSizeName();
	
	public Size findSizeById(int id);

}
