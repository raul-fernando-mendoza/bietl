package io.javabrains.springsecurityjpa;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.authority.SimpleGrantedAuthority; 

import io.javabrains.springsecurityjpa.models.User;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityResponse;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GroupType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.PasswordResetRequiredException;





/**
 * To make this code example work, create a Role that you want to assume.
 * Then define a Trust Relationship in the AWS Console. You can use this as an example:
 *
 * {
 *   "Version": "2012-10-17",
 *   "Statement": [
 *     {
 *       "Effect": "Allow",
 *       "Principal": {
 *         "AWS": "<Specify the ARN of your IAM user you are using in this code example>"
 *       },
 *       "Action": "sts:AssumeRole"
 *     }
 *   ]
 * }
 *
 *  For more information, see "Editing the Trust Relationship for an Existing Role" in the AWS Directory Service guide.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = { ThingRepository.class} )
public class AWSCognitoLogin extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(AWSCognitoLogin.class);
	
	public static boolean isBlank(String ptext) {
		 return ptext == null || ptext.trim().length() == 0;
		}	
	
	public static void main(String[] args) {
        /* Read the name from command args */
        String clientID ="68drn80osdvmd19rugbq9s8tpp";
        String userPoolId ="us-east-2_LxGV9jduH";
        String identityPoolId = "us-east-2:eb3fac9c-fa57-4353-8672-a65e45a2dbc9";
/*
        String username = "manual_user2";
        String password ="ETLbi123!";
*/

