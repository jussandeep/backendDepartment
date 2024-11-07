package com.example.DepartmentProj.Exceptions;

public class DepartmentNotFound  extends RuntimeException {
    
	 private static final long serialVersionUID = 1L;

	public DepartmentNotFound(String message) {
        super(message);
    }
}
