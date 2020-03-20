package cn.comtom.core.main.sichuan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	/**
	 * instance
	 */
	private static PropertiesReader instance = null;
	/**
	 * 
	 */
	private static Properties properties;

	/**
	 * 
	 * @return
	 */
	public static PropertiesReader getInstance() {
		if (instance == null) {
			instance = new PropertiesReader();
			properties = instance.loadPropertiesConfig();
		}
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	private Properties loadPropertiesConfig() {
		InputStream is = null;
		Properties config = new Properties();
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			is = cl.getResourceAsStream("application.properties");
			config.load(is);
			return config;
		} catch (IOException e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperties(String key) {
		return properties.getProperty(key);
	}
}
