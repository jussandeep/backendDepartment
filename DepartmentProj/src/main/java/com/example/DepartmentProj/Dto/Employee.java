package com.example.DepartmentProj.Dto;

import java.time.LocalDate;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private Double salary;
    private String email;
    private String phoneNumber;
    private Address address; // Use a String or another class as needed
    private List<String> technicalSkills;
    private LocalDate dateOfBirth;
    private String designation;
    private String otherSkills;
    private Boolean experienced;
    private Boolean active;
    private Integer experience;
    private String departmentId; // Add this if needed for grouping

    // Getters and Setters
    // ... (omitted for brevity)
}