package io.javabrains.springsecurityjpa;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.springsecurityjpa.models.Thing;

@RestController
public class ThingResource {
	
	@Autowired
	ThingService ts;

    @GetMapping("/admin/addThings")
    public String addThings() {
    	 
    	 
    	Thing t = ts.createThing("employee");
    	String s = "<h1>hello from add things</h1>";
    	s += "thins added" + t;
        return (s);
        
    }    
    
}

