<%@page import="com.deathRay.dao.TareaDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.deathRay.cloud.Tarea"%>
<%@page import="com.liferay.portal.model.Role"%>


<%@include file="init.jsp"%>

<%
	Long groupId = (Long) request.getAttribute("group_id");
	groupId = (groupId == null ? 0 : groupId);
	String keyName = (String) request.getAttribute("keyName");
	String action = (String) request.getAttribute("action");
	keyName = (keyName == null ? "" : keyName);

	List<User> users = UserLocalServiceUtil.getUserGroupUsers(groupId);
	HashMap<Long, String> nombres_usuarios = new HashMap<Long, String>();
	for (User esclavo : users) {
		nombres_usuarios
				.put(esclavo.getUserId(), esclavo.getFullName());
	}

	Tarea tarea = new Tarea();
	TareaDao tareaDao = tarea.findTaskByName(keyName);

	HashMap<Long, String> estados = util.getEstados();
	UserGroup userGroup = UserGroupLocalServiceUtil
			.getUserGroup(groupId);

	List<Role> roles = thisUser.getRoles();

	String rol_Supervisor = ConfigurationProperties.getInstance()
			.getProperty("Rol_Supervisor");
	boolean isSupervisor = false;
	for (Role rol : roles) {
		if (rol.getName()
				.equalsIgnoreCase(rol_Supervisor.toUpperCase())) {
			isSupervisor = true;
			break;
		}
	}
	log.info("isSupervisor: " + isSupervisor);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Crear o Modificar Tareas</title>
</head>
<body>

	<form id="<portlet:namespace />editorTareaForm" name="editorTareaForm"
		action="<portlet:actionURL /> " method="post" class="editorTareaForm">

		<input type="hidden" name="group_id" value="<%=groupId%>" /> <input
			type="hidden" name="action"
			value="<%=action.equals("newTask")?"saveTask":"saveEditedTask"%>" />
		<input type="hidden" name="isSupervisor" value="<%=isSupervisor%>" />

		<aui:layout>
			<aui:column columnWidth="100">
				<aui:fieldset label="Tarea">

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Identificador del proyecto: </span>
						</aui:column>
						<aui:column columnWidth="50">
							<span><%=groupId%></span>
						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Nombre del proyecto:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<span><%=userGroup.getName()%></span>
						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Nombre tarea:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<span><%=keyName%> <input
								type="<%=action.equals("newTask")?"text":"hidden"%>"
								name="keyName" value="<%=keyName%>" /> </span>
						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Descripción de la tarea:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<%
								if (isSupervisor) {
							%>
							<input id="descripcion" name="descripcion" type="text"
								value="<%=tareaDao.getDescripcion()%>"></input>
							<%
								} else {
							%>
							<span> "<%=tareaDao.getDescripcion()%>"
							</span>
							<input type="hidden" id="descripcion" name="descripcion"
								value="<%=tareaDao.getDescripcion()%>" />
							<%
								}
							%>

						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Fecha de Inicio:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<input id="fechaInicio" name="fechaInicio" type="text"  class="dateType1"
								value="<%=tareaDao.getFecha_inicial() %>" />
						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Fecha de Terminación:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<%
								if (!isSupervisor) {
							%>
							<input id="fechaTerminacion" name="fechaTerminacion" type="text" class="dateType2"
								value="<%=tareaDao.getFecha_final()%>"></input>
							<%
								} else {
							%>
							<%=tareaDao.getFecha_final()%>
							<input type="hidden" id="fechaTerminacion"
								name="fechaTerminacion" value="<%=tareaDao.getFecha_final()%>" />
							<%
								}
							%>

						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Persona Asignada:</span>
						</aui:column>
						<aui:column columnWidth="50">

							<%
								if (isSupervisor) {
							%>
							<select id="userId" name="userId">
								<option value="-1">Seleccione...</option>
								<%
									Iterator it = nombres_usuarios.entrySet()
																	.iterator();
															while (it.hasNext()) {
																Map.Entry pairs = (Map.Entry) it.next();
								%>


								<%
									if (pairs
																		.getKey()
																		.toString()
																		.equals(tareaDao.getUserId()
																				.toString())) {
								%>
								<option value="<%=pairs.getKey()%>" selected="selected"><%=pairs.getValue()%></option>
								<%
									} else {
								%>
								<option value="<%=pairs.getKey()%>"><%=pairs.getValue()%></option>
								<%
									}
								%>

								<%
									}
								%>
							</select>
							<%
								} else {
							%>
							<span><%=nombres_usuarios.get(tareaDao
											.getUserId())%></span>
							<input type="hidden" id="userId" name="userId"
								value="<%=tareaDao.getUserId()%>" />
							<%
								}
							%>

						</aui:column>
					</aui:layout>

					<aui:layout>
						<aui:column columnWidth="50">
							<span>Estado:</span>
						</aui:column>
						<aui:column columnWidth="50">
							<select id="estadoId" name="estadoId">
								<option value="-1">Seleccione...</option>
								<%
									Iterator it = estados.entrySet().iterator();
														while (it.hasNext()) {
															Map.Entry pairs = (Map.Entry) it.next();
								%>


								<%
									if (pairs
																	.getKey()
																	.toString()
																	.equals(tareaDao.getEstado()
																			.toString())) {
								%>
								<option value="<%=pairs.getKey()%>" selected="selected"><%=pairs.getValue()%></option>
								<%
									} else {
								%>
								<option value="<%=pairs.getKey()%>"><%=pairs.getValue()%></option>
								<%
									}
								%>

								<%
									}
								%>
							</select>
						</aui:column>
					</aui:layout>

				</aui:fieldset>
			</aui:column>
		</aui:layout>
		<aui:button-row>
			<aui:button name="saveButton" type="submit" value="save" />
			<aui:button name="cancelButton" type="button" value="cancel"
				onClick="cancelFunction()" />
		</aui:button-row>

		<script type="text/javascript">
			function cancelFunction() {
				document.editorTareaForm.action.value = "cancelSaveTask";
				document.editorTareaForm.submit();
			}

			$(document).ready(function() {

				// assuming the controls you want to attach the plugin to 
				// have the "datepicker" class set
				$('.dateType1').Zebra_DatePicker({
					view : 'years',
					format : 'd/m/Y',
					direction : 0,
					pair : $('.dateType2')
				});

				$('.dateType2').Zebra_DatePicker({
					view : 'years',
					format : 'd/m/Y',
					direction : 1
				});

			});
		</script>

	</form>

</body>
</html>