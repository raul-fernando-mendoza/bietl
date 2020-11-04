package io.javabrains.springsecurityjpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleUserService {

	@GetMapping("/SimpleUserService")
    private SimpleUser getCurrentUser() {
		SimpleUser user = new SimpleUser();
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = "unknown";
    	List<String> roles = new ArrayList<>();

    	if (principal instanceof UserDetails) {
    		
    	   username = ((UserDetails)principal).getUsername();
    	   user.setUserName(username);
    	   
    	   Collection<? extends GrantedAuthority> auth =  ((UserDetails)principal).getAuthorities();
    	   
    	   Object[] arrauth = auth.toArray();
    	   
    	   for( int i =0 ; i< arrauth.length; i++) {
    		   SimpleGrantedAuthority role = (SimpleGrantedAuthority)arrauth[i];
    		   String roleName = role.getAuthority(); 
    		   roles.add(roleName);
    	   }
    	   user.setRoles(roles);
    	} 
    	return user;
    }

}

