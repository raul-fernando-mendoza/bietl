package io.javabrains.springsecurityjpa;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRespondToAuthChallengeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRespondToAuthChallengeResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolResponse;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ThingRepository.class} )
public class CognitoUpdatePasswordUserApplication extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(CognitoUpdatePasswordUserApplication.class);
	
	public static boolean isBlank(String ptext) {
		 return ptext == null || ptext.trim().length() == 0;
		}	
	
	public static void main(String[] args) {
        /* Read the name from command args */
        String userPoolName = "etl-bi-user-pool";
        String clientID ="68drn80osdvmd19rugbq9s8tpp";
        String userPoolId ="us-east-2_LxGV9jduH";
        String username = "manual_user2";
        String oldpassword ="Etlbi123!";
        String newpassword = "ETLbi123!";
        

        CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2)
                .build();

        String id = initiateAutoRequest(cognitoclient,clientID, userPoolId, username, oldpassword ,newpassword);
        System.out.println("User pool ID: " + id);
        cognitoclient.close();
    }

    //snippet-start:[cognito.java2.create_user_pool.main]
    public static String initiateAutoRequest(CognitoIdentityProviderClient cognitoclient,String clientId, String userPoolId, String username, String pass, String newpass ) {

        try {
        	Map<String, String> authParameters = new HashMap<String, String>();
        	authParameters.put("USERNAME", username);
        	authParameters.put("PASSWORD", pass);
        	
        	
        	AdminInitiateAuthRequest adminInitiateAuthRequest = AdminInitiateAuthRequest.builder().userPoolId(userPoolId).clientId(clientId).authParameters(authParameters).authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).build();
        	AdminInitiateAuthResponse initialResponse = cognitoclient.adminInitiateAuth(adminInitiateAuthRequest);
        	
        	 if (!ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equalsIgnoreCase( initialResponse.challengeName().name() ) ) {
                 throw new RuntimeException("unexpected challenge: " + initialResponse.challengeName());
             }

             Map<String, String> challengeResponses = new HashMap<String, String>();
             challengeResponses.put("USERNAME", username);
             challengeResponses.put("PASSWORD", username);
             challengeResponses.put("NEW_PASSWORD", newpass);

             AdminRespondToAuthChallengeRequest finalRequest = AdminRespondToAuthChallengeRequest.builder()
            		 .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
            		 .challengeResponses(challengeResponses)
            		 .clientId(clientId)
            		 .userPoolId(userPoolId)
            		 .session(initialResponse.session())
            		 .build();


             AdminRespondToAuthChallengeResponse challengeResponse = cognitoclient.adminRespondToAuthChallenge(finalRequest);
             if (challengeResponse.challengeName() == null) {
                 System.out.println( challengeResponse.authenticationResult());
             } else {
                 throw new RuntimeException("unexpected challenge: " + challengeResponse.challengeName());
             }        	
        	System.out.println("*******");
        	System.out.println(challengeResponse.authenticationResult().toString());
            return challengeResponse.authenticationResult().toString();

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
        //snippet-end:[cognito.java2.create_user_pool.main]
    }
}
