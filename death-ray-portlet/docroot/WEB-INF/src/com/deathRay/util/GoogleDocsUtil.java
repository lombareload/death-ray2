package com.deathRay.util;

//imports para api de google docs
//import com.google.gdata.client.docs.DocsService;
import com.deathRay.cloud.Tarea;
import com.deathRay.dto.CsvGoogleSpreadsheet;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
//import com.google.gdata.client.spreadsheet.SpreadsheetService;
//import com.google.gdata.data.docs.SpreadsheetEntry;
//import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
//import com.google.gdata.util.ServiceException;

import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.common.io.Files;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import com.google.gdata.data.docs;
//import com.google.gdata.util;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GoogleDocsUtil {

//	private static final SpreadsheetService service = new SpreadsheetService(ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME"));
	private static final Log log = LogFactoryUtil.getLog(GoogleDocsUtil.class);
	private static final String pathToFile = System.getProperty("java.io.tmpdir", "tmp") + System.getProperty("file.separator", "/") + "deathRayTemporalFile.csv";
//	private static final DocsService service = new DocsService(ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME"));
	private Drive service;
	private static final String mimeType = "text/csv";//"application/vnd.google-apps.spreadsheet";
	
	// singleton
	private GoogleDocsUtil(){
		
//		try{
//			java.io.File file = new java.io.File(pathToFile);
//			FileOutputStream fileOutputStream = new FileOutputStream(file);
//			fileOutputStream.write(new byte[]{});
//			fileOutputStream.flush();
//			fileOutputStream.close();
//		} catch (IOException ioe) {
//			log.error("no pudo inicializarce el archivo temporal de la aplicacion");
//		}
	}
	
	private static class GoogleDocsUtilHolder{
		private static final GoogleDocsUtil INSTANCE = new GoogleDocsUtil();
	}
	
	public static GoogleDocsUtil getInstance(){
		return GoogleDocsUtilHolder.INSTANCE;
	}
	// fin singleton
	
	
	public File grantAccess(String code, User user) throws IOException{
		Tarea tarea = new Tarea();
		List<EntityResult> tareas = null;
		try {
			tareas = tarea.findAllTaskByUsuario(user.getUserId());
		} catch (DatastoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CsvGoogleSpreadsheet tabla = new CsvGoogleSpreadsheet();
		List<UserGroup> usersGroup = null;
		try {
			usersGroup = user.getUserGroups();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Long, String> nombres_grupos = new HashMap<Long, String>();
		HashMap<Long, String> estados = Util.getInstance().getEstados();
		nombres_grupos.put(-1L, "");
		for (UserGroup userGroup : usersGroup) {
			nombres_grupos.put(userGroup.getUserGroupId(), userGroup.getName());
		}
		tabla.appendLine("Identificador Proyecto",  "Proyecto", "Nombre", "Descripcion", "Fecha de Inicio", "Fecha de fin", "Estados");
		for (EntityResult entityResult : tareas) {
			// Identificador del proyecto
			String identificador = String.valueOf(entityResult.getEntity().getProperty(4).getValue(0).getIntegerValue());
			String proyecto      = nombres_grupos.get(entityResult.getEntity().getProperty(4).getValue(0).getIntegerValue());
			String nombre 		 = entityResult.getEntity().getKey().getPathElement(0).getName();
			String descripcion   = entityResult.getEntity().getProperty(5).getValue(0).getStringValue();
			String fechaInicio   = Util.getInstance().timestampMicrosecondsToDate(entityResult.getEntity().getProperty(0).getValue(0).getTimestampMicrosecondsValue());
			String fechaFin      = Util.getInstance().timestampMicrosecondsToDate(entityResult.getEntity().getProperty(1).getValue(0).getTimestampMicrosecondsValue());
			String estado        = estados.get(entityResult.getEntity().getProperty(2).getValue(0).getIntegerValue());
			tabla.appendLine(identificador, proyecto, nombre, descripcion, fechaInicio, fechaFin, estado);
		}
		GoogleCredential credential = AuthorizationUtil.getInstance().getGoogleCredential(code);
		service = getService(credential);
//		java.io.File content = new java.io.File(pathToFile);
		
//		CsvGoogleSpreadsheet contenido = new CsvGoogleSpreadsheet();
//		contenido.appendLine("esto", "es", "una", "prueba").appendLine("esto", "es", "una", "prueba");
		
		File file = new File();
		
		file.setTitle("Reporte desde deathRay");
		file.setDescription("este reporte se ha generado desde deathRay");
		file.setMimeType(mimeType);
//		try{
		java.io.File realFile = new java.io.File(pathToFile);
		FileOutputStream fileOutputStream = new FileOutputStream(realFile);
//		log.info(contenido.getResult());
//		fileOutputStream.write(contenido.getResult().getBytes());
		log.info(tabla.getResult());
		fileOutputStream.write(tabla.getResult().getBytes());
		fileOutputStream.flush();
		fileOutputStream.close();
		FileContent fileContent = new FileContent(mimeType, realFile);
		log.info("realFile.exists() = " + realFile.exists());
		file = service.files().insert(file, fileContent).setConvert(true).execute();
		log.info("id del documento guardado en google docs = " + file.getId());
		log.info("file.getSelfLink() = " + file.getSelfLink());
		log.info("file.getWebViewLink() = " + file.getWebViewLink());
		log.info("file.getWebContentLink() = " + file.getWebContentLink());
		log.info("file.getParents() = " + Arrays.toString(file.getParents().toArray()));
		log.info("file.getDownloadUrl() = " + file.getDownloadUrl());
		// el archivo sin modo edicion solo modo view
		log.info("file.getEmbedLink() = " + file.getEmbedLink());
		
		log.info("file.getDefaultOpenWithLink() = " + file.getDefaultOpenWithLink());
		// el archivo en modo edicion para poderlo modificar desde la cuenta de google drive
		log.info("file.getAlternateLink() = " + file.getAlternateLink());
		log.info("file.getMimeType() = " + file.getMimeType());
		log.info("file.values() = " + file.values());
//		} catch (IOException ioe) {
//			log.error("no pudo inicializarce el archivo temporal de la aplicacion", ioe);
//		}
		return file;
	}
	
	public Drive getService(GoogleCredential credential){
		return new Drive.Builder(AuthorizationUtil.TRANSPORT, AuthorizationUtil.JACKSON_FACTORY, credential).build();
	}
	
}