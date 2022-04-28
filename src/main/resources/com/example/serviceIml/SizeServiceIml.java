package com.example.serviceIml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.SizeDao;
import com.example.model.size.Size;
import com.example.service.SizeService;

@Service
public class SizeServiceIml implements SizeService{
	@Autowired
	SizeDao sizeDao;
	@Override
	public List<String> listSizeName() {
		// TODO Auto-generated method stub
		return sizeDao.listSizeName();
	}
	@Override
	public Size findSizeById(int id) {
		
		return sizeDao.findSizeById(id);
	}

}
