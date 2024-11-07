package com.example.DepartmentProj.depServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders; // Correct import for Spring HTTP Headers
import org.springframework.http.HttpMethod; // Correct import for Spring HTTP Methods
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;    
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.DepartmentProj.departmentData;
import com.example.DepartmentProj.Dto.Employee;

import com.example.DepartmentProj.Dto.employeeDTO;
import com.example.DepartmentProj.Exceptions.DeleteByIdExp;
import com.example.DepartmentProj.Exceptions.DepartmentNotFound;
import com.example.DepartmentProj.Exceptions.EmployeeCountException;
import com.example.DepartmentProj.Exceptions.PhoneNumberExistsException;
import com.example.DepartmentProj.Exceptions.UpdateDepartmentExp;
import com.example.DepartmentProj.depRepository.departmentRepository;
import com.example.DepartmentProj.restTempletConfigure.ResourceNotFoundException;
import com.example.DepartmentProj.restTempletConfigure.TransactionAspect;
import com.example.DepartmentProj.restTempletConfigure.TransactionAspect.UndoOperation;

import jakarta.ws.rs.BadRequestException;




@Service
public class departmentServices {

    @Autowired
    public departmentRepository depRepo;
    
    @Autowired
    public TransactionAspect TrAspect;
    
//    @Autowired
    private RestTemplate restTemplate;
    
    public departmentServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    private static final String ADD_EMPLOYEESTREAM = "http://EMPLOYEESTREAM/employeeDetails/save";
    private static final String EMPLOYEESTREAM_URL = "http://EmployeeStream/employeeDetails/fetch";
    private static final String DELETE_EMPLOYEE_URL = "http://EMPLOYEESTREAM/employeeDetails/deleteEmployee/{id}";

//    private final List<UndoOperation> undoOperations = new ArrayList<>();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(departmentServices.class);
//-----------------------------------------------------------------------------------------------------------------  
//-------------------ADD All Departments Data------------------------------------------------------------
    public List<departmentData> addUser(List<departmentData> users) {
        
    	  try {
    	// Validate and process each department in the list
        for (departmentData user : users) {
            // Validate number of employees
            if (user.getNumberOfEmployees() > 100) {
                logger.error("Number of employees exceeds the allowed limit of 100 for department: " + user.getId());
                throw new IllegalArgumentException("Number of employees cannot be more than 100");
            }

            if (user.getNumberOfEmployees() == 110) {
                throw new EmployeeCountException("Number of employees cannot be 110 for department: " + user.getId());
            }
        }

        // Save the list of departments
        List<departmentData> savedDepartments = depRepo.saveAll(users);

        // Return the list of saved departments
        return savedDepartments;
    	  } catch (Exception e) {
              logger.error("Error while adding department: {}", e.getMessage());
              throw new RuntimeException("An unexpected error occurred while adding the department");
          }
    }

//-----(or)--------------------------------------------------------------------------------- 
    public List<departmentData> addAllData (List<departmentData> data) {
    	return depRepo.saveAll(data);
    }
//-----------------------------------------------------------------------------------------
    
    
//--------------ADD Department Data-----------------------------------------------------------
    
