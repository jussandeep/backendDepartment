package com.example.DepartmentProj.Exceptions;

import jakarta.ws.rs.BadRequestException;

public class BadRequestExcep extends BadRequestException{
	public BadRequestExcep (String message) {
		super(message);
	}

}
