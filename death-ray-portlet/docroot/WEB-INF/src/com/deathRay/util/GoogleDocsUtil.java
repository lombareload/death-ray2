package com.deathRay.util;

//imports para api de google docs
import com.google.gdata.client.docs.DocsService;
//import com.google.gdata.data.docs;
//import com.google.gdata.util;

public class GoogleDocsUtil {

	// singleton
	private GoogleDocsUtil(){}
	
	private static class GoogleDocsUtilHolder{
		private static final GoogleDocsUtil INSTANCE = new GoogleDocsUtil();
	}
	
	public static GoogleDocsUtil getInstance(){
		return GoogleDocsUtilHolder.INSTANCE;
	}
	// fin singleton
	
	
	
	
}