package com.example.DepartmentProj.securityController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.DepartmentProj.securityEntity.AutherRequest;
import com.example.DepartmentProj.securityEntity.userInfo;
import com.example.DepartmentProj.securityService.JWTservice;
import com.example.DepartmentProj.securityService.userAuthService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class securityController {

	@Autowired
	private userAuthService UAService;
	
	@Autowired
	private JWTservice JWTserv;
	
	 @Autowired
	    private AuthenticationManager authenticationManager;

	 

	    @PostMapping("/new")
	    public String addNewUser(@RequestBody userInfo userInfo){
	        return UAService.addUser(userInfo);
	    }
	    
	 @PostMapping("/authenticate")
	    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AutherRequest authRequest) {
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
	        if (authentication.isAuthenticated()) {
	            String token = JWTserv.generateToken(authRequest.getEmail());
	            System.out.println(token);
	            return ResponseEntity.ok(token);
	        } else {
	            throw new UsernameNotFoundException("invalid user request !");
	        }
	    } 
	 
//=======================================================================================================
	//--------get All users Information-------------------------------------
	 
	 @GetMapping("/getAllSignupInformation")
	 public List<userInfo> getusersinfo() {
		 return UAService.getallusers();
	 }
	 
//--------------------Update Password with ID-------------------------------------------------
	 
	 @PutMapping("/updateUserInfo/{id}")
	 public userInfo updateUserInfowithId(@RequestBody userInfo data, @PathVariable int id) {
	data.setId(id);
		 return UAService.update(data);
	 }
	 
//===forgot Password update with Email=======================================
	 @PutMapping("/frontendUserInfoUpdate/{email}")
	 public userInfo frontendUserInfoUpdate (@RequestBody userInfo userInfoData, @PathVariable String email) {
		 userInfoData.setEmail(email);
		 return UAService.updateFrontEndData(userInfoData);
	 }
	 
//	 @PutMapping("/frontendUserInfoUpdate/{email}")
//	 public ResponseEntity<userInfo> frontendUserInfoUpdate(@RequestBody userInfo userInfoData, @PathVariable String email) {
//	     try {
//	         userInfoData.setEmail(email);
//	         userInfo updatedUser = UAService.updateFrontEndData(userInfoData);
//	         return ResponseEntity.ok(updatedUser);
//	     } catch (RuntimeException e) {
//	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//	     }
//	 }

	 
	 
		 
	 
}
