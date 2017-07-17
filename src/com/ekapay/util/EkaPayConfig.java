package com.ekapay.util;

import java.util.ResourceBundle;

public class EkaPayConfig {

	private static Object lock = new Object();
	private static EkaPayConfig config = null;
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE = "ParterInfo";

	private EkaPayConfig() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}

	public static EkaPayConfig getInstance() {
		if (null == config) {
			synchronized (lock) {
				if (null == config) {
					config = new EkaPayConfig();
				}
			}
		}
		return config;
	}

	public String getValue(String key) {
		return (rb.getString(key));
	}
	
	public static void main(String[] args) {
		System.out.println(EkaPayConfig.getInstance().getValue("parter"));
	}
}
