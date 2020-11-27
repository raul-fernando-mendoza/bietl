package io.javabrains.springsecurityjpa;

//snippet-start:[sts.java2.get_session_token.import]
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetSessionTokenResponse;
import software.amazon.awssdk.services.sts.model.StsException;
import software.amazon.awssdk.services.sts.model.GetSessionTokenRequest;
//snippet-end:[sts.java2.get_session_token.import]

public class AWSGetSessionTokenApplication {

 public static void main(String[] args) {
/*
     final String USAGE = "\n" +
             "To run this example, supply the access key id.  \n" +
             "\n" +
             "Ex: GetSessionToken <accessKeyId>\n";

     if (args.length < 1) {
         System.out.println(USAGE);
         System.exit(1);
     }

 
     String accessKeyId = args[0];
*/
	 
	 String accessKeyId = "";
			 
     Region region = Region.US_EAST_1;
     StsClient stsClient = StsClient.builder()
             .region(region)
             .build();

     getToken(stsClient, accessKeyId);
 }

 // snippet-start:[sts.java2.get_session_token.main]
 public static void getToken(StsClient stsClient, String accessKeyId ) {

     try {
         GetSessionTokenRequest tokenRequest = GetSessionTokenRequest.builder()
                 .durationSeconds(1500)
                 .build();

         GetSessionTokenResponse tokenResponse = stsClient.getSessionToken(tokenRequest);
         System.out.println("The token value is "+tokenResponse.credentials().sessionToken());

     } catch (StsException e) {
         System.err.println(e.getMessage());
         System.exit(1);
     }
 }
 // snippet-end:[sts.java2.get_session_token.main]
}
