package com.puxun;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ekapay.util.ConfigUtil;
import com.ekapay.util.DataHelper;
import com.puxun.service.PuxunPayService;
import com.puxun.util.DigestUtil;
import com.util.StringUtils;
import com.heepay.BaseAction;

/**
 * 101支付
 */
public class PuxunPayAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PuxunPayAction.class);

	/**
	 * 返回101支付请求支付Url
	 */
	public void doPay() {
		HttpServletRequest request = ServletActionContext.getRequest();

		logger.info("【101支付】请求支付参数, request:{" + JSONObject.fromObject(request.getParameterMap()).toString() + "}");

		String key = request.getParameter("key");
		if (StringUtils.isEmpty(key)) {
			writeError("商户秘钥不能为空");
			return;
		}

		String p1_MerId = request.getParameter("p1_MerId");// 商户编号 是 Max(11) 8882730
		if (StringUtils.isEmpty(p1_MerId)) {
			writeError("支付地址不能为空");
			return;
		}

		String p2_Order = request.getParameter("p2_Order");// 商户订单号 否 Max(50)
		if (StringUtils.isEmpty(p2_Order)) {
			writeError("订单号不能为空");
			return;
		}

		String p3_Amt = request.getParameter("p3_Amt");// 支付金额 否 Max(20)
														// 单位:元,精确到分
		if (StringUtils.isEmpty(p3_Amt) || Double.valueOf(p3_Amt) <= 0) {
			writeError("交易金额太小");
			return;
		}

		String p8_Url = request.getParameter("p8_Url");// 商户接收支付成功数据的地址 否
														// Max(200)
														// 支付成功后易宝支付会向该地址发送两次成功通知
		if (StringUtils.isEmpty(p8_Url)) {
			writeError("回调地址不能为空");
			return;
		}

		String p0_Cmd = "Buy";// 业务类型 是 Max(20) 固定值“Buy” . 1
		String p4_Cur = "CNY";// 交易币种 是 Max(10) 固定值 ”CNY”. 5
		String p5_Pid = StringUtils.defaultEmpty(request.getParameter("p5_Pid"));// 商品名称
		String p6_Pcat = StringUtils.defaultEmpty(request.getParameter("p6_Pcat"));// 商品种类
		String p7_Pdesc = StringUtils.defaultEmpty(request.getParameter("p7_Pdesc"));// 商品描述
		String p9_SAF = "0";// 送货地址 否 Max(1) 为“1”: 需要用户将送货地址留在易宝支付系统;为“0”:不需要，默认为 ”0”. 10
		String pa_MP = StringUtils.defaultEmpty(request.getParameter("pa_MP"));//  商户扩展信息 否 Max(200)
		
		String pd_FrpId = StringUtils.defaultEmpty(request.getParameter("pd_FrpId"));// 支付通道编码,到易宝支付网关，易宝支付网关默认显示已开通的全部支付通道.
		String pr_NeedResponse = "1"; // 应答机制 否 Max(1) 固定值为“1”: 需要应答机制;收到易宝支付服务器点对点支付成功通知，必须回写以”success”（无关大小写）开头的字符串，即使您收到成功通知时发现该订单已经处理过，也要正确回写”success”，否则易宝支付将认为您的系统没有收到通知，启动重发机制，直到收到”success”为止。

		Map<String, String> parmar = new HashMap<String, String>();
		parmar.put("p0_Cmd", p0_Cmd);
		parmar.put("p1_MerId", p1_MerId);
		parmar.put("p2_Order", p2_Order);
		parmar.put("p3_Amt", p3_Amt);
		parmar.put("p4_Cur", p4_Cur);
		parmar.put("p5_Pid", p5_Pid);
		parmar.put("p6_Pcat", p6_Pcat);
		parmar.put("p7_Pdesc", p7_Pdesc);
		parmar.put("p8_Url", p8_Url);
		parmar.put("p9_SAF", p9_SAF);
		parmar.put("pa_MP", pa_MP);
		parmar.put("pd_FrpId", pd_FrpId);
		parmar.put("pr_NeedResponse", pr_NeedResponse);
		parmar.put("key", key);

		String postData = PuxunPayService.buildPayParmar(parmar);

		String requestPostUrl = ConfigUtil.getPuxunPayUrl() + postData;
		logger.info("【101支付】请求支付URL, request url:{" + requestPostUrl + "}");
		writeSuccess(requestPostUrl);
	}

	/**
	 * 101支付回调接口
	 */
	public void callback() {
		HttpServletRequest request = ServletActionContext.getRequest();

		logger.info("【101支付】通知中转平台充值回调结果, response:{" + JSONObject.fromObject(request.getParameterMap()).toString() + "}");

		String hmac = request.getParameter("hmac");
		if (StringUtils.isEmpty(hmac)) {
			writeBack("error");
			logger.error("【101支付】通知中转平台充值回调结果, hmac(检验码)不能为空");
			return;
		}

		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rp_PayDate = request.getParameter("rp_PayDate");

		writeBack("SUCCESS");// 写成功通知回去

		StringBuffer postData = new StringBuffer();
		postData.append("r0_Cmd=" + r0_Cmd);
		postData.append("&r1_Code=" + r1_Code);
		postData.append("&r2_TrxId=" + r2_TrxId);
		postData.append("&r3_Amt=" + r3_Amt);
		postData.append("&r4_Cur=" + r4_Cur);
		postData.append("&r5_Pid=" + StringUtils.defaultEmpty(r5_Pid));
		postData.append("&r6_Order=" + r6_Order);
		postData.append("&r7_Uid=" + StringUtils.defaultEmpty(r7_Uid));
		postData.append("&r8_MP=" + r8_MP);
		postData.append("&r9_BType=" + r9_BType);
		postData.append("&rp_PayDate=" + rp_PayDate);
		postData.append("&hmac=" + hmac);

		String response = DataHelper.RequestPostUrl(ConfigUtil.getPuxunCallBackUrl(), postData.toString());
		logger.info("【101支付】中转平台通知现金平台充值结果, response:{" + response + "}");
	}

	
	public static void test(){
		
		//response:{{"r3_Amt":["1.000"],"r6_Order":["170113105751116"],"r0_Cmd":["Buy"],"r2_TrxId":["20170113110014527607E"],
		//"ru_Trxtime":[""],"rb_BankId":[""],"rp_PayDate":[""],"r7_Uid":[""],"rq_CardNo":[""],"r9_BType":["1"],"ro_BankOrderId":[""],
		//"r5_Pid":[""],"hmac":["cd18696fde5de48fe4ecf6178c0a0098"],"r8_MP":["http://223.27.35.185:8080/cash/paynotity/puxunNotice.action"],
		//"r1_Code":["1"],"r4_Cur":["RMB"],"p1_MerId":["8882730"],"subid":[""]}} 
		
		String r5_Pid = null;
		String r7_Uid = null;
		
		r5_Pid = "";
		r7_Uid = "";
		
		String sbOld = "";
		sbOld += "8882730";
		sbOld += "Buy";
		sbOld += "1";
		sbOld += "20170113110014527607E";
		sbOld += "1.000";
		sbOld += "RMB";
		sbOld += r5_Pid;
		sbOld += "170113105751116";
		sbOld += r7_Uid;
		sbOld += "http://223.27.35.185:8080/cash/paynotity/puxunNotice.action";
		sbOld += "1";
		sbOld += "";

		//postData.append("&hmac=" + );
		String nhmac = DigestUtil.hmacSign(sbOld, "413f2ee1358544179be9265e3cb1780d"); // 数据签名
		System.out.println(nhmac);
		System.out.println("cd18696fde5de48fe4ecf6178c0a0098".equals(nhmac));
		
		
		
	}
	
	
	public static void main(String[] args) {
		boolean falg = true;
		if (falg) {//测试签名
			test();
			return;
		}
		
		
		String payUrl = "http://api.101ka.com/GateWay/Bank/Default.aspx";

		Map<Integer, String> channels = new HashMap<Integer, String>();
		//channels.put(1, "alipay");// 支付宝企业接口
		channels.put(2, "alipaywap");// 支付宝无线支付
		//channels.put(3, "tenpay");// 财付通企业接口
		channels.put(4, "tenpaywap");// 财付通无线支付
		channels.put(5, "weixin");// 微信扫码接口
		channels.put(6, "wxwap");// 微信H5支付		
		//channels.put(7, "ICBC-WAP-D");// 信用卡工商银行		
		//channels.put(8, "ICBC-WAP");// 信用卡工商银行		

		String channel = channels.get(4);

		Map<String, String> parmar = new HashMap<String, String>();
		parmar.put("p8_Url", "http://192.168.4.65:8085/Pay/puxunCallback.action");
		parmar.put("pa_MP", "http://192.168.3.95:8080/cash/paynotity/puxunNotice.action");
		parmar.put("pd_FrpId", channel);
		parmar.put("p1_MerId", "8882730");// 商户编号 是 Max(11)
		parmar.put("p2_Order", "170110140649002");
		parmar.put("p3_Amt", "1");// 支付金额 否 Max(20)
		parmar.put("key", "413f2ee1358544179be9265e3cb1780d");// 商户密钥
		
		parmar.put("p0_Cmd", "Buy");// 业务类型 是 Max(20) 固定值“Buy” . 1
		parmar.put("p4_Cur", "CNY");
		parmar.put("p5_Pid", "");// 商品名称 否 Max(20) 用于支付时显示在易宝支付网关左侧的订单产品信息.
		parmar.put("p6_Pcat", "");// 商品种类 否 Max(20) 商品种类.
		parmar.put("p7_Pdesc", "");// 商品描述 否 Max(20) 商品描述.
		parmar.put("p9_SAF", "0");
		parmar.put("pr_NeedResponse", "1");// 应答机制 否 Max(1) 固定值为“1”: 需要应答机制必须回写以”success”（无关大小写）开头的字符串;
		
		String postData = PuxunPayService.buildPayParmar(parmar);

		String requestPostUrl = payUrl + postData;
		System.out.println(requestPostUrl);
	}

}
