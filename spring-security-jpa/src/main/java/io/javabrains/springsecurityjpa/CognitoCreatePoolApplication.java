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
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolResponse;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ThingRepository.class} )
public class CognitoCreatePoolApplication extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(CognitoCreatePoolApplication.class);
	
	public static void main(String[] args) {
        /* Read the name from command args */
        String userPoolName = "etl-bi-user-pool";

        CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2)
                .build();

        String id = createPool(cognitoclient,userPoolName);
        System.out.println("User pool ID: " + id);
        cognitoclient.close();
    }

    //snippet-start:[cognito.java2.create_user_pool.main]
    public static String createPool(CognitoIdentityProviderClient cognitoclient,String userPoolName ) {

        try {
            CreateUserPoolResponse repsonse = cognitoclient.createUserPool(
                    CreateUserPoolRequest.builder()
                            .poolName(userPoolName)
                            .build()
            );
            return repsonse.userPool().id();

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
        //snippet-end:[cognito.java2.create_user_pool.main]
    }
}
