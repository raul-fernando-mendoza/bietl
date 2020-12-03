package io.javabrains.springsecurityjpa;

//snippet-start:[lambda.java2.create.import]
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionCode;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import software.amazon.awssdk.services.lambda.model.CreateFunctionResponse;
import software.amazon.awssdk.services.lambda.model.Runtime;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
//snippet-end:[lambda.java2.create.import]

/**
*  This code example requires a ZIP or JAR that represents the code of the Lambda function.
*  If you do not have a ZIP or JAR, please refer to the following document:
*
*  https://github.com/aws-doc-sdk-examples/tree/master/javav2/usecases/creating_workflows_stepfunctions
*/
public class AWSLambdaCreateApplication {

 public static void main(String[] args) {

     final String USAGE = "\n" +
             "Usage:\n" +
             "    CreateFunction <functionName><filePath><role><handler> \n\n" +
             "Where:\n" +
             "    functionName - the name of the Lambda function \n"+
             "    filePath - the path to the ZIP or JAR where the code is located \n"+
             "    role - the role ARN that has Lambda permissions \n"+
             "    handler - the fully qualifed method name (for example, example.Handler::handleRequest)  \n";

       /*if (args.length < 4) {
           System.out.println(USAGE);
           System.exit(1);
       }*/

     /* Read the name from command args*/
     String functionName = "MyHelloWorldAutomatic";
     String filePath = "src/main/resources/myLambdaFunction.zip";
     String role = "arn:aws:iam::152442194835:role/service-role/myHelloWorld-role-j4w77vy9";
     String handler = "myLambdaFunction.lambda_handler"; //the name of the file and function to be executed

     Region region = Region.US_EAST_2;
     LambdaClient awsLambda = LambdaClient.builder()
             .region(region)
             .build();

     /* Read the name from command args*/
     createLambdaFunction(awsLambda, functionName, filePath, role, handler);
 }

 // snippet-start:[lambda.java2.create.main]
 public static void createLambdaFunction(LambdaClient awsLambda,
                                         String functionName,
                                         String filePath,
                                         String role,
                                         String handler) {

     try {
         // Create a SdkBytes object that represents the Lambda code
         InputStream is = new FileInputStream(filePath); 
         SdkBytes fileToUpload = SdkBytes.fromInputStream(is);

         // Create a FunctionCode object
         FunctionCode code = FunctionCode.builder()
             .zipFile(fileToUpload)
             .build();

         CreateFunctionRequest functionRequest = CreateFunctionRequest.builder()
             .functionName(functionName)
             .description("Created by the Lambda Java API")
             .code(code)
             .handler(handler)
             .runtime(Runtime.JAVA8)
             .role(role)
             .runtime("python3.8")
             .build();

         CreateFunctionResponse functionResponse = awsLambda.createFunction(functionRequest);
         System.out.println("The function ARN is "+functionResponse.functionArn());

     } catch(LambdaException | FileNotFoundException e) {
         System.err.println(e.getMessage());
         System.exit(1);
     }
 }
 // snippet-end:[lambda.java2.create.main]
}