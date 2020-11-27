package io.javabrains.springsecurityjpa;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;

//execute as a java app and the open a browser and login to http://localhost:8080/login
//run maven install to create the war file.
//chnage application.properties an use spring.jpa.hibernate.ddl-auto=create-drop to recreate the hibernate tables.

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminListGroupsForUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GroupType;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.AssumeRoleWithWebIdentityRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleWithWebIdentityResponse;
import software.amazon.awssdk.services.sts.model.Credentials;
import software.amazon.awssdk.services.sts.model.StsException;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClientBuilder;
import software.amazon.awssdk.services.cognitoidentity.model.CognitoIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityResponse;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdRequest;





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
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ThingRepository.class} )
public class CognitoLoginUserSTSApplication extends SpringBootServletInitializer{

	static final Logger log = 
	        LoggerFactory.getLogger(CognitoLoginUserSTSApplication.class);
	
	public static boolean isBlank(String ptext) {
		 return ptext == null || ptext.trim().length() == 0;
		}	
	
	public static void main(String[] args) {
        /* Read the name from command args */
        String clientID ="68drn80osdvmd19rugbq9s8tpp";
        String userPoolId ="us-east-2_LxGV9jduH";
        String identityPoolId = "us-east-2:eb3fac9c-fa57-4353-8672-a65e45a2dbc9";
        String username = "manual_user2";
        String password ="ETLbi123!";
        Region region = Region.US_EAST_2;
        
        //AnonymousCredentialsProvider credentialsProvider = AnonymousCredentialsProvider.create();
        
        String accessKeyId = "AKIASG7SDJOJRP2P262R";
        String secretAccessKey = "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";
        
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        
        
        CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(credentialsProvider)
                .build();

        String idToken = initiateAutoRequest(cognitoclient,clientID, userPoolId, username ,password);
        System.out.println("***********  User token: " + idToken);
        
    	
    	
    	
    	//getOpenIdTokenRequest
    	
        

    	CognitoIdentityClient amazonCognitoIdentity =  CognitoIdentityClient.builder().region(Region.US_EAST_2).credentialsProvider(credentialsProvider).build();
    	
    	Map<String, String> logins = new HashMap<>();
    	logins.put("cognito-idp.us-east-2.amazonaws.com/"+userPoolId,idToken);
    	/*
    	GetOpenIdTokenRequest openIdTokenRequest = GetOpenIdTokenRequest.builder()
    			.identityId(identityPoolId)
    			.logins(logins).build();
    			*/
    	
    	//GetOpenIdTokenResponse openIdTokenResponse = amazonCognitoIdentity.getOpenIdToken(openIdTokenRequest);    	
    	
    	GetIdRequest getIdRequest = GetIdRequest.builder()
//    			.accountId("152442194835")
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
         
         
         
         AwsSessionCredentials credentials_cognito = AwsSessionCredentials.create(accessKeyId_cognito, secretAccessKey_cognito,sessionToken_cognito);
         
         
         StaticCredentialsProvider credentialsProvider_cognito = StaticCredentialsProvider.create(credentials_cognito);
         
         
         S3Client s3 = S3Client.builder()
        		 .region(region)
        		 .credentialsProvider(credentialsProvider_cognito)
        		 .build();
         
         
         
         String bucket_name = "etl-bi-bucket"; // /cognito/etl-bi-app/us-east-2:082a59c5-d8d6-4f3e-8168-4e78ebd8f021/
        
         ListBucketsResponse listBucketsResponse = s3.listBuckets();
         
         List<Bucket> buckets = listBucketsResponse.buckets();
         
         System.out.println("******** my buckets ***********");
         for( Bucket b: buckets)
        	 System.out.println("bucket:"+ b.name());
         
         ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucket_name).build();
         
