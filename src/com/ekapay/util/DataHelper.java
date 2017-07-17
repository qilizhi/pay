package com.ekapay.util;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.util.QueueManager;

public class DataHelper {
	private static Logger logger = Logger.getLogger(DataHelper.class);

	public final static String UTF8Encode = "UTF-8";
	public final static String GBKEncode = "GBK";

	public static String RequestGetUrl(String getUrl) {
		return GetPostUrl(null, getUrl, "GET");
	}

	public static String RequestPostUrl(String getUrl, String postData) {
		String result = GetPostUrl(postData, getUrl, "POST");
		if (result != null) {
			return result;
		}

		for (int i = 1; i < 4; i++) {
			try {
				if (i == 1) {
					Thread.sleep(30000);
				}

				result = GetPostUrl(postData, getUrl, "POST");
				if (result == null) {
					Thread.sleep(30000);
				} else {
					break;
				}
			} catch (Exception e) {
				logger.error("重发第 " + i + " 次", e);
			}
		}

		if (result == null) {
			String url = getUrl + "?" + postData;
			QueueManager.getInstance().addUrl(url);
		}

		return result;
	}

	/**
	 * 重新请求
	 */
	public static String rewirePostUrl(String getUrl, String postData) {
		return GetPostUrl(postData, getUrl, "POST");
	}

	private static String GetPostUrl(String postData, String postUrl, String submitMethod) {
		String request = postUrl + "?" + postData;
		logger.info("request url:{" + request + "}");

		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try {
			url = new URL(postUrl);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setRequestMethod(submitMethod.toUpperCase());
			httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpurlconnection.setDoInput(true);
			httpurlconnection.setDoOutput(true);
			if (submitMethod.equalsIgnoreCase("POST")) {
				httpurlconnection.getOutputStream().write(postData.getBytes(UTF8Encode));
				httpurlconnection.getOutputStream().flush();
				httpurlconnection.getOutputStream().close();
			}

			int code = httpurlconnection.getResponseCode();
			if (code == 200) {
				DataInputStream in = new DataInputStream(httpurlconnection.getInputStream());
				int len = in.available();
				byte[] by = new byte[len];
				in.readFully(by);
				String rev = new String(by, UTF8Encode);
				in.close();

				logger.info("request result:{" + rev + "}");
				return rev;
			} else {// http 请求返回非 200状态时处理
				logger.error("response code:{" + code + "}");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("request error:", e);
		} finally {
			if (httpurlconnection != null) {
				httpurlconnection.disconnect();
			}
		}
		return null;
	}
}
