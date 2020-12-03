package io.javabrains.springsecurityjpa;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupResponse;

import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

@SpringBootApplication
public class AWSCognitoAddUserToGroupApplication extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(AWSCognitoAddUserToGroupApplication.class);
	
	   public static void main(String[] args) {

	        String groupName = "mygroup";
	        String userPoolId = "us-east-2_LxGV9jduH";
	        String userName = "automated_user";


	        CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
	                .region(Region.US_EAST_2)
	                .build();

        
	        AdminAddUserToGroup(cognitoclient, groupName, userPoolId, userName);
	        cognitoclient.close();
	    }

	    //snippet-start:[cognito.java2.add_login_provider.main]
	    public static void AdminAddUserToGroup(CognitoIdentityProviderClient cognitoclient,
	    								String groupName,
	                                   String userPoolId,
	                                   String username){

	        try{
	        	
	        	
	        	AdminAddUserToGroupResponse response = cognitoclient.adminAddUserToGroup(
	            		AdminAddUserToGroupRequest.builder().groupName(groupName).username(username).userPoolId(userPoolId).build()
	            		);
	            System.out.println("response:" + response);

	        } catch (CognitoIdentityProviderException e){
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	        //snippet-end:[cognito.java2.add_login_provider.main]
	    }
}
