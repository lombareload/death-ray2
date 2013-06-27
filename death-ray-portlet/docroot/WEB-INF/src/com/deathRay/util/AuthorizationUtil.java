package com.deathRay.util;

//imports para api de autorizacion


public class AuthorizationUtil {

	// singleton
	private AuthorizationUtil(){};
	
	private static class AuthorizationUtilHolder{
		private static final AuthorizationUtil INSTANCE = new AuthorizationUtil();
	}
	
	public static AuthorizationUtil getInstance(){
		return AuthorizationUtilHolder.INSTANCE;
	}
	// fin singleton
}