         ListObjectsV2Response  listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);
         
         
         List<S3Object> l = listObjectsV2Response.contents();
         
         System.out.println("******************** Objects **********");
         for( S3Object a : l)
        	 System.out.println("object:" + a.key());
        
         String key = "cognito/etl-bi-app/" + identityId + "/key.txt";

        // tutorialSetup(s3, bucket, region);

         System.out.println("Uploading object:" + key);

         s3.putObject(PutObjectRequest.builder().bucket(bucket_name).key(key)
                         .build(),
                 RequestBody.fromString("Testing with the AWS SDK for Java"));

         System.out.println("Upload complete");
         System.out.printf("%n");
 /*
         createBucket(s3, bucket, key);
 */
         System.out.println("Closing the connection to Amazon S3");
         s3.close();
         System.out.println("Connection closed");
         System.out.println("Exiting...");       
       
         
         
    }

    //snippet-start:[cognito.java2.create_user_pool.main]
    public static String initiateAutoRequest(CognitoIdentityProviderClient cognitoclient,String clientId, String userPoolId, String username, String pass ) {

        try {
        	Map<String, String> authParameters = new HashMap<String, String>();
        	authParameters.put("USERNAME", username);
        	authParameters.put("PASSWORD", pass);
        	
        	
        	AdminInitiateAuthRequest adminInitiateAuthRequest = AdminInitiateAuthRequest.builder().userPoolId(userPoolId).clientId(clientId).authParameters(authParameters).authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).build();
        	AdminInitiateAuthResponse initialResponse = cognitoclient.adminInitiateAuth(adminInitiateAuthRequest);
        	
        	
        	
        	 if (initialResponse.challengeName()!= null && !ChallengeNameType.NEW_PASSWORD_REQUIRED.name().equalsIgnoreCase( initialResponse.challengeName().name() ) ) {
                 throw new RuntimeException("unexpected challenge: " + initialResponse.challengeName());
             }

        	System.out.println("**************** RESULT *************");
        	System.out.println("authenticationResult:"+ initialResponse.authenticationResult().toString());
        	System.out.println("accessToken:"+ initialResponse.authenticationResult().accessToken());
        	System.out.println("accessToken:"+ initialResponse.authenticationResult().idToken());
        	System.out.println("initialResponse:"+ initialResponse.authenticationResult().tokenType());
        	

        	

        	
        	AdminListGroupsForUserRequest adminListGroupsForUserRequest = AdminListGroupsForUserRequest.builder().username(username).userPoolId(userPoolId).build();
        	AdminListGroupsForUserResponse groupresponse = cognitoclient.adminListGroupsForUser(adminListGroupsForUserRequest);
        	
        	
        	List<GroupType> listgroups = groupresponse.groups(); 
        	System.out.println("**************** GROUPS *************");
        	for( GroupType t: listgroups ) {
        		System.out.println("t:" + t.groupName());
        	}
        	System.out.println("**************** END_GROUPS *************");
        		
        	

        	
            return initialResponse.authenticationResult().idToken();          
            
            

        } catch (CognitoIdentityProviderException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
        //snippet-end:[cognito.java2.create_user_pool.main]
    }
    public static void assumeGivenRole(StsClient stsClient, String roleArn, String roleSessionName) {

        try {
         AssumeRoleRequest roleRequest = AssumeRoleRequest.builder()
                 .roleArn(roleArn)
                 .roleSessionName(roleSessionName)
                 .build();

            AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
            Credentials myCreds = roleResponse.credentials();

            // Display the time when the temp creds expire
            Instant exTime = myCreds.expiration();
            String tokenInfo = myCreds.sessionToken();

            // Convert the Instant to readable date
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                            .withLocale( Locale.US)
                            .withZone( ZoneId.systemDefault() );

            formatter.format( exTime );
            System.out.println("The token "+tokenInfo + "  expires on " + exTime );

        } catch (StsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
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
    
    public static void createBucket(S3Client s3Client, String bucketName, Region region) {
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(
                            CreateBucketConfiguration.builder()
                                    .locationConstraint(region.id())
                                    .build())
                    .build());
            System.out.println("Creating bucket: " + bucketName);
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build()).matched().response().isPresent();
            System.out.println(bucketName +" is ready.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }    
}
