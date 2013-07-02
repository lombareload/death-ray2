<%@page import="com.deathRay.util.ConfigurationProperties"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.google.api.services.drive.DriveScopes"%>
<%@page import="com.deathRay.util.TransactionStatus"%>
<%@include  file="init.jsp" %>

<div id="google-authorization">
	<button id="google-authorize-button" class="aui-helper-hidden">Generar resumen de tareas via google drive</button>
</div>

<script type="text/javascript">
function autenticarGoogleDriveApi() {
	var CLIENT_ID = '<%= ConfigurationProperties.getInstance().getProperty("CLIENT_ID") %>';
	var SCOPES = "<%= DriveScopes.DRIVE %>";
	
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
					if(result.status == '<%= TransactionStatus.NO_ERRORS.ordinal() %>'){
						
// 						var driveUrl = document.createElement('a');
// 						driveUrl.href = result.driveUrl;
// 						driveUrl.innerHTML = "ver en drive";
						
						var gadgetUrl = document.createElement("a");
						gadgetUrl.onclick = function(){showModalDialog(result.gadgetUrl);};
						gadgetUrl.style.cursor = "pointer";
						gadgetUrl.innerHTML = "ver el resumen";
						var parent = document.getElementById("google-authorization");
// 						parent.appendChild(driveUrl);
						var auxText = document.createElement("div");
						auxText.innerHTML = "se ha guardado una copia de la informacion en google drive";
						parent.appendChild(auxText);
						parent.appendChild(gadgetUrl);
					} else if(result.status == '<%= TransactionStatus.IO_EXCEPTION.ordinal() %>'){
						alert("no se pudo obtener respuesta desde google, intente mas tarde");
					} else{
						alert("se produjo un error");
					}
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
<jsp:include page="/html/deathray/tareasAsignadas.jsp" />

<script type="text/javascript" src="https://apis.google.com/js/client.js?onload=autenticarGoogleDriveApi"></script>


