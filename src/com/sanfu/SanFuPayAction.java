package com.sanfu;

import java.io.DataInputStream;
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
import com.ekapay.util.ConfigUtil;
import com.ekapay.util.DataHelper;
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

public class SanFuPayAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SanFuPayAction.class);
	
	


	public void sanFuNotifyCallback() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String remotehost=request.getRemoteHost();
		try {
			/*int totalbytes = request.getContentLength();
			String reqcontent="";
			if(totalbytes!=-1){
			byte[] dataOrigin = new byte[totalbytes];
			InputStream body=request.getInputStream();
			 DataInputStream in = new DataInputStream(body);
			 in.readFully(dataOrigin);
			 in.close();
			 reqcontent = new String(dataOrigin); 
			}*/
			String requestp="【闪付支付】通知-->中转平台-->充值回调host:"+remotehost+" body:\n " +
					"  【闪付支付】通知-->中转平台-->充值回调结果, params:{"+ JSONObject.fromObject(request.getParameterMap()).toString()+ "}";
			 logger.info(requestp);
			 System.out.println(requestp);
			 String MemberID = request.getParameter("MemberID");//商户号
			String TerminalID = request.getParameter("TerminalID");//商户终端号
			String TransID = request.getParameter("TransID");//商户流水号
			String Result = request.getParameter("Result");//支付结果
			String ResultDesc = request.getParameter("ResultDesc");//支付结果描述
			String FactMoney = request.getParameter("FactMoney");//实际成功金额
			String AdditionalInfo = request.getParameter("AdditionalInfo");//订单附加消息	
			String SuccTime = request.getParameter("SuccTime");//支付完成时间
			String Md5Sign = request.getParameter("Md5Sign");//MD5签名
			//String Md5key = "abcdefg";
			String MARK = "~|~";
			String md5 = "MemberID=" + MemberID + MARK + "TerminalID=" + TerminalID + MARK + "TransID=" + TransID + MARK + "Result=" + Result + MARK + "ResultDesc=" + ResultDesc + MARK
					+ "FactMoney=" + FactMoney + MARK + "AdditionalInfo=" + AdditionalInfo + MARK + "SuccTime=" + SuccTime
					+ MARK + "Md5Sign=" + Md5Sign;
    
			
			if (StringUtils.isEmpty(Md5Sign)) {
				writeBack("error");
				logger.error("【闪付支付】中转平台-->接收回调结果, hmac(检验码)不能为空");
				return;
			}
			/*String WaitSign = oMD5.getMD5ofStr(md5);	
			if(WaitSign.compareTo(Md5Sign)==0){
				System.out.println("OK");
				//校验通过开始处理订单		
				out.println("OK");//全部正确了输出OK		
			}else{
				System.out.println("Md5CheckFail");
				out.println("Md5CheckFail'");//MD5校验失败

			}*/
			StringBuffer postData = new StringBuffer();
			postData.append("MemberID=" + MemberID);
			postData.append("&TerminalID=" + TerminalID);
			postData.append("&TransID=" + TransID);
			postData.append("&Result=" + Result);
			postData.append("&ResultDesc=" + ResultDesc);
			postData.append("&FactMoney=" + FactMoney);
			postData.append("&AdditionalInfo=" + AdditionalInfo);
			postData.append("&SuccTime=" + SuccTime);
			postData.append("&Md5Sign=" + Md5Sign);
			logger.info("【闪付支付】回调的参数结果, response:{" + postData.toString() + "}");
			String response = DataHelper.RequestPostUrl(
					ConfigUtil.getSanFuCallBackUrl(), postData.toString());
			if("success".equalsIgnoreCase(response)){
			writeBack("OK");// 写成功通知回去
			}else{
				writeBack("ERROR");// 写成功通知回去
			}
			String toback="【闪付支付】现金平台业务-->中转平台-->返回结果, response:{" + response + "}";
			System.out.println(toback);
			logger.info(toback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("【闪付支付】回调出错"+e.getMessage());
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