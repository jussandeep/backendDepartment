package com.example.DepartmentProj;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.DepartmentProj.Dto.employeeDTO;

//import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@Entity
@Document(collection = "departmentData")
public class departmentData {

    @Id
//   @GeneratedValue(strategy = GenerationType.AUTO)
    
    
    
    private int id;

    @NotBlank(message = "departmentName should not be null or empty")
    
    private String departmentName;

    @NotNull(message = "numberOfEmployees should not be null")
    @Min(value = 2, message = "numberOfEmployees should be at least 2")
    @Max(value = 100, message = "numberOfEmployees should be at most 100")
    private int numberOfEmployees;

    @NotBlank(message = "rolesandWorkTypes should not be null or empty")
    private String rolesandWorkTypes; 

    @NotNull(message = "active should not be null")
    private boolean active;

    private String phoneNumber;
    
    private List<employeeDTO> employees;

    public departmentData( int id,
			@NotBlank(message = "departmentName should not be null or empty") String departmentName,
			@NotNull(message = "numberOfEmployees should not be null") @Min(value = 2, message = "numberOfEmployees should be at least 2") @Max(value = 100, message = "numberOfEmployees should be at most 100") int numberOfEmployees,
			@NotBlank(message = "rolesandWorkTypes should not be null or empty") String rolesandWorkTypes,
			@NotNull(message = "active should not be null") boolean active, String phoneNumber) {
		super();

		this.id = id;
		this.departmentName = departmentName;
		this.numberOfEmployees = numberOfEmployees;
		this.rolesandWorkTypes = rolesandWorkTypes;
		this.active = active;
		this.phoneNumber = phoneNumber;

	}
    // Default constructor
    public departmentData() {}

    // Getters and Setters
    public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

    public int getId() {
        return id;
    }
	public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getRolesandWorkTypes() {
        return rolesandWorkTypes;
    }

    public void setRolesandWorkTypes(String rolesandWorkTypes) {
        this.rolesandWorkTypes = rolesandWorkTypes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    

    public List<employeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<employeeDTO> employees) {
        this.employees = employees;
    }
	

	@Override
	public String toString() {
		return "departmentData [id=" + id + ", departmentName=" + departmentName + ", numberOfEmployees="
				+ numberOfEmployees + ", rolesandWorkTypes=" + rolesandWorkTypes + ", active=" + active
				+ ", phoneNumber=" + phoneNumber + "]";
	}
}
