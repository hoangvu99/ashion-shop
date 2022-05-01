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
	private String password;
	private long phone;
	private String userName;
	private String province;
	private String distric;
	private String comne;
	private String addressDetail;
	
}
