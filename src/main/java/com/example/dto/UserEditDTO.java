package com.example.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO {
	
	private String email;
	
	private long phone;
	private String userName;
	private String province;
	private String district;
	private String commune;
	private String addressDetail;
	
}
