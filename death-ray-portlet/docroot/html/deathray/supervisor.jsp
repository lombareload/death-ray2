<%@page import="java.util.HashMap"%>
<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.liferay.portal.model.UserGroup"%>

<%@include file="init.jsp"%>

<%
	List<UserGroup> usersGroup = UserGroupLocalServiceUtil
			.getUserUserGroups(thisUser.getUserId());

	for (UserGroup userGroup : usersGroup) {
		userGroup.getUserGroupId();
		userGroup.getName();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Supervisor</title>
</head>
<body>
	<form id="<portlet:namespace />supervisorForm" name="supervisorForm"
		action="<portlet:actionURL /> " method="post" class="supervisorForm">

		<input type="hidden" name="group_id" value="" /> 
		<input type="hidden" name="action" value="edit_tasks" />

		<liferay-ui:search-container delta="5"
			emptyResultsMessage="Sin proyectos asignados">
			<liferay-ui:search-container-results>
				<%
                    if (usersGroup != null && !usersGroup.isEmpty()) {
                        results = ListUtil.subList(usersGroup, searchContainer.getStart(), searchContainer.getEnd());
                        total = usersGroup.size();
                    } else {
                        results = new LinkedList();
                        total = 0;
                    }

                    pageContext.setAttribute("results", results);
                    pageContext.setAttribute("total", total);
                %>

			</liferay-ui:search-container-results>

			<liferay-ui:search-container-row
				className="com.liferay.portal.model.UserGroup" modelVar="grupo">

				<liferay-ui:search-container-column-text name="Nombre del proyecto"
					property="name" />

				<liferay-ui:search-container-column-text
					name="Identificador del proyecto" property="userGroupId" />

				<liferay-ui:search-container-column-text>
					<a
						href="javascript:goToAdminTareas('<%=String.valueOf(grupo.getUserGroupId())%>')">
						Ver Tareas </a>
				</liferay-ui:search-container-column-text>

			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>



		<script type="text/javascript">
			function goToAdminTareas(value_ID) {
				document.supervisorForm.group_id.value = value_ID;
				document.supervisorForm.submit();
			}
		</script>

	</form>
</body>

</html>

