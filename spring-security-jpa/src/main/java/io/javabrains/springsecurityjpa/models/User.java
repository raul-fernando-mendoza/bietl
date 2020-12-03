package io.javabrains.springsecurityjpa.models;

import java.util.ArrayList;

import javax.persistence.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.javabrains.springsecurityjpa.UserCredentials;

public class User {
    private String userName;
    private String idToken;
    
    UserCredentials userCredentials;
    
    public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	private ArrayList<SimpleGrantedAuthority> grantedAuthorities;
    
    public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

    public ArrayList<SimpleGrantedAuthority> getGrantedAutorities() {
		return grantedAuthorities;
	}

	public void setGrantedAutorities(ArrayList<SimpleGrantedAuthority> grantedAutorities) {
		this.grantedAuthorities = grantedAutorities;
	}

	public User(String userName) {
    	this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }

	

   
    
}
