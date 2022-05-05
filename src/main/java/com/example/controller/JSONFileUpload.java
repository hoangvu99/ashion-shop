package com.example.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class JSONFileUpload {
	public String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JSONFileUpload(String path) {
		super();
		this.path = path;
	}
	
	
}
