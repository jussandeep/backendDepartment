package com.example.DepartmentProj.securityFilter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.DepartmentProj.securityService.JWTservice;
import com.example.DepartmentProj.securityService.userInfoUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public  class JWTAutherFilter extends OncePerRequestFilter{

	
	@Autowired
	private JWTservice JWTServ;
	
	@Autowired
	private userInfoUserDetailsService userIUDService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	
		 response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
	        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	        response.setHeader("Access-Control-Expose-Headers", "Authorization");

	        String authHeader = request.getHeader("Authorization");
	        String token = null;
	        String email = null;
	        
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            email = JWTServ.extractUsername(token);
	        }
	        
	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	UserDetails userDetails = userIUDService.loadUserByUsername(email);
	            if (JWTServ.validateToken(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }
	        filterChain.doFilter(request, response);
	}
}
