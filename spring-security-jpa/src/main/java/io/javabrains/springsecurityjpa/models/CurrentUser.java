package io.javabrains.springsecurityjpa.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class CurrentUser{
    
    static public String getUserName() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = null;

    	if (principal instanceof UserDetails) {
    	   username = ((UserDetails)principal).getUsername();
    	}
    	return username;
    }
    static public List<String>  getRoles() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<String> roles = new ArrayList<String>();

    	if (principal instanceof UserDetails) {

    	   Collection<? extends GrantedAuthority> auth =  ((UserDetails)principal).getAuthorities();
    	   
    	   Object[] arrauth = auth.toArray();
    	   
    	   String roleName = null;
    	   for( int i =0 ; i< arrauth.length; i++) {
    		   SimpleGrantedAuthority role = (SimpleGrantedAuthority)arrauth[i];
    		   roleName = role.getAuthority(); 
    		   roles.add(roleName);
    	   }
    	}  
    	return roles;
    }
    
}
