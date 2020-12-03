package io.javabrains.springsecurityjpa.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


public class CurrentUser{
    
    static public String getUserName() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = null;

    	if (principal instanceof User) {
    	   username = ((User)principal).getUserName();
    	}
    	return username;
    }
    static public User getUser() {
    	User user = null;
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (principal instanceof User) {
    		user =  (User) principal;
    	}
    	return user;
    }
    static public List<String>  getRoles() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<String> roles = new ArrayList<String>();

    	if (principal instanceof User) {

    	   Collection<? extends GrantedAuthority> auth =  ((User)principal).getGrantedAutorities();
    	   
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
