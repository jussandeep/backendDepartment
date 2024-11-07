package com.example.DepartmentProj.securityService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.DepartmentProj.securityConfig.userInfoUserDetails;
import com.example.DepartmentProj.securityEntity.userInfo;
import com.example.DepartmentProj.securityRepository.userInfoRepository;


@Component
public class userInfoUserDetailsService implements UserDetailsService{

	
	@Autowired
	private userInfoRepository userRepo;
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 Optional<userInfo> userInfo = userRepo.findByEmail(email);
		 return userInfo.map(userInfoUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + email));

	    }
	
	
	
	}
