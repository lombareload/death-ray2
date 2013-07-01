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
import com.deathRay.cloud.Tarea;
import com.deathRay.dao.TareaDao;
import com.deathRay.util.Util;
//import com.google.gdata.util.ParseException;
import com.liferay.util.bridges.mvc.MVCPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;

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

	private String page = "/html/deathray/view.jsp";
	private static final String home = "/html/deathray/view.jsp";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		log.info("Entra a processAction");
		String action = ParamUtil.getString(request, "action", "");
		log.info("action:"+action);
		String keyName = "";

		if (request.getParameter("action") != null) {
			Long group_id = Long.valueOf(request.getParameter("group_id"));
			log.info("group_id:"+group_id);

			if (action.equals("edit_tasks")) {
				log.info("edit_tasks");

				try {
					page = "/html/deathray/editorTareas.jsp";
				} catch (Exception ex) {
					log.debug("Error in Long.valueOf "
							+ request.getParameter("group_id"));
				}

			} else if (action.equals("edit_task")) {

				try {
					keyName = String.valueOf(request.getParameter("keyName"));
					page = "/html/deathray/crearModificarTarea.jsp";
				} catch (Exception ex) {
					log.debug("Error in Long.valueOf "
							+ request.getParameter("group_id"));
				}

			} else if (action.equals("saveTask")
					|| action.equals("cancelSaveTask")
					|| action.equals("saveEditedTask")) {

				boolean isSupervisor = Boolean.valueOf(request
						.getParameter("isSupervisor"));
				keyName = String.valueOf(request.getParameter("keyName"));

				if (isSupervisor) {
					page = "/html/deathray/editorTareas.jsp";
				} else {
					page = home;
				}

				if (action.equals("saveTask")
						|| action.equals("saveEditedTask")) {

					try {
						Tarea tarea = new Tarea();

						TareaDao taskFound = tarea.findTaskByName(keyName);
						if (taskFound.getName().isEmpty()
								|| action.equals("saveEditedTask")) {
							String descripcion = String.valueOf(request
									.getParameter("descripcion"));
							String fechaInicio = String.valueOf(request
									.getParameter("fechaInicio"));
							Date fecha_inicial = fechaInicio.isEmpty() ? null
									: dateFormat.parse(fechaInicio);
							String fechaTerminacion = String.valueOf(request
									.getParameter("fechaTerminacion"));
							Date fecha_final = fechaTerminacion.isEmpty() ? null
									: dateFormat.parse(fechaTerminacion);
							String user_Id = request.getParameter("userId");
							Long userId = Long.valueOf(user_Id);
							String estadoId = request.getParameter("estadoId");
							Long estado = Long.valueOf(estadoId);
							tarea.createOrUpdateTarea(fecha_inicial,
									fecha_final, estado, userId, group_id,
									keyName, descripcion);
						} else {
							request.setAttribute("error",
									"Ya existe una tarea con este nombre");
							page = "/html/deathray/editorTareas.jsp";
						}

					} catch (Exception ex) {
						log.info("Error in Long.valueOf "
								+ request.getParameter("group_id"));
					}

				}

			} else if (action.equals("newTask")) {
				page = "/html/deathray/crearModificarTarea.jsp";
			}
			request.setAttribute("group_id", group_id);
		}
		
		
		request.setAttribute("keyName", keyName);
		request.setAttribute("action", action);
		response.setPortletMode(PortletMode.VIEW);
	}

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		log.info("Entra a doView");
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = getPortletContext()
				.getRequestDispatcher(page);
		page = home;
		dispatcher.include(request, response);
	}

	@Override
	public void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		log.info("Entra a doEdit");
		response.setContentType("text/html");
		// Implementar:
		PortletRequestDispatcher dispatcher = getPortletContext()
				.getRequestDispatcher(
						"/WEB-INF/pages/admin/damage/DamageAdminPortlet_edit.jsp");
		dispatcher.include(request, response);
	}
	
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