/*   need update */ 
        String username ="manualuser3";
        String password ="Honda2033!";
       

        Region region = Region.US_EAST_2;
        
        
        

        User user = null;
        try {
	        user = login(region, clientID, userPoolId, identityPoolId,  username ,password);
		}catch (NotAuthorizedException e) {
        	System.out.println("usuario con password incorrecto:"); 
        }catch (PasswordResetRequiredException e) {
        	System.out.println("usuario requiere reset:" + e.getMessage()); 
        }
        catch (CognitoIdentityProviderException e){
	        System.err.println("Error en cognito:" + e.getMessage());
	    }
        
        System.out.println("***********  User: " + user);
        
         
    }

    //snippet-start:[cognito.java2.create_user_pool.main]
    public static User login( Region region, String clientId, String userPoolId, String identityPoolId, String username, String pass ) throws CognitoIdentityProviderException  {

    	User user = null;
        	
		CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
		        .region(region)
		  //      .credentialsProvider(credentialsProvider)
		        .build();        	
		
		Map<String, String> authParameters = new HashMap<String, String>();
		authParameters.put("USERNAME", username);
		authParameters.put("PASSWORD", pass);
		
		
		AdminInitiateAuthRequest adminInitiateAuthRequest = AdminInitiateAuthRequest.builder().userPoolId(userPoolId).clientId(clientId).authParameters(authParameters).authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).build();
		AdminInitiateAuthResponse initialResponse = cognitoclient.adminInitiateAuth(adminInitiateAuthRequest);
		
		
		 if (initialResponse.challengeName()!= null && ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equalsIgnoreCase( initialResponse.challengeName().name() ) ) {
			 System.out.println("respondio con un challenge de nuevo password");
			 throw PasswordResetRequiredException.builder().message(ChallengeNameType.NEW_PASSWORD_REQUIRED.name()).build();
			 
		 }	 
		
		 if (initialResponse.challengeName()!= null && !ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equalsIgnoreCase( initialResponse.challengeName().name() ) ) {
		     throw new RuntimeException("unexpected challenge: " + initialResponse.challengeName());
		 }
		
		
		 
		System.out.println("**************** RESULT *************");
		System.out.println("authenticationResult:"+ initialResponse.authenticationResult().toString());
		System.out.println("accessToken:"+ initialResponse.authenticationResult().accessToken());
		System.out.println("idToken:"+ initialResponse.authenticationResult().idToken());
		System.out.println("initialResponse:"+ initialResponse.authenticationResult().tokenType());
		
		
		AdminListGroupsForUserRequest adminListGroupsForUserRequest = AdminListGroupsForUserRequest.builder().username(username).userPoolId(userPoolId).build();
		AdminListGroupsForUserResponse groupresponse = cognitoclient.adminListGroupsForUser(adminListGroupsForUserRequest);
		
		
		List<GroupType> listgroups = groupresponse.groups(); 
		System.out.println("**************** GROUPS *************");
		
		ArrayList<SimpleGrantedAuthority> userGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		for( GroupType t: listgroups ) {
			userGrantedAuthorities.add(new SimpleGrantedAuthority(t.groupName()));
			System.out.println("t:" + t.groupName());
		}
		System.out.println("**************** END_GROUPS *************");
			
		String idToken =  initialResponse.authenticationResult().idToken();
		
		CognitoIdentityClient amazonCognitoIdentity =  CognitoIdentityClient.builder().region(region).build();
		
		Map<String, String> logins = new HashMap<>();
		logins.put("cognito-idp.us-east-2.amazonaws.com/"+userPoolId,idToken);
		/*
		GetOpenIdTokenRequest openIdTokenRequest = GetOpenIdTokenRequest.builder()
				.identityId(identityPoolId)
				.logins(logins).build();
				*/
		
		//GetOpenIdTokenResponse openIdTokenResponse = amazonCognitoIdentity.getOpenIdToken(openIdTokenRequest);    	
		
		GetIdRequest getIdRequest = GetIdRequest.builder()
		//        			.accountId("152442194835")
				.logins(logins)
				.identityPoolId(identityPoolId)
				.build();
				
		GetIdResponse getIdResult = amazonCognitoIdentity.getId(getIdRequest);
		String identityId = getIdResult.identityId();
		
		GetCredentialsForIdentityRequest cognitoIdentityRequest = GetCredentialsForIdentityRequest.builder()
				.identityId(identityId)
				.logins(logins)
				.build();
		
		
		GetCredentialsForIdentityResponse credentialsForIdentity = amazonCognitoIdentity
		        .getCredentialsForIdentity(cognitoIdentityRequest);    	
		
		software.amazon.awssdk.services.cognitoidentity.model.Credentials credentialsUser = credentialsForIdentity.credentials();
		
		System.out.println("************* KEYS ********");
		System.out.println("accessKeyId:" + credentialsUser.accessKeyId());
		System.out.println("secretKey:"+ credentialsUser.secretKey());
		System.out.println("sessionToken:"+ credentialsUser.sessionToken());
		
		Instant now = Instant.now();
		boolean isExpirre = credentialsUser.expiration().isAfter(now) ;
		long diff = credentialsUser.expiration().toEpochMilli() - now.toEpochMilli();
		
		System.out.println("Is expire:"+ diff + " " + isExpirre);
		
		
		 cognitoclient.close();
		 
		
		 
		 String accessKeyId_cognito = credentialsUser.accessKeyId();
		 String secretAccessKey_cognito = credentialsUser.secretKey();
		 String sessionToken_cognito = credentialsUser.sessionToken();
		 
		 UserCredentials userCredentials = new UserCredentials();
		 
		 user = new User(username);
		 
		 user.setIdToken(idToken);

		 
		 userCredentials.setAccessKeyId(accessKeyId_cognito);
		 userCredentials.setSecretKey(secretAccessKey_cognito);
		 userCredentials.setSessionToken(sessionToken_cognito);
		 
		 user.setUserCredentials(userCredentials);

		 user.setGrantedAutorities(userGrantedAuthorities);
		 
		 return user;
    }

    
    public static String getClientID(CognitoIdentityClient cognitoclient, String identityPoolId){
        try {

            GetIdRequest request = GetIdRequest.builder()
                    .identityPoolId(identityPoolId)
                    .build();

            GetIdResponse response = cognitoclient.getId(request);
            System.out.println("Identity ID " + response.identityId());
            return response.identityId();

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        //snippet-end:[cognito.java2.GetID.main]
        return "";
    }    
    
  
}
