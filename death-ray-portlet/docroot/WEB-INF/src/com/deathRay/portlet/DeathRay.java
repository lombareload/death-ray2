package com.deathRay.portlet;

import com.deathRay.util.AuthorizationUtil;
import com.deathRay.util.DeathRayTokenResponseException;
import com.deathRay.util.GoogleDocsUtil;
import com.deathRay.util.TransactionStatus;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * Portlet implementation class DeathRay
 */
public class DeathRay extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(DeathRay.class);

	/**
	 * maneja el token de autenticacion retornado por google para dar
	 * permiso en el google drive del usuario.
	 */
	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		JSONObject jResponse = JSONFactoryUtil.createJSONObject();
		String code = ParamUtil.getString(resourceRequest, "code");
		
		try{
			log.debug("llamando metodo en  AuthorizationUtil");
//			AuthorizationUtil.getInstance().getGoogleCredential(code);
			GoogleDocsUtil.getInstance().grantAccess(code);
			log.debug("fin llamado metodo AuthorizationUtil");
			jResponse.put("status", TransactionStatus.NO_ERRORS.ordinal());
		} catch(TokenResponseException tre){
			log.warn("se produjo un error con la autorizacion de google: " + TransactionStatus.TOKEN_RESPONSE_EXCEPTION.toString(), tre);
			jResponse.put("status", TransactionStatus.TOKEN_RESPONSE_EXCEPTION.ordinal());
		} catch(IOException ioe){
			log.warn("se produjo un error con la autorizacion de google: " + TransactionStatus.IO_EXCEPTION.toString(), ioe);
			jResponse.put("status", TransactionStatus.IO_EXCEPTION.ordinal());
		}
//		catch (DeathRayTokenResponseException dtre) {
//			log.warn("se produjo un error con la autorizacion de google: " + TransactionStatus.DEATH_RAY_TOKEN_RESPONSE_EXCEPTION.toString(), dtre);
//			jResponse.put("status", TransactionStatus.DEATH_RAY_TOKEN_RESPONSE_EXCEPTION.ordinal());
//		}
		
		log.debug(jResponse.toString());
		PrintWriter writer = resourceResponse.getWriter();
		writer.print(jResponse);
		writer.flush();
		writer.close();
		//super.serveResource(resourceRequest, resourceResponse);
	}

}