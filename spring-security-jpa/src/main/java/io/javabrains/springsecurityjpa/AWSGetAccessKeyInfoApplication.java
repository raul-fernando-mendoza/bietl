package io.javabrains.springsecurityjpa;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.StsException;
import software.amazon.awssdk.services.sts.model.GetAccessKeyInfoRequest;
import software.amazon.awssdk.services.sts.model.GetAccessKeyInfoResponse;

public class AWSGetAccessKeyInfoApplication {
	 public static void main(String[] args) {

	        final String USAGE = "\n" +
	                "To run this example, supply the accessKey Id value.  \n" +
	                "\n" +
	                "Ex: GetAccessKeyInfo <accessKeyId>\n";

	        if (args.length < 1) {
	             System.out.println(USAGE);
	             System.exit(1);
	        }

	        /* Read the name from command args*/
	        String accessKeyId = args[0];

	        Region region = Region.US_EAST_1;
	        StsClient stsClient = StsClient.builder()
	                .region(region)
	                .build();

	        getKeyInfo(stsClient, accessKeyId );
	    }

	    // snippet-start:[sts.java2.get_access_key.main]
	    public static void getKeyInfo(StsClient stsClient, String accessKeyId ) {

	        try {
	            GetAccessKeyInfoRequest accessRequest = GetAccessKeyInfoRequest.builder()
	                    .accessKeyId(accessKeyId)
	                    .build();

	            GetAccessKeyInfoResponse accessResponse = stsClient.getAccessKeyInfo(accessRequest);
	            System.out.println("The account associated with the access key is "+accessResponse.account());

	        } catch (StsException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        }
	    }
	    // snippet-end:[sts.java2.get_access_key.main]
}
