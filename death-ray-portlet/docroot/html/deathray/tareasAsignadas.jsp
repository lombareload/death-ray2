<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>
<%@page
	import="com.google.api.services.datastore.DatastoreV1.EntityResult"%>
<%@page import="com.google.api.services.datastore.DatastoreV1.Entity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.deathRay.cloud.Tarea"%>

<%@include file="init.jsp"%>

<%
	//Long groupId = (Long) request.getAttribute("group_id");
	String error = (String)request.getAttribute("error");

	/*groupId = (groupId == null ? 0 : groupId);

	List<User> users = UserLocalServiceUtil.getUserGroupUsers(groupId);
	HashMap<Long, String> nombres_usuarios = new HashMap<Long, String>();
	nombres_usuarios.put(-1L, "");
	for (User esclavo : users) {
		nombres_usuarios
				.put(esclavo.getUserId(), esclavo.getFullName());
	}
	*/
	
	List<UserGroup> usersGroup = thisUser.getUserGroups();
	HashMap<Long, String> nombres_grupos = new HashMap<Long, String>();
	nombres_grupos.put(-1L, "");
	for (UserGroup userGroup : usersGroup) {
		nombres_grupos
				.put(userGroup.getUserGroupId(), userGroup.getName());
	}

	Tarea tarea = new Tarea();
	
	Long userId = thisUser.getUserId();
	userId = (userId == null ? 0 : userId);
	List<EntityResult> tareas = tarea.findAllTaskByUsuario(userId);	

	HashMap<Long, String> estados = util.getEstados();
	
	
	//UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(groupId);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editor de tareas</title>
</head>
<body>

	<%
            if (error!=null&&!error.isEmpty()) {
        %>
	<div class="portlet-msg-error" id="errorMessage">  
	<%=error%>          
            </div>
	<%} %>

	<form id="<portlet:namespace />editorTareasForm"
		name="editorTareasForm" action="<portlet:actionURL /> " method="post"
		class="editorTareasForm">

		<input type="hidden" id="group_id" name="group_id" value="" /> 
		<input type="hidden" name="keyName" value="" /> <input type="hidden"
			name="action" value="edit_tasks" />

		<aui:layout>
			<aui:fieldset label="Tareas por Persona">
				<aui:layout>
					<aui:column columnWidth="25">
            Identificador del persona: 
				</aui:column>
					<aui:column columnWidth="25">
						<%=thisUser.getUserId()%>
					</aui:column>
					<aui:column columnWidth="25">
            Nombre del proyecto:
				</aui:column>
					<aui:column columnWidth="25">
						<%=thisUser.getFullName()%>
					</aui:column>
				</aui:layout>
			</aui:fieldset>
		</aui:layout>



		<liferay-ui:search-container delta="5"
			emptyResultsMessage="Sin tareas definidas">
			<liferay-ui:search-container-results>
				<%
					if (tareas != null && !tareas.isEmpty()) {
								results = ListUtil.subList(tareas,
										searchContainer.getStart(),
										searchContainer.getEnd());
								total = tareas.size();
							} else {
								results = new LinkedList();
								total = 0;
							}

							pageContext.setAttribute("results", results);
							pageContext.setAttribute("total", total);
				%>

			</liferay-ui:search-container-results>

			<liferay-ui:search-container-row
				className="com.google.api.services.datastore.DatastoreV1.EntityResult"
				modelVar="entityResult">

				<liferay-ui:search-container-column-text name="Identificador Proyecto">
					<span class=""> <%=entityResult.getEntity().getProperty(4).getValue(0).getIntegerValue()%>
					</span>
				</liferay-ui:search-container-column-text>
				
				<liferay-ui:search-container-column-text name="Proyecto">
					<span class=""> <%=nombres_grupos.get(entityResult.getEntity().getProperty(4).getValue(0).getIntegerValue())%>
					</span>
				</liferay-ui:search-container-column-text>
				
				<liferay-ui:search-container-column-text name="Nombre">
					<span class=""> <%=entityResult.getEntity().getKey()
								.getPathElement(0).getName()%>
					</span>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text name="Descripción">
					<span class=""> <%=entityResult.getEntity().getProperty(5)
								.getValue(0).getStringValue()%>
					</span>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text name="Fecha de Inicio">
					<span class=""> <%=util.timestampMicrosecondsToDate(entityResult
								.getEntity().getProperty(0).getValue(0)
								.getTimestampMicrosecondsValue())%>
					</span>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text name="Fecha de Fin">
					<span class=""> <%=util.timestampMicrosecondsToDate(entityResult
								.getEntity().getProperty(1).getValue(0)
								.getTimestampMicrosecondsValue())%>
					</span>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text name="Estados">
					<span class=""> <%=estados.get(entityResult.getEntity()
								.getProperty(2).getValue(0).getIntegerValue())%>
					</span>
				</liferay-ui:search-container-column-text>

				<liferay-ui:search-container-column-text>
					<a
						href="javascript:goToAdminTarea('<%=entityResult.getEntity().getKey()
								.getPathElement(0).getName()%>', '<%=entityResult.getEntity().getProperty(4).getValue(0).getIntegerValue()%>')">
						Editar Tarea </a>
				</liferay-ui:search-container-column-text>

			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>

		<aui:button-row>
		</aui:button-row>

		<script type="text/javascript">
			function goToAdminTarea(keyName, goupId) {
				document.editorTareasForm.keyName.value = keyName;
				document.editorTareasForm.action.value = "edit_task";
				document.editorTareasForm.group_id.value = goupId;
				document.editorTareasForm.submit();
			}
		</script>

	</form>
</body>
</html>