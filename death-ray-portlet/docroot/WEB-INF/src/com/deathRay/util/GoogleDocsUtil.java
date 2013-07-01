package com.deathRay.util;

//imports para api de google docs
//import com.google.gdata.client.docs.DocsService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.gdata.client.spreadsheet.SpreadsheetService;
//import com.google.gdata.data.docs.SpreadsheetEntry;
//import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
//import com.google.gdata.util.ServiceException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.DriveScopes;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
//import com.google.gdata.data.docs;
//import com.google.gdata.util;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GoogleDocsUtil {

//	private static final SpreadsheetService service = new SpreadsheetService(ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME"));
	private static final Log log = LogFactoryUtil.getLog(GoogleDocsUtil.class);
	private Drive drive;
	
	// singleton
	private GoogleDocsUtil(){}
	
	private static class GoogleDocsUtilHolder{
		private static final GoogleDocsUtil INSTANCE = new GoogleDocsUtil();
	}
	
	public static GoogleDocsUtil getInstance(){
		return GoogleDocsUtilHolder.INSTANCE;
	}
	// fin singleton
	
	
	public void grantAccess(String code) throws IOException{
		GoogleCredential credential = AuthorizationUtil.getInstance().getGoogleCredential(code);
		drive = getDriveService(credential);
		File file = new File();
		file.setMimeType("application/vnd.google-apps.spreadsheet");
//		
		List<File> archivos = drive.files().list().execute().getItems();
		log.info(Arrays.toString(archivos.toArray()));
//		service.setOAuth2Credentials(credential);
//		URL url = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
//		try{
//			SpreadsheetFeed spreadSheetFeed = service.getFeed(url, SpreadsheetFeed.class);
//			log.debug("spreadSheetFeed = " + spreadSheetFeed.getEntries().toString());
//			SpreadsheetEntry spreadSheetEntry = service.getEntry(url, SpreadsheetEntry.class);
//			log.debug("spreadSheetEntry = " + spreadSheetEntry);
//		} catch(ServiceException se){
//			log.error("no se pudieron obtener las hojas de calculo asociadas al usuario", se);
//		}
	}
	
	private Drive getDriveService(GoogleCredential credential){
		return new Drive.Builder(AuthorizationUtil.TRANSPORT, AuthorizationUtil.JACKSON_FACTORY, credential).build();
	}
}