    public departmentData addDep(departmentData department) {
        try {
        	 
        	// Validate number of employees
            if (department.getNumberOfEmployees() > 100) {
                logger.error("Number of employees exceeds the allowed limit of 100 for department: " + department.getId());
                throw new IllegalArgumentException("Number of employees cannot be more than 100");
            }

            if (department.getNumberOfEmployees() == 110) {
                throw new EmployeeCountException("Number of employees cannot be 110 for department: " + department.getId());
            }
            
            Optional<departmentData> existingDepartmentWithPhoneNumber = depRepo.findByPhoneNumber(department.getPhoneNumber());
            if (existingDepartmentWithPhoneNumber.isPresent()) {
                throw new PhoneNumberExistsException("Phone number already exists");
            }

            // Retrieve all department data from the repository
            List<departmentData> allDepartmentsList = depRepo.findAll();
            
            // Find the maximum ID from the existing departments
            Integer latestId = allDepartmentsList.stream()
                    .map(departmentData::getId)
                    .max(Integer::compareTo)
                    .orElse(null);

            // Generate a new ID based on the maximum ID
            int newId = (latestId == null) ? 1 : latestId + 1;
            department.setId(newId);

            logger.info("Adding department with details: {}", department);
            departmentData Data = depRepo.save(department);
            
//            int i = 10 / 0;
            return Data;
        
        } catch (PhoneNumberExistsException e) {
            logger.error("Error while adding department: {}", e.getMessage());
            throw e; //  custom exception is thrown
        } catch (Exception e) {
            logger.error("Error while adding department: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while adding the department");
        }
    }

//------------ADD employee and department---------------------------------
    
//public departmentData addEmpDep(departmentData department) {
//	ResponseEntity<employeeDTO> depSaveRes = null;
////	 Employee savedEmployee = null; 
//	try {
//
////		  Optional<departmentData> existingDepartmentWithPhoneNumber = depRepo.findByPhoneNumber(department.getPhoneNumber());
////          if (existingDepartmentWithPhoneNumber.isPresent()) {
////              throw new PhoneNumberExistsException("Phone number already exists");
////          }
//			List<departmentData>allusersList = depRepo.findAll();
//			Integer latestId = allusersList.stream()
//					.map(departmentData::getId)
//					.max(Integer::compareTo)
//					.orElse(null);
//			
//			int newId = latestId == null? 1: latestId +1;
//			System.out.println("sdfd");
//			
//			employeeDTO emp = new employeeDTO();
//			emp.setFirstName("sai");
//			emp.setLastName("sandeep");
//			emp.setAge(25);
//			emp.setGender("Male");
//			emp.setSalary(25000.45);
//			emp.setEmail("sandeep@gmail.com");
//			emp.setPhoneNumber("9866873945");
////			emp.setAddress();
////			emp.setTechnicalSkills("java");
////			emp.setDateOfBirth();
//			emp.setDesignation("Engineer");
//			emp.setOtherSkills("java");
//			emp.setExperienced(true);
//			emp.setActive(true);
//			emp.setExperience(1);
////			emp.setDepartmentId();
//			
//			System.out.println("sdfd-------");
//
//	ResponseEntity<Employee> empSave = restTemplate.postForEntity(ADD_EMPLOYEESTREAM, emp, Employee.class);
//
//    if (empSave.getStatusCode() == HttpStatus.OK) {
//        Employee savedEmployee = empSave.getBody();
//        logger.info("Employee data saved: {}", savedEmployee);
//        
//        // Register an undo operation to delete the employee
//        TrAspect.add(() -> {
//            try {
//                restTemplate.delete(DELETE_EMPLOYEE_URL, savedEmployee.getId());
//                logger.info("Deleted employee with ID: {}", savedEmployee.getId());
//            } catch (Exception e) {
//                logger.error("Failed to delete employee: {}", e.getMessage());
//            }
//        });
//    } else {
//        logger.error("Failed to save employee: {}", empSave.getStatusCode());
//        throw new RuntimeException("Failed to save employee");
//    }
//		
//
//		
//		ResponseEntity<Employee[]> response = restTemplate.getForEntity(EMPLOYEESTREAM_URL, Employee[].class);
//        Employee[] employees = null;
//
//        if (response.getStatusCode() == HttpStatus.OK && employees != null) {
//            logger.info("Retrieved Employees: {}", Arrays.stream(employees).map(Employee::toString).collect(Collectors.joining(", ")));
//        } else {
//            logger.info("No Employees retrieved or failed with status code: {}", response.getStatusCode());
//        }
//        
//        List<employeeDTO> employeeList = new ArrayList<>();
//        employeeList.add(emp);
//        department.setEmployees(employeeList);
//        // Randomly pick one employee and assign their ID to the department
//        if (employees != null && employees.length > 0) {
//            Employee firstEmployee = employees[new Random().nextInt(employees.length)];
//            department.setId(newId);
//            department.setNumberOfEmployees(firstEmployee.getId());
//            department.setEmployees(employeeList);
//        }
//
//        // Save department data
//        departmentData savedDepartment = depRepo.save(department);
//        logger.info("Department saved: {}", savedDepartment);
//
//       
//        int i = 10 / 0;
//        undoOperations.clear();
//        return savedDepartment;
//
//    } catch (RuntimeException e) {
//    	
//    	 TrAspect.rollback();
//        logger.error("Error while processing department data: {}", e.getMessage());
//        throw new ResourceNotFoundException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//			
//			
//		
//	
//}
//private void rollback() {
//	  for (UndoOperation undoOperation : undoOperations) {
//	   try {
//	    undoOperation.undo();
//	   } catch (Exception e) {
//	    logger.error("Error while rolling back: {}", e.getMessage());
//	   }
//	  }
//	 }
//    
   

