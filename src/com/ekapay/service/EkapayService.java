package com.ekapay.service;

import java.util.Map;

import com.ekapay.util.EkaPayEncrypt;

/**
 * 科讯支付业务类
 */
public class EkapayService {

	/**
	 * 创建商户发给网关接口的参数(支付)
	 * 商户发给网关接口的url范例:
	 * website?parter={parter}&type={type}&value={value}&orderid={orderid}&callbackurl={callbackurl}&hrefbackurl={hrefbackurl}&payerIp={payerIp}&attach={attach}&sign={sign} 
	 */
	public static String buildPayParmar(Map<String, String> parmar) {

		String key = EkaPayEncrypt.paymentMd5Sign(parmar);

		StringBuffer sendsb = new StringBuffer();
		sendsb.append("?parter=" + parmar.get("parter"));
		sendsb.append("&type=" + parmar.get("type"));
		sendsb.append("&value=" + parmar.get("value"));
		sendsb.append("&orderid=" + parmar.get("orderid"));
		sendsb.append("&callbackurl=" + parmar.get("callbackurl"));
		sendsb.append("&hrefbackurl=" + parmar.get("hrefbackurl"));
		sendsb.append("&payerIp=" + parmar.get("payerIp"));
		sendsb.append("&attach=" + parmar.get("attach"));
		sendsb.append("&sign=" + key);
		return sendsb.toString();
	}
}
