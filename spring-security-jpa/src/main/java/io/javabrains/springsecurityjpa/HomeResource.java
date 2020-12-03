package io.javabrains.springsecurityjpa;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.springsecurityjpa.models.User;

@RestController
public class HomeResource {
    
    private String getUserInfo() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = "unknown";
    	String roles = "";

    	if (principal instanceof User) {

    	   username = ((User)principal).getUserName();
    	   
    	   Collection<SimpleGrantedAuthority> auth =  ((User)principal).getGrantedAutorities();
    	   
    	   Object[] arrauth = auth.toArray();
    	   for( int i =0 ; i< arrauth.length; i++) {
    		   SimpleGrantedAuthority role = (SimpleGrantedAuthority)arrauth[i];
    		   roles += role.getAuthority(); 
    		   roles+=",";
    	   }
    	} else {

    	   username = principal.toString();
    	}
    	return username + " roles: " + roles;
    }
/*
    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome " + getUserInfo() + "</h1>");
    }    
 */   
    @GetMapping("/user/welcome")
    public String user() {
        return ("<h1>Welcome User named:" + getUserInfo() + "</h1>");
    }
    
    @GetMapping("/currentUser")
    public String admin() {
        return ("<h1>Welcome Admin2 named:" + getUserInfo() + "</h1>");
    }
    
  
}

