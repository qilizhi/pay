package com.ekapay.util;

import java.util.Map;


public class EkaPayEncrypt {

	/**
	 * TODO 1.商户发给网关接口的参数签名
	 * 注意：sign加密时参数的要按照顺序，否则加密后无法通过网关验证,范例：parter={}&type={}&value={}&orderid ={}&callbackurl={}key
	 */
	public static String paymentMd5Sign(Map<String, String> parmar) {
		
		StringBuffer sendsign = new StringBuffer();
		sendsign.append("parter=" + parmar.get("parter"));
		sendsign.append("&type=" + parmar.get("type"));
		sendsign.append("&value=" + parmar.get("value"));
		sendsign.append("&orderid=" + parmar.get("orderid"));
		sendsign.append("&callbackurl=" + parmar.get("callbackurl"));

		return MD5.MD5Encode(sendsign + parmar.get("key"));
	}
	
	/**
	 * TODO 2.网关接口异步返回(callbackurl)给商户的参数签名
	 * 注意：sign加密时参数的要按照顺序，否则加密后无法通过网关验证,范例：签名源串及格式如:orderid={orderid}&opstate={opstate}&ovalue={ovalue}key
	 * sign为进行MD5加密并转为小写的字符串，key为商户在网关系统的密钥
	 */
	public static String callBackMd5Sign(Map<String, String> parmar) {
		StringBuffer sendsign = new StringBuffer();
		sendsign.append("orderid=" + parmar.get("orderid"));
		sendsign.append("&opstate=" + parmar.get("opstate"));
		sendsign.append("&ovalue=" + parmar.get("ovalue"));
		
		return MD5.MD5Encode(sendsign + parmar.get("key"));
	}
	
	
	
	
	
	
	
	
	
	/******************************************************************************************/
	/**********************************  TODO 下面的未用到(保留)    ********************************/
	/******************************************************************************************/
	
	
	
	public static String EkaPayCardMd5Sign(String type, String parter, String cardno, String cardpwd, String value, String restrict,
			String orderid, String callbackurl, String key) {
		StringBuffer sendsb = new StringBuffer();
		sendsb.append("type=" + type);
		sendsb.append("&parter=" + parter);
		sendsb.append("&cardno=" + cardno);
		sendsb.append("&cardpwd=" + cardpwd);
		sendsb.append("&value=" + value);
		sendsb.append("&restrict=" + restrict);
		sendsb.append("&orderid=" + orderid);
		sendsb.append("&callbackurl=" + callbackurl);
		String md5 = MD5.MD5Encode(sendsb + key);

		return md5;
	}

	public static String EkaPayCardMultiMd5Sign(String type, String parter, String cardno, String cardpwd, String value, String totalvalue,
			String restrict, String orderid, String attach, String callbackurl, String key) {
		StringBuffer sendsb = new StringBuffer();
		sendsb.append("type=" + type);
		sendsb.append("&parter=" + parter);
		sendsb.append("&cardno=" + cardno);
		sendsb.append("&cardpwd=" + cardpwd);
		sendsb.append("&value=" + value);
		sendsb.append("&totalvalue=" + totalvalue);
		sendsb.append("&restrict=" + restrict);
		sendsb.append("&attach=" + attach);
		sendsb.append("&orderid=" + orderid);
		sendsb.append("&callbackurl=" + callbackurl);
		String md5 = MD5.MD5Encode(sendsb + key);

		return md5;
	}

	public static String EkaPayBankMd5Sign(String type, String parter, String value, String orderid, String callbackurl, String md5key) {
		StringBuffer sendsb = new StringBuffer();
		sendsb.append("parter=" + parter);
		sendsb.append("&type=" + type);
		sendsb.append("&value=" + value);
		sendsb.append("&orderid=" + orderid);
		sendsb.append("&callbackurl=" + callbackurl);
		return MD5.MD5Encode(sendsb + md5key);
	}

	public static String EkaPayCardBackMd5Sign(String orderid, String opstate, String ovalue, String key) {

		StringBuffer sendsb = new StringBuffer();
		sendsb.append("orderid=" + orderid);
		sendsb.append("&opstate=" + opstate);
		sendsb.append("&ovalue=" + ovalue);
		return MD5.MD5Encode(sendsb + key);
	}

	public static String EkaPayCardMultiBackMd5Sign(String orderid, String cardno, String opstate, String ovalue, String totalvalue,
			String attach, String msg, String key) {
		StringBuffer sendsb = new StringBuffer();
		sendsb.append("orderid=" + orderid);
		sendsb.append("&cardno=" + cardno);
		sendsb.append("&opstate=" + opstate);
		sendsb.append("&ovalue=" + ovalue);
		sendsb.append("&ototalvalue=" + totalvalue);
		sendsb.append("&attach=" + attach);
		sendsb.append("&msg=" + msg);
		return MD5.MD5Encode(sendsb + key);
	}
}
