package com.puxun.service;

import java.util.Map;

import com.puxun.util.DigestUtil;

/**
 * 101支付业务类
 */
public class PuxunPayService {

	/**
	 * 创建商户发给网关接口的参数(支付)
	 */
	public static String buildPayParmar(Map<String, String> parmar) {
		String hmac = paymentMd5Sign(parmar);

		StringBuffer sendsb = new StringBuffer();
		sendsb.append("?p0_Cmd=" + parmar.get("p0_Cmd"));
		sendsb.append("&p1_MerId=" + parmar.get("p1_MerId"));
		sendsb.append("&p2_Order=" + parmar.get("p2_Order"));
		sendsb.append("&p3_Amt=" + parmar.get("p3_Amt"));
		sendsb.append("&p4_Cur=" + parmar.get("p4_Cur"));
		sendsb.append("&p5_Pid=" + parmar.get("p5_Pid"));
		sendsb.append("&p6_Pcat=" + parmar.get("p6_Pcat"));
		sendsb.append("&p7_Pdesc=" + parmar.get("p7_Pdesc"));
		sendsb.append("&p8_Url=" + parmar.get("p8_Url"));
		sendsb.append("&p9_SAF=" + parmar.get("p9_SAF"));
		sendsb.append("&pa_MP=" + parmar.get("pa_MP"));
		sendsb.append("&pd_FrpId=" + parmar.get("pd_FrpId"));
		sendsb.append("&pr_NeedResponse=" + parmar.get("pr_NeedResponse"));
		sendsb.append("&hmac=" + hmac);
		return sendsb.toString();
	}

	/**
	 * 商户发给网关接口的数据签名
	 */
	public static String paymentMd5Sign(Map<String, String> parmar) {

		StringBuffer sendsign = new StringBuffer();
		sendsign.append(parmar.get("p0_Cmd"));
		sendsign.append(parmar.get("p1_MerId"));
		sendsign.append(parmar.get("p2_Order"));
		sendsign.append(parmar.get("p3_Amt"));
		sendsign.append(parmar.get("p4_Cur"));
		sendsign.append(parmar.get("p5_Pid"));
		sendsign.append(parmar.get("p6_Pcat"));
		sendsign.append(parmar.get("p7_Pdesc"));
		sendsign.append(parmar.get("p8_Url"));
		sendsign.append(parmar.get("p9_SAF"));
		sendsign.append(parmar.get("pa_MP"));
		sendsign.append(parmar.get("pd_FrpId"));
		sendsign.append(parmar.get("pr_NeedResponse"));

		return DigestUtil.hmacSign(sendsign.toString(), parmar.get("key"));
	}
}
