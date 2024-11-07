package com.example.DepartmentProj.depServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.DepartmentProj.departmentData;
//import com.example.DepartmentProj.Dto.Employee;
import com.example.DepartmentProj.Dto.employeeDTO;
import com.example.DepartmentProj.Exceptions.DepartmentNotFound;
import com.example.DepartmentProj.Exceptions.EmployeeException;
import com.example.DepartmentProj.Exceptions.PhoneNumberExistsException;
import com.example.DepartmentProj.depRepository.departmentRepository;
import com.example.DepartmentProj.restTempletConfigure.ResourceNotFoundException;
import com.example.DepartmentProj.restTempletConfigure.TransactionAspect;
import com.example.DepartmentProj.restTempletConfigure.TransactionAspect.UndoOperation;

@Service
public class AddUpdateDelete {
    private static final String RETRIEVE_ALL_EMPLOYEES = "http://EMPLOYEESTREAM/employeeDetails/fetch";
    private static final String ADD_EMPLOYEESTREAM = "http://EMPLOYEESTREAM/employeeDetails/save";
    private static final String UPDATE_EMPLOYEE = "http://EMPLOYEESTREAM/employeeDetails/UpdateEmployeeWithId/";
    private static final String DELETE_EMPLOYEE_URL = "http://EMPLOYEESTREAM/employeeDetails/deleteEmployee/";
    private static final String EMPLOYEE_BY_ID = "http://EMPLOYEESTREAM/employeeDetails/getEmployeeById/";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AddUpdateDelete.class);

    @Autowired
    private departmentRepository depRepository;

    @Autowired
    private TransactionAspect TrAspect;

    @Autowired
    private RestTemplate restTemplate;

    
    public departmentData updateEmpDep(departmentData department) throws  ResourceNotFoundException {
        ResponseEntity<employeeDTO> empDataUpdateByIdSaved = null;
        try {
            // Check for existing department with the same phone number
            Optional<departmentData> existingDepartmentWithPhoneNumber = depRepository.findByPhoneNumber(department.getPhoneNumber());
            if (existingDepartmentWithPhoneNumber.isPresent()) {
                throw new PhoneNumberExistsException("Phone number already exists");
            }

            // Determine the new ID for the department
            List<departmentData> allDepartments = depRepository.findAll();
            Integer latestId = allDepartments.stream()
                    .map(departmentData::getId)
                    .max(Integer::compareTo)
                    .orElse(null);

            int newId = latestId == null ? 1 : latestId + 1;

//--------------- Create a new employeeDTO-------------------------
            employeeDTO empDataUpdateById = new employeeDTO();
            empDataUpdateById.setFirstName("jsai");
            empDataUpdateById.setLastName("sandeep");
            empDataUpdateById.setAge(25);
            empDataUpdateById.setGender("Male");
            empDataUpdateById.setSalary(25000.45);
            empDataUpdateById.setEmail("sandeep@gmail.com");
            empDataUpdateById.setPhoneNumber("1266567890");
            empDataUpdateById.setDesignation("Engineer");
            empDataUpdateById.setOtherSkills("java");
            empDataUpdateById.setExperienced(true);
            empDataUpdateById.setActive(true);
            empDataUpdateById.setExperience(1);

 //---------------- Save the new employee--------------------------------------------
           empDataUpdateByIdSaved = restTemplate.postForEntity(ADD_EMPLOYEESTREAM, empDataUpdateById, employeeDTO.class);

            employeeDTO savedEmployee;
            if (empDataUpdateByIdSaved.getStatusCode() == HttpStatus.OK) {
                savedEmployee = empDataUpdateByIdSaved.getBody();
                logger.info("Employee saved: {}", savedEmployee);
                assert savedEmployee != null;
                // Add undo operation to delete the employee if needed
                TrAspect.addUndoOperation(() -> {
                    restTemplate.delete(DELETE_EMPLOYEE_URL + savedEmployee.getId());
                });
            } else {
                logger.error("Failed to save employee: {}", empDataUpdateByIdSaved.getStatusCode());
                throw new RuntimeException("Failed to save employee");
            }

            // Retrieve all employees
            ResponseEntity<employeeDTO[]> response = restTemplate.getForEntity(RETRIEVE_ALL_EMPLOYEES, employeeDTO[].class);
            employeeDTO[] employees = null;
            if (response.getStatusCode() == HttpStatus.OK) {
                employees = response.getBody();
                if (employees != null) {
                    logger.info("Retrieved employees: {}", Arrays.stream(employees)
                            .map(employeeDTO::toString)
                            .collect(Collectors.joining(", ")));
                } else {
                    logger.info("No employees retrieved");
                }
            } else {
                logger.info("Failed to retrieve employees. Status code: {}", response.getStatusCode());
            }

 // ------------Ensure employees are retrieved before proceeding--------------------------------
            // if (employees == null || employees.length == 0) {
            //     throw new RuntimeException("No employees available to assign to department");
            // }

//---------------- Assign a random employee to the department--------------------------------------
            assert employees != null;
            employeeDTO fristEmployee = employees[new Random().nextInt(employees.length)];
            department.setId(newId);
            department.setNumberOfEmployees(fristEmployee.getId());
            logger.info("Department: {}", department);

//--------- Save the department----------------------------------------------------
            departmentData savedDepartment = depRepository.save(department);
            logger.info("Department saved: {}", savedDepartment);

            // Add undo operation to delete the department if needed
            // TrAspect.addUndoOperation(() -> {
            //     depRepository.deleteById(savedDepartment.getId());
            // });

 //------------ Update the employee's first name------------------------------------------
            employeeDTO originalEmployeeData = restTemplate.getForObject(EMPLOYEE_BY_ID + fristEmployee.getId(), employeeDTO.class);
            
            employeeDTO originalEmpCopy = new employeeDTO();
            assert originalEmployeeData != null;
            BeanUtils.copyProperties(originalEmployeeData, originalEmpCopy);
            logger.info("Original Employee Copy: {}", originalEmpCopy);

            employeeDTO updateEmployee = new employeeDTO();
            logger.info("update data with 1", updateEmployee);
            updateEmployee.setFirstName(originalEmpCopy.getFirstName() + 9866); // Assuming you want to append '1'
            logger.info("update data with 1", updateEmployee);
            
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(updateEmployee, originalEmpCopy);

            // Send update request
            ResponseEntity<employeeDTO> updateEmployeeResponse = restTemplate.exchange(UPDATE_EMPLOYEE + fristEmployee.getId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(originalEmpCopy),
                    employeeDTO.class
            );

            if (updateEmployeeResponse.getStatusCode() == HttpStatus.OK) {
                logger.info("Employee updated successfully: {}", updateEmployeeResponse.getBody());
                logger.info("Employee before update: {}", originalEmployeeData);
                
                // Add undo operation to revert the employee update
                TrAspect.addUndoOperation(() -> {
                    // try {
                        restTemplate.put(UPDATE_EMPLOYEE + fristEmployee.getId(), originalEmployeeData, employeeDTO.class);
                    // } catch (Exception e) {
                    //     logger.error("Failed to revert employee update: {}", e.getMessage());
                    // }
                });
            } else {
                logger.error("Failed to update employee: {}", updateEmployeeResponse.getStatusCode());
                throw new RuntimeException("Failed to update employee");
            }

            // Delete an employee 
            int employeeIdToDelete = 41;
            

           //--------------------------------------------------------------------------------- 
            employeeDTO employeeToDelete = restTemplate.getForObject(EMPLOYEE_BY_ID + employeeIdToDelete, employeeDTO.class);
            logger.info("employeeToDelete {}",employeeToDelete);

            // if (employeeToDelete == null) {
            //     throw new RuntimeException("Employee to delete not found");
            // }

            ResponseEntity<Void> deleteResponse = restTemplate.exchange(DELETE_EMPLOYEE_URL + employeeIdToDelete,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Void.class
            );

            if (deleteResponse.getStatusCode() == HttpStatus.OK) {
                logger.info("Employee deleted successfully: {}", employeeIdToDelete);
                
                // Add undo operation to recreate the deleted employee
                TrAspect.addUndoOperation(() -> {
                    restTemplate.postForObject(ADD_EMPLOYEESTREAM, employeeToDelete, employeeDTO.class);
                });
            } else {
                logger.error("Failed to delete employee: {}", deleteResponse.getStatusCode());
                throw new RuntimeException("Failed to delete employee");
            }

            //exception to test rollback
           
//     int i = 10 / 0;

// If all operations succeed, clear the undo operations
         TrAspect.clearUndoOperations();
            return savedDepartment;
        }
//        catch (EmployeeException e) {
//            throw new EmployeeException( e.getMessage());
//        
//        }
         catch (PhoneNumberExistsException e) {
         	throw new PhoneNumberExistsException( e.getMessage());
        
         } 
//        catch (ResourceNotFoundException e) {
//        	throw new ResourceNotFoundException( e.getMessage());
//        
//        }
        catch (RuntimeException e) {
            logger.error("Error while processing department data: {}", e.getMessage());
            TrAspect.rollback();
            throw new ResourceNotFoundException( "Exception Occurred ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


