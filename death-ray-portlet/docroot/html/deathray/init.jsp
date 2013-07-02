<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.model.UserGroup"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>

<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="com.liferay.portal.kernel.log.LogFactoryUtil"%>

<%@page import="java.util.*"%>
<%@page import="com.deathRay.util.*"%>
<%@page import="java.util.LinkedList"%>

<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="java.text.SimpleDateFormat"%>

<script type="text/javascript" src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/deathRay.css" />
<%--
 Calendario:
--%>
 <link rel="stylesheet" href="<%= request.getContextPath()%>/css/cssZebra/metallic.css" />
<script src="<%= request.getContextPath()%>/js/zebra_datepicker.js"></script>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<% 
User thisUser = themeDisplay.getUser();
Log log = LogFactoryUtil.getLog("view.jsp");
Util util = Util.getInstance();
SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
%>



