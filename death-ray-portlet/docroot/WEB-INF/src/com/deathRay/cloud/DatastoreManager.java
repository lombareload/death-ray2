package com.deathRay.cloud;

import com.deathRay.util.ConfigurationProperties;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreFactory;
import com.google.api.services.datastore.client.DatastoreHelper;
import com.google.api.services.datastore.client.DatastoreOptions;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class DatastoreManager {
	
	private static Datastore datastore;
	
	/**
	 * Devuelve el elemento Datastore que permite gestionar la persistencia en google cloud.
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static Datastore getDatastore() throws GeneralSecurityException, IOException {
		Credential credential = DatastoreHelper.getServiceAccountCredential(ConfigurationProperties.getInstance().getProperty("DATASTORE_SERVICE_ACCOUNT"), ConfigurationProperties.getInstance().getProperty("DATASTORE_PRIVATE_KEY_FILE"));
		DatastoreOptions.Builder options = new DatastoreOptions.Builder();
		options.dataset(System.getenv("DATASTORE_DATASET"));
	    options.host(System.getenv("DATASTORE_HOST"));
	    options.credential(credential);
	    datastore = DatastoreFactory.get().create(options.dataset(ConfigurationProperties.getInstance().getProperty("datasetId")).build());
	    return datastore;
	}

	
	
}
