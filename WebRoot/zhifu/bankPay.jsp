<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.zf.ZFPayAction"%>
<%@page import="com.alipay.util.UtilDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="com.itrus.util.sign.*" %>

<%
//////////////////////////////////// 请求参数 //////////////////////////////////////
      Logger logger = Logger.getLogger("bankPay.jsp");
	// 接收表单提交参数
		request.setCharacterEncoding("UTF-8");			
		String merchant_code = request.getParameter("merchant_code");
		String merchant_private_key=request.getParameter("merchant_private_key");
	//	String service_type = request.getParameter("service_type");
		String service_type ="direct_pay";
		//String interface_version = request.getParameter("interface_version");		
		String interface_version ="V3.0";		
		//String input_charset = request.getParameter("input_charset");				
		String input_charset ="UTF-8";				
		String notify_url = request.getParameter("notify_url");
		//String sign_type = request.getParameter("sign_type");		
		String sign_type ="RSA-S";		
		String order_no = request.getParameter("order_no");		
		//String order_time = request.getParameter("order_time");		
		String order_time =UtilDate.getDateFormatter();		
		String order_amount = request.getParameter("order_amount");		
		String product_name = request.getParameter("product_name");		
		
		//String return_url = request.getParameter("return_url");
	//	String bank_code = request.getParameter("bank_code");		
		//String redo_flag = request.getParameter("redo_flag");
		String redo_flag ="0";//当值为1时不允许商户订单号重复提交；当值为 0或空时允许商户订单号重复提交 ;
		//String product_code = request.getParameter("product_code");
		//String product_num = request.getParameter("product_num");
		//String product_desc = request.getParameter("product_desc");
	//	String pay_type = request.getParameter("pay_type");
		//String client_ip = request.getParameter("client_ip");
	/* 	String extend_param = request.getParameter("extend_param"); */
		//String extra_return_param = request.getParameter("extra_return_param");
		//String show_url = request.getParameter("show_url");		

		/** 数据签名
		签名规则定义如下：
		（1）参数列表中，除去sign_type、sign两个参数外，其它所有非空的参数都要参与签名，值为空的参数不用参与签名；
		（2）签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，组成规则如下：
		参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n
		*/
		String pr="merchant_code="+merchant_code
		+"&merchant_private_key="+merchant_private_key
		+"&service_type="+service_type
		+"&interface_version="+interface_version
		+"&input_charset="+input_charset
		+"&notify_url="+notify_url
		+"&sign_type="+sign_type
		+"&order_no="+order_no
		+"&order_time="+order_time
		+"&order_amount="+order_amount
		+"&product_name="+product_name
		+"&redo_flag="+order_amount+"\n";
		
		System.out.println("[智付银联支付]接收的param:"+pr);
		logger.info("[智付银联支付]接收的param:"+pr);
	

		StringBuffer signSrc= new StringBuffer();	
		/* if (!"".equals(bank_code)) {
			signSrc.append("bank_code=").append(bank_code).append("&");	
		} */
		/* if (!"".equals(client_ip)) {
			signSrc.append("client_ip=").append(client_ip).append("&");	
		} */
		/* if (!"".equals(extend_param)) {
			signSrc.append("extend_param=").append(extend_param).append("&");	
		}
		if (!"".equals(extra_return_param)) {
			signSrc.append("extra_return_param=").append(extra_return_param).append("&");	
		}
		 */
		signSrc.append("input_charset=").append(input_charset).append("&");			
		signSrc.append("interface_version=").append(interface_version);
		signSrc.append("&merchant_code=").append(merchant_code);
		signSrc.append("&notify_url=").append(notify_url);					
		signSrc.append("&order_amount=").append(order_amount);
		signSrc.append("&order_no=").append(order_no);		
		signSrc.append("&order_time=").append(order_time);
		
		/* if (!"".equals(pay_type)) {
			signSrc.append("&pay_type=").append(pay_type);	
		}	
		if (!"".equals(product_code)) {
			signSrc.append("&product_code=").append(product_code);	
		}
		if (!"".equals(product_desc)) {
			signSrc.append("&product_desc=").append(product_desc);	
		}		 */
		signSrc.append("&product_name=").append(product_name);
		/* if (!"".equals(product_num)) {
			signSrc.append("&product_num=").append(product_num);	
		}	 */
		if (!"".equals(redo_flag)) {
			signSrc.append("&redo_flag=").append(redo_flag);	
		}
		/* if (!"".equals(return_url)) {
			signSrc.append("&return_url=").append(return_url);	
		} */	
		
		signSrc.append("&service_type=").append(service_type);
			
		/* if (!"".equals(show_url)) {
			signSrc.append("&show_url=").append(show_url);	
		} */
				
		
		String signInfo = signSrc.toString();
		
		String sign="";
		
		
		if("RSA-S".equals(sign_type)) {//sign_type = "RSA-S"
	
		/** 
			1)merchant_private_key，商户私钥，商户按照《密钥对获取工具说明》操作并获取商户私钥。获取商户私钥的同时，也要
   			获取商户公钥（merchant_public_key）并且将商户公钥上传到智付商家后台"公钥管理"（如何获取和上传请看《密钥对获取工具说明》），
  			不上传商户公钥会导致调试的时候报错“签名错误”。
  			2)demo提供的merchant_private_key是测试商户号1111110166的商户私钥，请自行获取商户私钥并且替换
  		*/
  	
  
			// merchant_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALf/+xHa1fDTCsLYPJLHy80aWq3djuV1T34sEsjp7UpLmV9zmOVMYXsoFNKQIcEzei4QdaqnVknzmIl7n1oXmAgHaSUF3qHjCttscDZcTWyrbXKSNr8arHv8hGJrfNB/Ea/+oSTIY7H5cAtWg6VmoPCHvqjafW8/UP60PdqYewrtAgMBAAECgYEAofXhsyK0RKoPg9jA4NabLuuuu/IU8ScklMQIuO8oHsiStXFUOSnVeImcYofaHmzIdDmqyU9IZgnUz9eQOcYg3BotUdUPcGgoqAqDVtmftqjmldP6F6urFpXBazqBrrfJVIgLyNw4PGK6/EmdQxBEtqqgXppRv/ZVZzZPkwObEuECQQDenAam9eAuJYveHtAthkusutsVG5E3gJiXhRhoAqiSQC9mXLTgaWV7zJyA5zYPMvh6IviX/7H+Bqp14lT9wctFAkEA05ljSYShWTCFThtJxJ2d8zq6xCjBgETAdhiH85O/VrdKpwITV/6psByUKp42IdqMJwOaBgnnct8iDK/TAJLniQJABdo+RodyVGRCUB2pRXkhZjInbl+iKr5jxKAIKzveqLGtTViknL3IoD+Z4b2yayXg6H0g4gYj7NTKCH1h1KYSrQJBALbgbcg/YbeU0NF1kibk1ns9+ebJFpvGT9SBVRZ2TjsjBNkcWR2HEp8LxB6lSEGwActCOJ8Zdjh4kpQGbcWkMYkCQAXBTFiyyImO+sfCccVuDSsWS+9jrc5KadHGIvhfoRjIj2VuUKzJ+mXbmXuXnOYmsAefjnMCI6gGtaqkzl527tw=";
			/* String merchant_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALpJStZUgs8SrJbP"+
					"2Zo8sqVKp3kJd4pypRixQwvEfQL0fB6ICCmvG7x5NMp19u1mQC32NZZao4bNL+Ni"+
					"xj7aZHH7BycAtrDQeNZzByVMf/fr6lMrfHpj3f63SB5mT8zPhXjLJm5RqaVjM3C6"+
					"E6vwVkw9ZJkZTE7m3B+saCceUaHFAgMBAAECgYAOnPM+7vA3+DoLtpBWVg6Zgsqy"+
					"Zvi2ppmm3zjoMmiwE6es6XTieDcPyN5IR4qwQsYkFN1NRxzu1Se9iaENiyo32FKX"+
					"hyG0gUsOAjRRh2eNTyJ0yV8lNl8CnL4zwq6wKK2YT0y3Fm/Wik9+EbvQMyc/N444"+
					"D970dfLIW0fI6l63fQJBAPB207Z6x1NtLabTasedAJUFg63k2wbNi+bQ4biX8Hq7"+
					"sMzlWRDdIKKGsMGy9cg11Yogv7WFF56UxKZTOaLlEj8CQQDGUmMKCSNzaDbzn7+2"+
					"LiwJ+JVWdMlEDkIgWO+mebk0+yEiAZ+6QSQw9m2/BNd4xhDgtPRTJUN8s5iq8s7M"+
					"28L7AkEA8Cq+aZNXYcu5vNWL4LK+0hgf3J6m47SH9pDJ4URfuzWa8AphWqaA4dME"+
					"13GWadDa5oqu7u1vGcQcZMLa3gdMoQJBAIlNrs3a59VVuH3RAsyhiNHabaHutw1M"+
					"BdIZYMnUCjq5BwZhM3Gi+eiAJBkUxA0Uq8i231TVPrUOLktkUXKZT5MCQHPQ/ykk"+
					"AF1YHZ/2Ja+SmarhXyJTfvEMlVUBEOYMMcW35+Ttvof8mc264ix87ip5YaKXiMmW"+
					"oAULhdO3aPnQfv0="; */
					
			 sign = RSAWithSoftware.signByPrivateKey(signInfo,merchant_private_key) ;  
			
			//System.out.println("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
			//System.out.println("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
		
		}
		
		if("RSA".equals(sign_type)){//数字证书加密方式 sign_type = "RSA"
		
			String rootPath=this.getClass().getResource("/").toString();
			
			//请在商家后台证书下载处申请和下载pfx数字证书，一般要1~3个工作日才能获取到,1111110166.pfx是测试商户号1111110166的数字证书
			String path= rootPath.substring(rootPath.indexOf("/")+1,rootPath.length()-8)+"certification/1111110166.pfx";	
			String pfxPass = "87654321"; //证书密钥，初始密码是商户号			  
			RSAWithHardware mh = new RSAWithHardware();						
			mh.initSigner(path, pfxPass);		  
			sign = mh.signByPriKey(signInfo);		
			
			//System.out.println("RSA商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
			//System.out.println("RSA商家发送的签名：" + sign.length() + " -->" + sign + "\n");
			}
		 
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
</head>
<body onLoad="document.dinpayForm.submit();">
  <form name="dinpayForm" method="post" action="https://pay.dinpay.com/gateway?input_charset=<%=input_charset%>" >
	<input type="hidden" name="sign" value="<%=sign%>" />
	<input type="hidden" name="merchant_code" value="<%=merchant_code%>" />
	<input type="hidden" name="service_type" value="<%=service_type%>" />	
	<input type="hidden" name="interface_version" value="<%=interface_version%>" />			
	<input type="hidden" name="input_charset" value="<%=input_charset%>" />	
	<input type="hidden" name="notify_url" value="<%=notify_url%>"/>
	<input type="hidden" name="sign_type" value="<%=sign_type%>" />		
	<input type="hidden" name="order_no" value="<%=order_no%>"/>
	<input type="hidden" name="order_time" value="<%=order_time%>" />	
	<input type="hidden" name="order_amount" value="<%=order_amount%>"/>
	<input type="hidden" name="product_name" value="<%=product_name%>" />	
<%-- 	<input type="hidden" name="return_url" value="<%=return_url%>"/>	 --%>
<%-- 	<input type="hidden" name="bank_code" value="<%=bank_code%>" />	 --%>
	<input type="hidden" name="redo_flag" value="<%=redo_flag%>"/>
<%-- 	<input type="hidden" name="product_code" value="<%=product_code%>"/>
	<input type="hidden" name="product_num" value="<%=product_num%>"/>
	<input type="hidden" name="product_desc" value="<%=product_desc%>"/>
	<input type="hidden" name="pay_type" value="<%=pay_type%>"/>
	<input type="hidden" name="client_ip" value="<%=client_ip%>"/>
	<input type="hidden" name="extend_param" value="<%=extend_param%>"/>
	<input type="hidden" name="extra_return_param" value="<%=extra_return_param%>"/> --%>
	<%-- <input type="hidden" name="show_url" value="<%=show_url%>"/> --%>
  </form>
</body>
</html>
