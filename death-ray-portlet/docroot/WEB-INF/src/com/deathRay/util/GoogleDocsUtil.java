package com.deathRay.util;

//imports para api de google docs
//import com.google.gdata.client.docs.DocsService;
import com.deathRay.dto.CsvGoogleSpreadsheet;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
//import com.google.gdata.client.spreadsheet.SpreadsheetService;
//import com.google.gdata.data.docs.SpreadsheetEntry;
//import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
//import com.google.gdata.util.ServiceException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.common.io.Files;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import com.google.gdata.data.docs;
//import com.google.gdata.util;
import java.net.URL;
import java.util.Arrays;
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
	
	
	public File grantAccess(String code) throws IOException{
		GoogleCredential credential = AuthorizationUtil.getInstance().getGoogleCredential(code);
		service = getService(credential);
//		java.io.File content = new java.io.File(pathToFile);
		
		CsvGoogleSpreadsheet contenido = new CsvGoogleSpreadsheet();
		contenido.appendLine("esto", "es", "una", "prueba").appendLine("esto", "es", "una", "prueba");
		
		File file = new File();
		
		file.setTitle("Reporte desde deathRay");
		file.setDescription("este reporte se ha generado desde deathRay");
		file.setMimeType(mimeType);
//		try{
			java.io.File realFile = new java.io.File(pathToFile);
			FileOutputStream fileOutputStream = new FileOutputStream(realFile);
			log.info(contenido.getResult());
			fileOutputStream.write(contenido.getResult().getBytes());
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