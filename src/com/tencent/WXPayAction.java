package com.tencent;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
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

import com.heepay.BaseAction;
import com.puxun.PuxunPayAction;
import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.Signature;
import com.tencent.common.XMLParser;
import com.util.HttpUtil;
import com.util.PropertiesUtil;
import com.util.StringUtils;


public class WXPayAction extends BaseAction {
		private static final long serialVersionUID = 1L;
		private static Logger logger = Logger.getLogger(WXPayAction.class);
		
		public void weixinPay() throws IllegalAccessException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, ParserConfigurationException, SAXException{
			HttpServletRequest request = ServletActionContext.getRequest();
			/**************************************交易下单***********************************************/
			//获取请求参数
			request.setCharacterEncoding("UTF-8");//传值编码
		//	response.setContentType("text/html;charset=UTF-8");//设置传输编码
			String spbill_create_ip=request.getParameter("spbill_create_ip");//机器ip 是否必传：Y
			String attach=request.getParameter("attach");//附加数据  是否必传：N
			String body=request.getParameter("body");//商品描述  是否必传：Y
			String product_id=request.getParameter("product_id");//商品ID 是否必传：Y
			String out_trade_no=request.getParameter("out_trade_no");//商户订单号 是否必传：Y
			String total_fee=request.getParameter("total_fee");//标价金额 是否必传：Y
			String paramStr="[微信扫码支付中转接口：] WXPayAction request params["+
					"spbill_create_ip:"+spbill_create_ip+"|"+
					"attach:"+attach+"|"+
					"body:"+body+"|"+
					"product_id:"+product_id+"|"+
					"out_trade_no:"+out_trade_no+"|"+
					"total_fee:"+total_fee+"|"+
					"]";
			System.out.println(paramStr);
			logger.info(paramStr);
			System.out.println("=============参数处理================");
			//因为传过来的是以元为单位，做转成分
			Double dT=Double.valueOf(total_fee);
			Integer tmount=new Double(dT*100).intValue();
			PayInfo pay=PayInfo.createPayInfo(spbill_create_ip,product_id,body,out_trade_no,attach,tmount);
			String sign=Signature.getSign(pay);
			pay.setSign(sign);
			HttpsRequest http=new HttpsRequest();
			System.out.println("=============参数处理================");
			System.out.println("=============开始下单================");
			String result=http.sendPost(Configure.UNIFIEDORDER_API,pay);
			System.out.println("==============返回xml结果============");
			System.out.println(result);
			Map<String,Object>map=XMLParser.getMapFromXML(result);
			//String QRString="";
			String jsonr=JSONObject.fromObject(map).toString();
			System.out.println("==============转换xml成JSON结果============");
			System.out.println(jsonr);
		/*	if("SUCCESS".equalsIgnoreCase(map.get("result_code").toString())){
				QRString=map.get("code_url").toString();
			}
			System.out.println(	"二维码code_url|:"+QRString);*/
			writeBack(jsonr);
		}
		
