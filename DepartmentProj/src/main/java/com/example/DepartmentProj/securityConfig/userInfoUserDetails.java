package com.example.DepartmentProj.securityConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.DepartmentProj.securityEntity.userInfo;

public class userInfoUserDetails  implements UserDetails {
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public userInfoUserDetails(userInfo useInf) {
    	email= useInf.getEmail();
    	password= useInf.getPassword();
    	authorities= Arrays.stream(useInf.getRoles().split(","))
    			.map(SimpleGrantedAuthority::new)
    			.collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

   
    @Override
    public String getUsername() {
		return email;
	}

	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	

	
    

}
