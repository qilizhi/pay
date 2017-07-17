package com.util;

import java.util.ResourceBundle;

public class PropertiesUtil {
	private static Object lock = new Object();
	private static PropertiesUtil config = null;
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE = "config";

	private PropertiesUtil() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}

	public static PropertiesUtil getInstance() {
		if (null == config) {
			synchronized (lock) {
				if (null == config) {
					config = new PropertiesUtil();
				}
			}
		}
		return config;
	}

	public String getValue(String key) {
		return (rb.getString(key));
	}
}
