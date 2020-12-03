package io.javabrains.springsecurityjpa;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.javabrains.springsecurityjpa.models.User;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.PasswordResetRequiredException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
    /* Read the name from command args */
    String clientID ="68drn80osdvmd19rugbq9s8tpp";
    String userPoolId ="us-east-2_LxGV9jduH";
    String identityPoolId = "us-east-2:eb3fac9c-fa57-4353-8672-a65e45a2dbc9";
    Region region = Region.US_EAST_2;
    

	@Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
 
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("validating username:" + username + " password:" + password);
                
        User user = null;
        try {
	        user = AWSCognitoLogin.login(region, clientID, userPoolId, identityPoolId,  username ,password);
		}catch (NotAuthorizedException e) {
        	System.out.println("usuario con password incorrecto:"); 
        }catch (PasswordResetRequiredException e) {
        	System.out.println("usuario requiere reset:" + e.getMessage()); 
        }
        catch (CognitoIdentityProviderException e){
	        System.err.println("Error en cognito:" + e.getMessage());
	    }

        log.info("user:" + user);
        
        if ( user != null) {
        	Object principal = user;
        	Object credentials = user.getUserCredentials();
        	ArrayList<SimpleGrantedAuthority> authorities = user.getGrantedAutorities();
        	
            return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        	
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
    	log.info("supports p:" + authentication );
    	log.info("supports a:" + UsernamePasswordAuthenticationToken.class + " " + authentication.equals(UsernamePasswordAuthenticationToken.class));
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
