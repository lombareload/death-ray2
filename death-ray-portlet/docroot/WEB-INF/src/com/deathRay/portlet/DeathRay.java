package com.deathRay.portlet;

import com.deathRay.util.Util;
import com.liferay.util.bridges.mvc.MVCPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import java.io.IOException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * Portlet implementation class DeathRay
 */
public class DeathRay extends MVCPortlet {

	private static final Log log = LogFactoryUtil.getLog(Util.class);
	private String page = "/html/deathray/view.jsp";
	private static final String home = "/html/deathray/view.jsp";

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		log.info("Entra a processAction");
		String action = ParamUtil.getString(request, "action", "");
		Long group_id = 0L;
		String keyName = "";

		if (request.getParameter("action") != null) {

			if (action.equals("edit_tasks")) {
				log.info("edit_tasks");

				try {
					group_id = Long.valueOf(request.getParameter("group_id"));
					page = "/html/deathray/editorTareas.jsp";
				} catch (Exception ex) {
					log.debug("Error in Long.valueOf "
							+ request.getParameter("group_id"));
				}

			} else if (action.equals("edit_task")) {

				try {
					group_id = Long.valueOf(request.getParameter("group_id"));
					keyName = String.valueOf(request.getParameter("keyName"));
					page = "/html/deathray/crearModificarTarea.jsp";
				} catch (Exception ex) {
					log.debug("Error in Long.valueOf "
							+ request.getParameter("group_id"));
				}

			} else if (action.equals("saveTask") || action.equals("cancelSaveTask")) {

				boolean isSupervisor = Boolean.valueOf(request
						.getParameter("isSupervisor"));

				if (isSupervisor) {
					page = "/html/deathray/editorTareas.jsp";
				} else {
					page = home;
				}

				if (action.equals("saveTask")) {

					try {
						group_id = Long.valueOf(request
								.getParameter("group_id"));
						keyName = String.valueOf(request
								.getParameter("keyName"));

					} catch (Exception ex) {
						log.debug("Error in Long.valueOf "
								+ request.getParameter("group_id"));
					}

				}

			}

		}
		request.setAttribute("group_id", group_id);
		request.setAttribute("keyName", keyName);
		response.setPortletMode(PortletMode.VIEW);
	}

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		log.info("Entra a doView");
		log.info("page: " + page);
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
}
