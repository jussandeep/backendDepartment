package com.example.DepartmentProj.depController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.DepartmentProj.departmentData;

import com.example.DepartmentProj.Exceptions.DeleteByIdExp;
import com.example.DepartmentProj.Exceptions.DepartmentNotFound;
import com.example.DepartmentProj.Exceptions.UpdateDepartmentExp;
import com.example.DepartmentProj.depServices.AddUpdateDelete;
import com.example.DepartmentProj.depServices.departmentServices;






@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/departmentDetails")
public class departmentController {

    @Autowired
    public departmentServices depServ;
    
    @Autowired
    public AddUpdateDelete AUDService;
    
    @PostMapping("/departmentData")
    public List<departmentData> save(@RequestBody List<departmentData> department) {
       
       return depServ.addUser(department);
    }

    @PostMapping("/saveAddUpdateDelete")
    public departmentData saveAddUpdateDelete(@RequestBody departmentData depData) {
    	return AUDService.updateEmpDep(depData);
    }
    
    @PostMapping ("/postData")
    public departmentData postdata(@RequestBody departmentData depdata) {
    	return depServ.addDep(depdata);
    }
 //------------------------------------------------------------------------
    @GetMapping("/AddEmpAndDep")
    public departmentData addEmpDep(@RequestBody departmentData depempData) {
    	return depServ.addEmpDep(depempData);
    }	
    
    
//-------------------------------------------------------------------
    @GetMapping("/depAllData")
    
    public  List<departmentData> getAllUsers() {
    	
        return depServ.getAllDepartmentData();
    }
    

    @GetMapping("/getAllDepAndEmpData")
    public List<departmentData> getAllData() {
        
        return  depServ.getAllDepartmentWithEmployee();
    }
 
   //-----------------------------------------------------------------------
//  @GetMapping("/depDataFindById/{id}")
//  public departmentData findById(@PathVariable int id) throws departmentNotFound {
//      return depServ.depfindById(id);
//  }


  
  @GetMapping("/depDataFindById/{id}")
  public ResponseEntity<departmentData> findById(@PathVariable int id) throws DepartmentNotFound {
  	departmentData department = depServ.depfindById(id);
  	return ResponseEntity.ok(department);
  }
//------------------------------------------------------------------------
      
//    @PutMapping("/department/{id}")
//    public departmentData updateSave(@PathVariable int id, @RequestBody departmentData data) {
//        data.setId(id);
//        return depServ.updateDepartment(data);
//    }
    @PutMapping("/depupDate/{id}")
    public ResponseEntity<?> updateDep(@PathVariable int id, @RequestBody departmentData data)  throws UpdateDepartmentExp {
    	data.setId(id);
    	departmentData updateDepartment = depServ.updateDepartment(data);
    	return ResponseEntity.ok(updateDepartment);
    	
    			
//        data.setId(id);  // Set ID from path variable
//        try {
//            // Call service method to update department
//            departmentData updatedDepartment = depServ.updateDepartment(data);
//            return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);  // Return updated department
//        } catch (UpdateDepartmentExp e) {
//            // Handle exception if department is not found
//            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
//        }
    }

    @PutMapping("/updateData")
    public departmentData updateDep(@RequestBody departmentData depdata) {
        return depServ.saveOrUpdate(depdata);
    }

//---------------------------------------------------------------------------

//    @DeleteMapping("/deleteDepartmentData/{id}")
//    public void deleteId(@PathVariable int id) {
//        depServ.deleteById(id);
//    }
    
    @DeleteMapping("/deleteDepartmentData/{id}")
    public ResponseEntity<String> deleteId(@PathVariable int id) throws DeleteByIdExp{
    
    	String message = depServ.deleteById(id);
        return ResponseEntity.ok (message);
      
       
    }



    @GetMapping("/getPhoneNumbers")
    public List<String> getPhoneNumbers(){
        return depServ.getPhoneNumbers();
    }
    
    
//    @DeleteMapping("/deleteSelectedIds{id}")
//    public void deleteSelectedIds(@PathVariable int id){
//    	depServ.deleteSelectedId(id);
//    }

    @DeleteMapping("/deleteSelectedIds")
    public ResponseEntity<Void> deleteSelectedIds(@RequestBody List<Integer> ids) { 
    	 System.out.println("Received IDs for deletion: " + ids); 
    	depServ.deleteSelectedIds(ids);
        return ResponseEntity.noContent().build();
    }

    
	@GetMapping("/sortByName")
	public List<departmentData>sorting(){
		return depServ.sortByDepartmentName(getAllUsers());
	}



}