    public departmentData addEmpDep(departmentData department) {
       
        try {
        	 Optional<departmentData> existingDepartmentWithPhoneNumber = depRepo.findByPhoneNumber(department.getPhoneNumber());
             if (existingDepartmentWithPhoneNumber.isPresent()) {
                 throw new PhoneNumberExistsException("Phone number already exists");
             }

            // List of all departments
            List<departmentData> allusersList = depRepo.findAll();
            Integer latestId = allusersList.stream()
                    .map(departmentData::getId)
                    .max(Integer::compareTo)
                    .orElse(null);
            
           
            // Creating a new employee DTO
            employeeDTO emp = new employeeDTO();
            emp.setFirstName("jhon");
            emp.setLastName("Doe");
            emp.setAge(25);
            emp.setGender("Male");
            emp.setSalary(25000.45);
            emp.setEmail("sandeep@gmail.com");
            emp.setPhoneNumber("9866873945");
            emp.setDesignation("Engineer");
            emp.setOtherSkills("java");
            emp.setExperienced(true);
            emp.setActive(true);
            emp.setExperience(1);

            // Saving the employee
            ResponseEntity<Employee> empSave = restTemplate.postForEntity(ADD_EMPLOYEESTREAM, emp, Employee.class);

            if (empSave.getStatusCode() == HttpStatus.OK) {
                Employee savedEmployee = empSave.getBody();
                logger.info("Employee data saved: {}", savedEmployee);

                   // Register an undo operation to delete the employee
                TrAspect.addUndoOperation (() -> {
                    try {
                        restTemplate.delete(DELETE_EMPLOYEE_URL, savedEmployee.getId());
                        logger.info("Deleted employee with ID: {}", savedEmployee.getId());
                    } catch (Exception e) {
                        logger.error("Failed to delete employee: {}", e.getMessage());
                    }
                });
            } else {
                logger.error("Failed to save employee: {}", empSave.getStatusCode());
                throw new RuntimeException("Failed to save employee");
            }

            // Fetching all employees
            ResponseEntity<Employee[]> response = restTemplate.getForEntity(EMPLOYEESTREAM_URL, Employee[].class);
            Employee[] employees = response.getBody();  // Correctly assign the response body

            if (response.getStatusCode() == HttpStatus.OK && employees != null) {
                logger.info("Retrieved Employees: {}", Arrays.stream(employees).map(Employee::toString).collect(Collectors.joining(", ")));
            } else {
                logger.info("No Employees retrieved or failed with status code: {}", response.getStatusCode());
            }

            List<employeeDTO> employeeList = new ArrayList<>();
            employeeList.add(emp);
            department.setEmployees(employeeList);

            
            int newId = latestId == null ? 1 : latestId + 1;

            if (employees != null && employees.length > 0) {
//                Employee firstEmployee = employees[new Random().nextInt(employees.length)];
                department.setId(newId);
                //department.setNumberOfEmployees(firstEmployee.getId());
            }

            // Save department data
            departmentData savedDepartment = depRepo.save(department);
            logger.info("Department saved: {}", savedDepartment);
//int i = 10/0;
            return savedDepartment;
        } catch (PhoneNumberExistsException e) {
            logger.error("Error while adding department: {}", e.getMessage());
            throw e;
//            throw new PhoneNumberExistsException("Phone number already exists");
        } catch (RuntimeException e) {
            TrAspect.rollback();  // Rollback on error
            logger.error("Error while processing department data: {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  
  



//public departmentData addDep(departmentData data) {
//	return depRepo.save(data);
//}
     
    
//--------------------------get Phone Numbers---------------------------------------------------------  
    public List<String> getPhoneNumbers() {
        List<departmentData> employees = depRepo.findAll();
        return employees.stream()
              .map(departmentData::getPhoneNumber)
              .distinct()
              .toList();
    } 
//------------------------------------------------------------------------------------------
    public List<departmentData> getAllDepartmentData() {
    	logger.info("Employees info is logging enabled ");
    	logger.debug("Employees debug is logging enabled");
    	logger.error("getAllDepartmentData-----------------link not found");
    	logger.trace("This is a TRACE message");
    	System.out.println("--successfully getAllDepartmentData--");
    	return depRepo.findAll();
    }
    

 
    
    public List<departmentData> getAllDepartmentWithEmployee() {
    	
    	
        // Create headers and add authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWlzYW5kZWVwIiwiaWF0IjoxNzI0ODE5NDM5LCJleHAiOjE3MjQ5MDU4Mzl9.-tS3dlJ1DUCFXSPZM9klG426cllz_E5su-CetiDhtsI"); // Replace with actual token
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Employee[]> employeeResponse = restTemplate.exchange(
                EMPLOYEESTREAM_URL, HttpMethod.GET, entity, Employee[].class
            );
            
            System.out.println("Raw API Response: " + employeeResponse);
            
            Employee[] employeesArray = employeeResponse.getBody();
            if (employeesArray == null) {
                System.out.println("No employees found from the remote service.");
                return Collections.emptyList(); // Return an empty list
            }

            // Convert the array to a list
            List<Employee> employees = List.of(employeesArray);

            // Log the employees to see what's being retrieved
            employees.forEach(employee ->
                System.out.println("Employee ID: " + employee.getId() + ", Department ID: " + employee.getDepartmentId())
            );

            // Check for employees with null departmentId
            long countWithNullDepartmentId = employees.stream()
                .filter(employee -> employee.getDepartmentId() == null)
                .count();
            System.out.println("Number of employees with null Department ID: " + countWithNullDepartmentId);

            // Filter out employees with null departmentId and log the count
            List<Employee> validEmployees = employees.stream()
                .filter(employee -> employee.getDepartmentId() != null)
                .collect(Collectors.toList());

            System.out.println("Number of valid employees: " + validEmployees.size());

            // Check if we have valid employees to process
            if (validEmployees.isEmpty()) {
                System.out.println("No employees could be grouped by departmentId.");
                return Collections.emptyList(); // Return an empty list
            }

            // Group employees by department ID
            Map<String, List<Employee>> employeesByDepartment = validEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartmentId));
            System.out.println("employeesByDepartment"+employeesByDepartment);

            // Convert to departmentData
            List<departmentData> departmentDataList = employeesByDepartment.entrySet().stream()
                .map(entry -> {
                    String departmentId = entry.getKey();
                    List<Employee> employeesInDepartment = entry.getValue();

                    // Create a departmentData entity
                    departmentData departmentData = new departmentData();
                    departmentData.setId(Integer.parseInt(departmentId)); // Assuming departmentId can be parsed to an int
                    departmentData.setDepartmentName("Department Name"); // Replace with actual logic
                    departmentData.setNumberOfEmployees(employeesInDepartment.size());
                    departmentData.setRolesandWorkTypes("Roles and Work Types"); // Replace with actual logic
                    departmentData.setActive(true); // Replace with actual logic
                    departmentData.setPhoneNumber("Phone Number"); // Replace with actual logic

                    // Map employees to EmployeeDTO
                    List<employeeDTO> employeeDtos = employeesInDepartment.stream()
                        .map(employee -> {
                            employeeDTO employeeDto = new employeeDTO();
                            employeeDto.setId(employee.getId());
                            employeeDto.setFirstName(employee.getFirstName());
                            employeeDto.setLastName(employee.getLastName());
                            employeeDto.setAge(employee.getAge());
                            employeeDto.setGender(employee.getGender());
                            employeeDto.setSalary(employee.getSalary());
                            employeeDto.setEmail(employee.getEmail());
                            employeeDto.setPhoneNumber(employee.getPhoneNumber());
                            employeeDto.setAddress(employee.getAddress());
                            employeeDto.setTechnicalSkills(employee.getTechnicalSkills());
                            employeeDto.setDateOfBirth(employee.getDateOfBirth());
                            employeeDto.setDesignation(employee.getDesignation());
                            employeeDto.setOtherSkills(employee.getOtherSkills());
                            employeeDto.setExperienced(employee.getExperienced());
                            employeeDto.setActive(employee.getActive());
                            employeeDto.setExperience(employee.getExperience());
                            return employeeDto;
                        })
                        .collect(Collectors.toList());

                    departmentData.setEmployees(employeeDtos);
                    return departmentData;
                })
                .collect(Collectors.toList());

            return departmentDataList;

        } catch (HttpClientErrorException.Forbidden e) {
            // Log the error details
            System.err.println("Access forbidden: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Log and rethrow other exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw e;
        }
    }

   //-----------------------Find By Depatment Id -----------------------------------------------------
    
     public departmentData depfindById(int id) throws DepartmentNotFound {
        try {
    	Optional<departmentData> dep = depRepo.findById(id);
        if (dep.isPresent()) {
            return dep.get();
        } else {
            throw new DepartmentNotFound("" + id );
        }
        }catch (Exception e){
        	throw new DepartmentNotFound("department not found with id : " + e.getMessage()); // department not found with id 1
        }
    }

//---------------------Update Department------------------------------------
// frontend edit menthod 

    
    public departmentData updateDepartment(departmentData data) throws UpdateDepartmentExp {
        try {
            Optional<departmentData> existingDep = depRepo.findById(data.getId());

            if (existingDep.isPresent()) {  // Check if department exists
                departmentData dep = existingDep.get();
                dep.setDepartmentName(data.getDepartmentName());
                dep.setNumberOfEmployees(data.getNumberOfEmployees());
                dep.setRolesandWorkTypes(data.getRolesandWorkTypes());
                dep.setActive(data.isActive());
                dep.setPhoneNumber(data.getPhoneNumber());
               
                departmentData Data = depRepo.save(dep);  // Save updated department
//                int i = 10 / 0;
                return Data;
            }
            else {
            	throw new UpdateDepartmentExp(""+data.getId() );
            }

        } catch (Exception e) {
            // Handle any other exceptions that might occur
            throw new UpdateDepartmentExp("An unexpected error occurred while updating the Department Data" + e.getMessage());
        }
       
    }
    
    public departmentData saveOrUpdate(departmentData depdata) {
        return depRepo.save(depdata);
    }

//    public departmentData  depfindById(int id) throws departmentNotFound{
//    	Optional<departmentData> dep = depRepo.findById(id);
//return dep.orElseThrow(() -> new departmentNotFound ("department not found with id " + id));
//
//    }

//-----------------Delete By Id ----------------------------------------------

//    public void deleteById(int id) {
//        depRepo.deleteById(id);
//    }
    
//    public String deleteById(int id) throws DeleteByIdExp{
//    	
//    	try {
//        if (!depRepo.existsById(id)) {
//        	logger.debug("deEmployees debug is logging enabled");
//
//            throw new DeleteByIdExp("Department id not found with this id: " + id);
//        }
//        else {
//        	logger.debug("deEmployees debug is logging enabled");
//        	 depRepo.deleteById(id);
////        	 int i= 10/0;
//        	return ("Successfully deleted department with id " + id);
//        }
//        
//    	}catch(Exception e) {
//    		logger.error("Employees debug is logging enabled");
//
//    		throw new DeleteByIdExp("An unexpected error occurred while deleting department with id: " + id); 
//    	}
//    }
    
    public String deleteById(int id) throws DeleteByIdExp {
        try {
            if (!depRepo.existsById(id)) {
                logger.debug("Department with id " + id + " does not exist.");
//                int i = 10 / 0;
                throw new DeleteByIdExp("Department id not found with this id: " + id);
                
            } else {
                
              
//            	int i = 10 / 0; //frontend test 

               depRepo.deleteById(id);

                return "Successfully deleted department with id " + id;
                
            }
        } catch (DeleteByIdExp e) {
            logger.error("DeleteByIdExp occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting department with id " + id, e);
            throw new DeleteByIdExp("An unexpected error occurred while deleting department with id: " + id);
        }
    }

    
//    public void deleteSelectedId(int id){
//    	depRepo.deleteAllById(id);
//    }
    
    public void deleteSelectedIds(List<Integer> ids) {
        depRepo.deleteAllByIdIn(ids);
    }

    	
    public List<departmentData> sortByDepartmentName(List<departmentData> data) {
        // Sort the list by departmentName
        return data.stream()
                .sorted((d1, d2) -> d1.getDepartmentName().compareTo(d2.getDepartmentName()))
                .collect(Collectors.toList());
    }

   
}
