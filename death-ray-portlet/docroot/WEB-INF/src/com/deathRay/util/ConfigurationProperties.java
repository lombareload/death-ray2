package com.deathRay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class ConfigurationProperties extends Properties {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ConfigurationProperties() {
        InputStream is = getClass().getResourceAsStream("conf.properties");
        try {
            this.load(is);
        } catch (IOException ex) {
            Logger.getLogger(getClass()).error("ConfigurationPropertiesInit", ex);
        }
    }

    public static ConfigurationProperties getInstance() {
        return ConfigurationPropertiesHolder.INSTANCE;
    }

    private static class ConfigurationPropertiesHolder {

        private static final ConfigurationProperties INSTANCE = new ConfigurationProperties();
    }
    
    
}
