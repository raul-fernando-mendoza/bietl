package io.javabrains.springsecurityjpa;

public class UserCredentials {
	private String accessKeyId;
	private String secretKey;
    private String sessionToken;
    
    public String getAccessKeyId() {
		return accessKeyId;
	}


	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}


	public String getSecretKey() {
		return secretKey;
	}


	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	public String getSessionToken() {
		return sessionToken;
	}


	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}     
}
