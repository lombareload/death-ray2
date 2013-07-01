<%@page import="com.deathRay.util.ConfigurationProperties"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@include  file="init.jsp" %>

<div id="google-authorization">
	<button id="google-authorize-button" class="aui-helper-hidden">Permitir acceso a google drive</button>
</div>

<script type="text/javascript">
function autenticarGoogleDriveApi() {
	var CLIENT_ID = '<%= ConfigurationProperties.getInstance().getProperty("CLIENT_ID") %>';
	var SCOPES = "https://www.googleapis.com/auth/drive";
	
	var googleButton = document.getElementById("google-authorize-button");
	if(gapi.auth.getToken()){
		console.debug("el token ya se encontraba en el cliente");
		console.debug(gapi.auth.getToken());
	}else {
		googleButton.className = "";
		function autorizar(){
			gapi.auth.authorize(
				{'response_type':"code", 'client_id': CLIENT_ID, 'scope': SCOPES, 'inmediate': true},
				manejarResultadoDeAutorizacion
			);    
		}
		googleButton.onclick = autorizar;
	}
}

function manejarResultadoDeAutorizacion(resultadoAutorizacion){
	if(resultadoAutorizacion){
		console.log("resultadoAutorizacion");
		console.log(resultadoAutorizacion);
		if(resultadoAutorizacion.error){
			console.error("fallo al autenticacion del usuario ante google");
			return;
			// fin del flujo de autorizacion
		} else if(resultadoAutorizacion.access_token){
			console.debug("la autenticacion retorno con token de acceso");
			console.debug(resultadoAutorizacion);
			console.debug("gapi.auth.getToken()");
			console.debug(gapi.auth.getToken());
			enviarCodigoAlServidor(resultadoAutorizacion.access_token);
		} else if(resultadoAutorizacion.code){
			console.debug("la autenticacion retorno con con codigo");
			console.debug(resultadoAutorizacion.code);
			console.debug("gapi.auth.getToken()");
			console.debug(gapi.auth.getToken());
			enviarCodigoAlServidor(resultadoAutorizacion.code);
		}
	} else{
		console.warn("el cliente no dio permiso a la aplicacion");
		// fin del flujo de autorizacion
	}
}

function enviarCodigoAlServidor(code){
	console.debug(code);
	AUI().use("aui-io-request", function(A){
		A.io.request('<portlet:resourceURL></portlet:resourceURL>', {
			method: 'post',
			dataType: 'json',
			data:{
				'code': code
			},
			on:{
				success: function(){
					var  result = this.get('responseData');
					console.debug('respuesta del servior');
					console.debug(result);
				},
				failure: function(){
					alert("no hay respuesta del servidor");
				}
			}
		});
	});
}
</script>




<%
	
	List<Role> roles = thisUser.getRoles();
	
	String rol_Supervisor = ConfigurationProperties.getInstance()
			.getProperty("Rol_Supervisor");
	boolean isSupervisor = false;
	for (Role rol : roles) {
		if (rol.getName().equalsIgnoreCase(rol_Supervisor.toUpperCase())) {
			isSupervisor = true;
			break;
		}
	}	
	log.info("isSupervisor: "+isSupervisor);
%>
<%
if(isSupervisor){
%>
<jsp:include page="/html/deathray/supervisor.jsp" />
<%
}
%>
<script type="text/javascript" src="https://apis.google.com/js/client.js?onload=autenticarGoogleDriveApi"></script>


