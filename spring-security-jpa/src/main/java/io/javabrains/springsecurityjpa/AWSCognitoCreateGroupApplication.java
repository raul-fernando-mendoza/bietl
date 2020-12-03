package io.javabrains.springsecurityjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateGroupRequest.Builder;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateGroupResponse;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

@SpringBootApplication
public class AWSCognitoCreateGroupApplication extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(AWSCognitoCreateGroupApplication.class);
	
	   public static void main(String[] args) {

	        String groupName = "mygroup";
	        String userPoolId = "us-east-2_LxGV9jduH";
	        String description = "mydescription";
	        String roleArn = "arn:aws:cognito-idp:us-east-2:152442194835:userpool/us-east-2_LxGV9jduH";
	        int precedence = 1;

	        CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
	                .region(Region.US_EAST_2)
	                .build();

	        createGroup(cognitoclient, groupName, userPoolId, description,roleArn,precedence);
	        cognitoclient.close();
	    }

	    //snippet-start:[cognito.java2.add_login_provider.main]
	    public static void createGroup(CognitoIdentityProviderClient cognitoclient,
	                                   String groupName,
	    							   String userPoolId,
	                                   String description,
	                                   String roleArn,
	                                   int precedence){

	        try{
		        Builder b =  CreateGroupRequest.builder(); 
		        b.groupName(groupName);
		        b.userPoolId(userPoolId);
		        b.roleArn(roleArn);
		        b.description(description);
		        b.precedence(precedence);
	            CreateGroupResponse response = cognitoclient.createGroup(b.build());
	            System.out.println("response " + response.group());

	        } catch (CognitoIdentityProviderException e){
	            System.err.println(e.awsErrorDetails().errorMessage());
	            System.exit(1);
	        }
	        //snippet-end:[cognito.java2.add_login_provider.main]
	    }
}
