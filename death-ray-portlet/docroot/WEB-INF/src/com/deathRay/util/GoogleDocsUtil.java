package com.deathRay.util;

//imports para api de google docs
//import com.google.gdata.client.docs.DocsService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.docs.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
//import com.google.gdata.data.docs;
//import com.google.gdata.util;
import java.net.URL;

public class GoogleDocsUtil {

	private static final SpreadsheetService service = new SpreadsheetService(ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME"));
	private static final Log log = LogFactoryUtil.getLog(GoogleDocsUtil.class);
//	private static final DocsService service = new DocsService(ConfigurationProperties.getInstance().getProperty("APPLICATION_NAME"));
	
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
		Credential credential = AuthorizationUtil.getInstance().getGoogleCredential(code);
		service.setOAuth2Credentials(credential);
		URL url = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
		try{
			SpreadsheetFeed spreadSheetFeed = service.getFeed(url, SpreadsheetFeed.class);
			log.debug("spreadSheetFeed = " + spreadSheetFeed.getEntries().toString());
			SpreadsheetEntry spreadSheetEntry = service.getEntry(url, SpreadsheetEntry.class);
			log.debug("spreadSheetEntry = " + spreadSheetEntry);
		} catch(ServiceException se){
			log.error("no se pudieron obtener las hojas de calculo asociadas al usuario", se);
		}
	}
	
}