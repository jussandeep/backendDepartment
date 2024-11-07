package com.example.DepartmentProj.securityEntity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AutherRequest {
	
	private String email;
//	username
	private String password;

}
