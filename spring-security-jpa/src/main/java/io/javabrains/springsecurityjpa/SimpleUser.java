package io.javabrains.springsecurityjpa;

import java.util.List;

public class SimpleUser {
	String userName=null;
	List<String> roles= null;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
