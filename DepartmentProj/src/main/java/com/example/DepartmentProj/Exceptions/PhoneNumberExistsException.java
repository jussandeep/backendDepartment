package com.example.DepartmentProj.Exceptions;

public class PhoneNumberExistsException extends RuntimeException {
  public PhoneNumberExistsException(String message) {
  super(message);
}
}
