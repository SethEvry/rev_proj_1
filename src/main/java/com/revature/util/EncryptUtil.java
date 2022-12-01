package com.revature.util;

import java.util.Base64;

public class EncryptUtil {
	
	
	
	
	//takes basic auth "Basic Basr64(username:password)" and changes it to array {username, password}
	public static String[] decrypt(String basicAuth) {
		//take "Basic " out
		String encodedCreds = basicAuth
				.substring(6)
				.trim();
		//decode base64 and split username and password.

		return new String(Base64
				.getDecoder()
				.decode(encodedCreds))
				.split(":");
	}

}
