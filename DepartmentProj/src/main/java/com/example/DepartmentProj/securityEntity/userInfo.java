package com.example.DepartmentProj.securityEntity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "User")
public class userInfo {
	
	private int id;
	private String name;
	private String email;
	private String password;
	 private String roles;

}