		public void WXNotifyCallback() throws ParserConfigurationException, IOException, SAXException{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			System.out.println("weixin 回调页面");
			System.out.println("//**************************   weixin 回调页面 run start  **************************//");
			request.setCharacterEncoding("UTF-8");//传值编码
			response.setContentType("text/html;charset=UTF-8");//设置传输编码
			//这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream is =  request.getInputStream();
			Document document = builder.parse(is);
			//获取到document里面的全部结点
			NodeList allNodes = document.getFirstChild().getChildNodes();
			Node node;
			Map<String, Object> map = new HashMap<String, Object>();
			int i=0;
			while (i < allNodes.getLength()) {
			    node = allNodes.item(i);
			    if(node instanceof Element){
			        map.put(node.getNodeName(),node.getTextContent());
			    }
			    i++;
			}
			/* {"is_subscribe":"Y",是否关注公众账号
				"appid":"wxee3e6d3312bc7802",
				"fee_type":"CNY",
				"nonce_str":"yupneyg6gt1gk7kj",
				"out_trade_no":"T201705112448",
				"device_info":"WEB",
				"transaction_id":"4000492001201705120593273439",
				"trade_type":"NATIVE", 交易类型
				"sign":"0E2CBD29BF93109D055FADBFEEB0FAA3",
				"result_code":"SUCCESS", 业务结果
				"mch_id":"1467128202",
				"total_fee":"1",
				"attach":"attach",
				"time_end":"20170512211929",
				"openid":"oL506wXiUTbNs1UEOTyUQfw-5DF4",
				"return_code":"SUCCESS", SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
				"bank_type":"GRCB_CREDIT",
				"cash_fee":"1"} */
			//微信回调参数验签 
			String tt="微信回过来的原字符串："+JSONObject.fromObject(map).toString();
			System.out.println(tt);
			logger.info(tt);
			Boolean isSign=Signature.checkIsSignValidFromResponseString(map);
			if(!isSign){
				//验签不成功o
				String  rebFail="<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败！]]></return_msg></xml>";
				logger.info(rebFail);
				System.out.println(rebFail);
				writeBack(rebFail);
				return ;
			}
			String resb="处理金额单位元前，微信 扫码支付回调接口的数据map:"+JSONObject.fromObject(map).toString()+"";
			System.out.println(resb);
			logger.info(resb);
			//微信传回金额单位由分处理为元单位
			String total_fee=map.get("total_fee").toString();
			Double tf=Double.valueOf(total_fee);
			Double tft=divide(tf,(double) 100,2);
			map.put("total_fee",tft);
			String res="处理金额单位元后，微信 扫码支付回调接口的数据map:"+JSONObject.fromObject(map).toString()+"";
			System.out.println(res);
			logger.info(res);
			//业务处理
			if("SUCCESS".equalsIgnoreCase(map.get("return_code").toString())){
				
				if("SUCCESS".equalsIgnoreCase(map.get("result_code").toString())){
				//支付成功
					//获取参数，全部返回
					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
					for(String key:map.keySet()){
						formparams.add(new BasicNameValuePair(key,map.get(key).toString()));	
					}
					String platformCallBackUrl=PropertiesUtil.getInstance().getValue("wx_pay_to_platform_url");
					if(StringUtils.isEmpty(platformCallBackUrl)){
						logger.error("get wx_pay_to_platform_url value is null");
					}else{
					//更新订单状态
					String erTop="中转平台向业务平台发送回调结果";
					String result=HttpUtil.post(formparams, platformCallBackUrl);
					System.out.println(erTop+"Url:"+platformCallBackUrl+"params:"+ArrayUtils.toString(formparams)+"result:"+result);
					if("SUCCESS".equalsIgnoreCase(result)){
						//业务平台更改状态成功，通知微信不再回调该结果
						String  rebstr="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
						String t="通知微信不再回调该结果,发送确认参数给微信：";
						logger.info(t+rebstr);
						System.out.println(t+rebstr);
						writeBack(rebstr);
					}else{
						String e="回调不成功！原因："+result;
						logger.info(e);
						System.out.println(e);
					}
					}
					
				}else{
					//支付不成功
					//更新订单状支付失败原因，好像失败微信没有回调
					
				}
			}else{
				String tr="与微信通信错误，没有业务逻辑！";
				logger.info(tr);
				System.out.println(tr);
			}
			String  rebFail="<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[处理失败！]]></return_msg></xml>";
			logger.info(rebFail);
			System.out.println(rebFail);
			writeBack(rebFail);
			
		}
		
		public   String   inputStream2String   (InputStream   in)   throws   IOException   { 
	        StringBuffer   out   =   new   StringBuffer(); 
	        byte[]   b   =   new   byte[4096]; 
	        for   (int   n;   (n   =   in.read(b))   !=   -1;)   { 
	                out.append(new   String(b,   0,   n)); 
	        } 
	        return   out.toString(); 
	} 
		// 进行除法运算
	    public static double divide(double d1,double d2,int len) {// 进行除法运算
	         BigDecimal b1 = new BigDecimal(d1);
	         BigDecimal b2 = new BigDecimal(d2);
	        return b1.divide(b2,len,BigDecimal.ROUND_HALF_UP).doubleValue();
	     }
}