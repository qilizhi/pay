package com.ddb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alipay.util.UtilDate;
import com.heepay.BaseAction;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.tencent.common.Util;
import com.tencent.common.XMLParser;
import com.util.HttpUtil;
import com.util.PropertiesUtil;
import com.util.StringUtils;
import com.zf.http.HttpClientUtil;

/**
 * 
 * @author Administrator qilizhi
 *
 */

public class DDBPayAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DDBPayAction.class);
	
	
	/**
	 * 多得宝微信扫码
	 * @throws Exception
	 */
	public void ddbPay() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		/************************************** 交易下单 ***********************************************/
		logger.info("----------------多得宝微信扫码-- begin----------------");
		// 支付请求地址
		String reqUrl = PropertiesUtil.getInstance().getValue("ddb_reqUrl");
		// 获取请求参数
		request.setCharacterEncoding("UTF-8");// 传值编码
		logger.info("【多得宝支付】业务平台请求中转平台的参数, params:{"
				+ JSONObject.fromObject(request.getParameterMap()).toString()
				+ "}");
		System.out.println("【多得宝支付】业务平台请求中转平台的参数, params:{"
				+ JSONObject.fromObject(request.getParameterMap()).toString()
				+ "}");
		// response.setContentType("text/html;charset=UTF-8");//设置传输编码
		Map<String, String> remap = new HashMap<String, String>();
		// 参数校验
		String key = request.getParameter("merchant_private_key");
		if (StringUtils.isEmpty(key)) {
			remap.put("resp_code", "FAIL");
			remap.put("resp_desc", "商户秘钥不能为空");
			writeBack(JSONObject.fromObject(remap).toString());
			return;
		}

		String p1_MerId = request.getParameter("merchant_code");// 商户编号 是
																// Max(11)
		// 8882730
		if (StringUtils.isEmpty(p1_MerId)) {
			remap.put("resp_code", "FAIL");
			remap.put("resp_desc", "支付地址不能为空");
			writeBack(JSONObject.fromObject(remap).toString());
			return;
		}

		String p2_Order = request.getParameter("order_no");// 商户订单号 否 Max(50)
		if (StringUtils.isEmpty(p2_Order)) {
			remap.put("resp_code", "FAIL");
			remap.put("resp_desc", "订单号不能为空");
			writeBack(JSONObject.fromObject(remap).toString());
			return;
		}

		String p3_Amt = request.getParameter("order_amount");// 支付金额 否 Max(20)
		// 单位:元,精确到分
		if (StringUtils.isEmpty(p3_Amt) || Double.valueOf(p3_Amt) <= 0) {
			remap.put("resp_code", "FAIL");
			remap.put("resp_desc", "交易金额太小");
			writeBack(JSONObject.fromObject(remap).toString());
			return;
		}

		String p8_Url = request.getParameter("notify_url");// 商户接收支付成功数据的地址 否
															// Max(200)
															// 支付成功后易宝支付会向该地址发送两次成功通知
		if (StringUtils.isEmpty(p8_Url)) {
			remap.put("resp_code", "FAIL");
			remap.put("resp_desc", "回调地址不能为空");
			writeBack(JSONObject.fromObject(remap).toString());
			return;
		}
		String merchant_code = (String) request.getParameter("merchant_code");
		String service_type = (String) request.getParameter("service_type");
		String notify_url = (String) request.getParameter("notify_url");
		String interface_version = "V3.1";// 接口版本，固定值：V3.1(必须大写)
		String client_ip = (String) request.getParameter("client_ip");
		String sign_type = "RSA-S";// "RSA或RSA-S，不参与签名";
		String order_no = (String) request.getParameter("order_no");
		String order_time = UtilDate.getDateFormatter();
		String order_amount = (String) request.getParameter("order_amount");
		String product_name = (String) request.getParameter("product_name");
		String merchant_private_key = (String) request
				.getParameter("merchant_private_key");
		String paramStr = "[多得宝扫码支付中转接口：] ZFPayAction params["
				+ "merchant_code="
				+ merchant_code
				+ "&service_type="
				+ service_type
				+ "&notify_url="
				+ notify_url
				+ "&sign_type="
				+ sign_type
				+ "&client_ip="
				+ client_ip
				+ "&interface_version="
				+ interface_version
				+ "&order_no="
				+ order_no
				+ "&order_time="
				+ order_time
				+ "&order_amount="
				+ order_amount
				+ "&product_name="
				+ product_name
				+ "&order_no=" + order_no +"]";
		System.out.println(paramStr);
		logger.info(paramStr);
		System.out.println("=============参数处理================");

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("merchant_code", merchant_code);
		reqMap.put("service_type", service_type);
		reqMap.put("notify_url", notify_url);
		reqMap.put("interface_version", interface_version);
		reqMap.put("client_ip", client_ip);
		reqMap.put("sign_type", sign_type);
		reqMap.put("order_no", order_no);
		reqMap.put("order_time", order_time);
		reqMap.put("order_amount", order_amount);
		reqMap.put("product_name", product_name);

		/**
		 * 数据签名 签名规则定义如下：
		 * （1）参数列表中，除去sign_type、sign两个参数外，其它所有非空的参数都要参与签名，值为空的参数不用参与签名；
		 * （2）签名参数排序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，组成规则如下：
		 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n
		 */

		StringBuffer signSrc = new StringBuffer();
		signSrc.append("client_ip=").append(client_ip).append("&");
		signSrc.append("interface_version=").append(interface_version)
				.append("&");
		signSrc.append("merchant_code=").append(merchant_code).append("&");
		signSrc.append("notify_url=").append(notify_url).append("&");
		signSrc.append("order_amount=").append(order_amount).append("&");
		signSrc.append("order_no=").append(order_no).append("&");
		signSrc.append("order_time=").append(order_time).append("&");
		signSrc.append("product_name=").append(product_name).append("&");
		signSrc.append("service_type=").append(service_type);

		String signInfo = signSrc.toString();
		String sign = "";

		/**
		 * 1)merchant_private_key，商户私钥，商户按照《密钥对获取工具说明》操作并获取商户私钥；获取商户私钥的同时，
		 * 也要获取商户公钥（merchant_public_key）；调试运行
		 * 代码之前首先先将商户公钥上传到多得宝商家后台"支付管理"->"公钥管理"
		 * （如何获取和上传请查看《密钥对获取工具说明》），不上传商户公钥会导致调试运行代码时报错。
		 * 2)demo提供的merchant_private_key是测试商户号1111110166的商户私钥，请自行获取商户私钥并且替换
		 */
		sign = RSAWithSoftware.signByPrivateKey(signInfo, merchant_private_key); // 签名
																					// signInfo签名参数排序，
																					// merchant_private_key商户私钥
		reqMap.put("sign", sign);
		String posturl="【多得宝】url:"+reqUrl+"【多得宝】param:"+JSONObject.fromObject(reqMap).toString();
		System.out.println(posturl);
		logger.info(posturl);
		String result = new HttpClientUtil().doPost(reqUrl, reqMap, "utf-8"); // 向多得宝发送POST请求

		System.out.println("签名参数排序：" + signInfo.length() + " --> " + signInfo);
		System.out.println("sign值：" + sign.length() + " --> " + sign);
		System.out.println("result值：" + result);
		System.out.println("==============返回xml结果============");
		System.out.println(result);
		logger.info(paramStr);
		Map<String, Object> map = XMLParser.getMapFromXMLBySecondRoot(result);
		String jsonr = JSONObject.fromObject(map).toString();
		System.out.println("==============转换xml成JSON结果============");
		System.out.println(jsonr);
		logger.info(jsonr);
		writeBack(jsonr);
		logger.info("----------------多得宝微信扫码--end----------------");
	}

	public void DDBNotifyCallback() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		System.out.println("多得宝 回调页面");
		System.out
				.println("//**************************   多得宝 回调页面 run start  **************************//");
		request.setCharacterEncoding("UTF-8");// 传值编码
		response.setContentType("text/html;charset=UTF-8");// 设置传输编码
		String reqp="【多得宝支付】多得宝-->>通知-->>中转平台,充值回调结果, params:{"
				+ JSONObject.fromObject(request.getParameterMap()).toString()
				+ "}";
		logger.info(reqp);System.out.println(reqp);
		// 接收多得宝返回的参数
		String interface_version = (String) request
				.getParameter("interface_version");
		String merchant_code = (String) request.getParameter("merchant_code");
		String notify_type = (String) request.getParameter("notify_type");
		String notify_id = (String) request.getParameter("notify_id");
		String sign_type = (String) request.getParameter("sign_type");
		String dinpaySign = (String) request.getParameter("sign");
		String order_no = (String) request.getParameter("order_no");
		String order_time = (String) request.getParameter("order_time");
		String order_amount = (String) request.getParameter("order_amount");
		String extra_return_param = (String) request
				.getParameter("extra_return_param");
		String trade_no = (String) request.getParameter("trade_no");
		String trade_time = (String) request.getParameter("trade_time");
		String trade_status = (String) request.getParameter("trade_status");
		String bank_seq_no = (String) request.getParameter("bank_seq_no");
		// 封装参数

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("interface_version",
				interface_version));
		formparams.add(new BasicNameValuePair("merchant_code", merchant_code));
		formparams.add(new BasicNameValuePair("notify_type", notify_type));
		formparams.add(new BasicNameValuePair("notify_id", notify_id));
		formparams.add(new BasicNameValuePair("sign_type", sign_type));
		formparams.add(new BasicNameValuePair("dinpaySign", dinpaySign));
		formparams.add(new BasicNameValuePair("order_no", order_no));
		formparams.add(new BasicNameValuePair("order_time", order_time));
		formparams.add(new BasicNameValuePair("order_amount", order_amount));
		formparams.add(new BasicNameValuePair("extra_return_param",
				extra_return_param));
		formparams.add(new BasicNameValuePair("trade_no", trade_no));
		formparams.add(new BasicNameValuePair("trade_time", trade_time));
		formparams.add(new BasicNameValuePair("trade_status", trade_status));
		formparams.add(new BasicNameValuePair("bank_seq_no", bank_seq_no));
		String rep = "interface_version = " + interface_version + "\n"
				+ "merchant_code = " + merchant_code + "\n" + "notify_type = "
				+ notify_type + "\n" + "notify_id = " + notify_id + "\n"
				+ "sign_type = " + sign_type + "\n" + "dinpaySign = "
				+ dinpaySign + "\n" + "order_no = " + order_no + "\n"
				+ "order_time = " + order_time + "\n" + "order_amount = "
				+ order_amount + "\n" + "extra_return_param = "
				+ extra_return_param + "\n" + "trade_no = " + trade_no + "\n"
				+ "trade_time = " + trade_time + "\n" + "trade_status = "
				+ trade_status + "\n" + "bank_seq_no = " + bank_seq_no + "\n";
		System.out.println("【多得宝支付】多得宝回调的参数:" + rep);
		logger.info("【多得宝支付】多得宝回调的参数:" + rep);
		/**
		 * 数据签名 签名规则定义如下：
		 * （1）参数列表中，除去sign_type、sign两个参数外，其它所有非空的参数都要参与签名，值为空的参数不用参与签名；
		 * （2）签名参数排序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，组成规则如下：
		 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n
		 */

		StringBuilder signStr = new StringBuilder();
		if (null != bank_seq_no && !bank_seq_no.equals("")) {
			signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
		}
		if (null != extra_return_param && !extra_return_param.equals("")) {
			signStr.append("extra_return_param=").append(extra_return_param)
					.append("&");
		}
		signStr.append("interface_version=").append(interface_version)
				.append("&");
		signStr.append("merchant_code=").append(merchant_code).append("&");
		signStr.append("notify_id=").append(notify_id).append("&");
		signStr.append("notify_type=").append(notify_type).append("&");
		signStr.append("order_amount=").append(order_amount).append("&");
		signStr.append("order_no=").append(order_no).append("&");
		signStr.append("order_time=").append(order_time).append("&");
		signStr.append("trade_no=").append(trade_no).append("&");
		signStr.append("trade_status=").append(trade_status).append("&");
		signStr.append("trade_time=").append(trade_time);
		String signInfo = signStr.toString();
		String t1 = "【多得宝支付】多得宝返回的签名参数排序：" + signInfo.length() + " -->" + signInfo;
		String t2 = "【多得宝支付】多得宝返回的签名：" + dinpaySign.length() + " -->" + dinpaySign;
		logger.info(t1 + "|||||" + t2);
		boolean result = false;
		if ("RSA-S".equals(sign_type)) { // sign_type = "RSA-S"

			/**
			 * 1)dinpay_public_key，多得宝公钥，每个商家对应一个固定的多得宝公钥（
			 * 不是使用工具生成的商户公钥merchant_public_key，不要混淆），
			 * 即为多得宝商家后台"支付管理"->"公钥管理"->"多得宝公钥"里的绿色字符串内容
			 * 2)demo提供的dinpay_public_key是测试商户号1111110166的多得宝公钥
			 * ，请自行复制对应商户号的多得宝公钥进行调整和替换
			 */

			String dinpay_public_key = PropertiesUtil.getInstance().getValue(
					"ddb_dinpay_public_key");
			result = RSAWithSoftware.validateSignByPublicKey(signInfo,
					dinpay_public_key, dinpaySign); // 验签 signInfo多得宝返回的签名参数排序，
													// dinpay_public_key多得宝公钥，
													// dinpaySign多得宝返回的签名
		}
		if ("RSA".equals(sign_type)) { // 数字证书加密方式 sign_type = "RSA"

			// 请在商家后台"支付管理"->"证书下载"处申请和下载pfx数字证书，一般要1~3个工作日才能获取到，1111110166.pfx是测试商户号1111110166的数字证书
			String webRootPath = request.getSession().getServletContext()
					.getRealPath("/");
			String merPfxPath = webRootPath + "pfx/1111110166.pfx"; // 商家的pfx证书文件路径
			String merPfxPass = "87654321"; // 商家的pfx证书密码,初始密码是商户号
			RSAWithHardware mh = new RSAWithHardware();
			mh.initSigner(merPfxPath, merPfxPass);
			result = mh.validateSignByPubKey(merchant_code, signInfo,
					dinpaySign); // 验签 merchant_code为商户号， signInfo多得宝返回的签名参数排序，
									// dinpaySign多得宝返回的签名
		}
		if (result) {
			String su = "【多得宝支付】验签结果result的值：" + result + " -->SUCCESS";
			logger.info(su);
			// pw.write("SUCCESS"); // 验签成功，响应SUCCESS
			// 业务处理
			if ("SUCCESS".equalsIgnoreCase(trade_status)) {
				// 支付成功
				// 获取参数，全部返回
				String platformCallBackUrl = PropertiesUtil.getInstance()
						.getValue("ddb_pay_to_platform_url");
				if (StringUtils.isEmpty(platformCallBackUrl)) {
					logger.error("get ddb_pay_to_platform_url value is null");
				} else {
					// 更新订单状态
					String erTop = "【多得宝支付】中转-->>通知-->>业务平台,发送回调结果";
					String resback = HttpUtil.post(formparams,
							platformCallBackUrl);
					String er=erTop + "Url:" + platformCallBackUrl
							+ "?" + signInfo
							+ "result:" + result;
					System.out.println(er);
					logger.info(er);
					if ("SUCCESS".equalsIgnoreCase(resback)) {
						// 业务平台更改状态成功，通知微信不再回调该结果
						String rebstr = "SUCCESS";
						String t = "【多得宝支付】中转-->>通知-->>多得宝平台，不再回调该结果,发送确认参数给多得宝：";
						logger.info(t + rebstr);
						System.out.println(t + rebstr);
						writeBack(rebstr);
					} else {
						String e = "【多得宝支付】中转-->>通知-->>多得宝平台，回调不成功！原因：" + resback;
						logger.info(e);
						System.out.println(e);
						String rebstr = "FAIL";
						String t = "【多得宝支付】中转-->>通知-->>多得宝平台，通知多得宝该结果,发送参数给多得宝：";
						logger.info(t + rebstr);
						System.out.println(t + rebstr);
						writeBack(rebstr);
					}
				}

			} else {
				// 支付不成功
				// 更新订单状支付失败原因，好像失败微信没有回调
				String rebFail = "FAIL";
				logger.info(rebFail);
				System.out.println(rebFail);
				writeBack(rebFail);

			}

		} else {
			String er = "【多得宝支付】验签结果result的值：" + result + " -->Signature Error";
			System.out.println(er);
			logger.info(er);
			writeBack("Signature Error");
			// 验签失败，业务结束
		}

	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {

		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><dinpay><response><interface_version>V3.1</interface_version><merchant_code>2060226623</merchant_code><order_amount>1</order_amount><order_no>170605214315005</order_no><order_time>2017-06-05 21:43:15</order_time><qrcode>weixin://wxpay/bizpayurl?pr=bYDYk7a</qrcode><resp_code>SUCCESS</resp_code><resp_desc>通讯成功</resp_desc><result_code>0</result_code><sign>OQpdWZcqnnrnjqdSjXusomtTQvBCWBp61qTDdAY9pg2HbReDihyZ/gSgwmsCGSn/T/s2G7tGm4Kv5kMl8EeBivJiBCn2Z09LRYQdyReD/UgM5KNQNpHEFDhzJ8812bZ18T00W+tBF6p5kmFzQQfW2Y+tEYflyKtq5//zuXiRuKY=</sign><sign_type>RSA-S</sign_type><trade_no>1486074772</trade_no><trade_time>2017-06-05 21:44:31</trade_time></response></dinpay>";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream is = Util.getStringStream(str);
		Document document = builder.parse(is);
		// 获取到document里面的全部结点
		NodeList allNodes = document.getFirstChild().getFirstChild()
				.getChildNodes();
		Node node;
		Map<String, Object> map = new HashMap<String, Object>();
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);
			if (node instanceof Element) {
				map.put(node.getNodeName(), node.getTextContent());
			}
			i++;
		}
		System.out.println(JSONObject.fromObject(map).toString());
	}

}