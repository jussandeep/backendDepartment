package com.example.DepartmentProj.Dto;

import java.time.LocalDate;
import java.util.List;

//import jakarta.mail.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class employeeDTO {


	 private int id;
	    private String firstName;
	    private String lastName;
	    private Integer age;
	    private String gender;
	    private Double salary;
	    private String email;
	    private String phoneNumber;
	    private Address address;
	    private List<String> technicalSkills;
	   
	    private LocalDate dateOfBirth;
	    private String designation;
	    private String otherSkills;
	    private Boolean experienced;
	    private Boolean active;
	    private Integer experience;
}
