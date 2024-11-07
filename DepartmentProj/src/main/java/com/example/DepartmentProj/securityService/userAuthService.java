package com.example.DepartmentProj.securityService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.DepartmentProj.securityEntity.userInfo;
import com.example.DepartmentProj.securityRepository.userInfoRepository;

@Service
public class userAuthService {
	
	@Autowired
	private userInfoRepository userRepo;
	
	@Autowired
	 private PasswordEncoder passwordEncoder; 
	
	public String addUser(userInfo userInf) {
		userInf.setPassword(passwordEncoder.encode(userInf.getPassword()));
		List<userInfo> alluserList = userRepo.findAll();
		Integer latestId = alluserList.stream()
				.map(userInfo::getId)
				.max(Integer::compareTo)
				.orElse(null);
		
		// Generate a new ID based on the maximum ID
        int newId = latestId == null ? 1 : latestId + 1;

        userInf.setId(newId);
        userRepo.save(userInf);
        return "user added to system ";
	}
	
	public List<userInfo> getallusers(){
		return userRepo.findAll();
	}

	
//========================Update Password with ID===========================================
//	public userInfo update(userInfo userdata) {
//		return userRepo.save(userdata);
//	}
	
	public userInfo update(userInfo userdata) {
		// Check if the password is being updated
		Optional<userInfo> existingUserOpt = userRepo.findById(userdata.getId());
		if (existingUserOpt.isPresent()) {
			userInfo existingUser = existingUserOpt.get();
			
			// If the password has been changed, encode it again
			if (!existingUser.getPassword().equals(userdata.getPassword())) {
				userdata.setPassword(passwordEncoder.encode(userdata.getPassword()));
			}
			
			// Save the updated user information
			return userRepo.save(userdata);
		} else {
			throw new RuntimeException("User not found");
		}
	}
//=========================forgot Password update with Email=========================================

	public userInfo updateFrontEndData(userInfo userFrontendData) {

		Optional<userInfo> existingUserInfo = userRepo.findByEmail(userFrontendData.getEmail());
		
		if (existingUserInfo.isPresent()) {
			userInfo existingUser = existingUserInfo.get();
			
//			existingUser.setPassword(userFrontendData.getPassword());
			
			if (!passwordEncoder.matches(userFrontendData.getPassword(), existingUser.getPassword())) {
			    // Encode the new password before setting it
			    existingUser.setPassword(passwordEncoder.encode(userFrontendData.getPassword()));
			}

		return userRepo.save(existingUser);
		}else {
			 throw new RuntimeException("User not found");		
		//		return userRepo.save(userFrontendData);
			 
		}
		
		
		
		
		
	}
}
