package com.deathRay.util;

//imports para api de autorizacion
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.auth.oauth2.TokenResponse;
//import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.oauth2.Oauth2;
//import com.google.api.services.oauth2.model.Tokeninfo;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

/**
 * Clase para simplificar la interaccion con el api de autorizacion de google implementada como un singleton
 * 
 * @author fabian
 *
 */
public class AuthorizationUtil {

	// datos que identifican a la aplicacion ante google
	private static final String CLIENT_ID = ConfigurationProperties.getInstance().getProperty("CLIENT_ID", "869906549016.apps.googleusercontent.com");
	private static final String CLIENT_SECRET = ConfigurationProperties.getInstance().getProperty("CLIENT_SECRET", "qQj9lWu2EDocTCNYRwr1SsKN");
//	private static final String APPLICATION_NAME = ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME", "DeathRay");
	public static final JacksonFactory JACKSON_FACTORY = new JacksonFactory();
	public static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final Log log = LogFactoryUtil.getLog(AuthorizationUtil.class);;
	
	// singleton
	private AuthorizationUtil(){
		log.debug("inicializando AuthorizationUtil");
	}
	
	
	public static AuthorizationUtil getInstance(){
		return AuthorizationUtilHolder.INSTANCE;
	}
	// fin singleton
	
	/**
	 * retorna un TokenResponse que puede ser almacenado en la sesion del usuario para 
	 * futuras autenticaciones, o una excepcion en caso de que la autenticacion halla 
	 * tenido algun problema.
	 * 
	 * @param code
	 * @return
	 * @throws TokenResponseException
	 * @throws IOException
	 */
	public GoogleTokenResponse connect(String code) throws DeathRayTokenResponseException, IOException{//, TokenResponseException
		log.debug("codigo recibido = " + code);
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				TRANSPORT, JACKSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();
		log.debug("creando credencial");
		GoogleCredential credential = new GoogleCredential.Builder().
				setJsonFactory(JACKSON_FACTORY).
				setTransport(TRANSPORT).
				setClientSecrets(CLIENT_ID, CLIENT_SECRET).
				build().
				setFromTokenResponse(tokenResponse);
		log.debug("creando oauth2");
		
		/*
		// verificacion de que el token esta dando permiso al usuario actual
		if( ! tokeninfo.getUserId().equals(gPlusId)){}
		
		// verificacion de que el token esta dando permiso a esta aplicacion
		if( ! tokeninfo.getIssuedTo().equals(CLIENT_ID)){}
		*/
		log.debug("retornando respuesta");
		return tokenResponse;
	}
	
	public GoogleCredential getGoogleCredential(String code)throws IOException{//, TokenResponseException{
		log.info("codigo recibido = " + code);
//		TokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JACKSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, "").execute();
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				TRANSPORT, JACKSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();
		log.info("tokenResponse = " + tokenResponse);
		GoogleCredential credential = new GoogleCredential.Builder().
				setJsonFactory(JACKSON_FACTORY).
				setTransport(TRANSPORT).
				setClientSecrets(CLIENT_ID, CLIENT_SECRET).
				build().
				setFromTokenResponse(tokenResponse);
		log.info("credential.getRefreshToken() = " + credential.getRefreshToken());
		return credential;
	}

	private static class AuthorizationUtilHolder{
		private static final AuthorizationUtil INSTANCE = new AuthorizationUtil();
	}
}