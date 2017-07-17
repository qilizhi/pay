<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
  <head>    
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head> 
  <body>
	<form method="post" action="GetMessCode.jsp" target="_blank">
		<br>接口版本(interface_version)：<input Type="text" Name="interface_version" value="V3.0"/> *		
		<br>服务类型(service_type)：<input Type="text" Name="service_type" value="sign_pay_sms_code"> *				
		<br>参数编码字符集(input_charset)
			<select name="input_charset">
				<option value="UTF-8">UTF-8</option>
				<option value="GBK">GBK</option>
			</select> *										
		<br>签名方式(sign_type)：
			<select name="sign_type">
				<option value="RSA-S">RSA-S</option>
				<option value="RSA">RSA</option>
			</select> *														
		<br>商 家 号(merchant_code)：<input Type="text" Name="merchant_code" value="1111110166"> *	
		<br>商户订单号(order_no)：<input Type="text" Name="order_no" value="<%=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())%>"> *
		<br>商户订单金额(order_amount)：<input Type="text" Name="order_amount" value="0.01"> *
		<br>验证码类型(sms_type)：
			<select name="sms_type">
				<option value="0">签约+支付验证码</option>
				<option value="1">支付验证码</option>
			</select> *	
		<br>发送类型(send_type)：
			<select name="send_type">
				<option value="0">平台下发</option>
			</select> *			
		<br>手机号(mobile)：<input Type="text" Name="mobile" value="13265627543"> *			
		<br>签约号(merchant_sign_id)：<input Type="text" Name="merchant_sign_id" value="8e8062fce7a47afb"> *	
		<br>-------------------------------------------------------------	
		<br><input Type="submit" Name="submit" value="获取短信验证码">
	</form>
  </body>
</html>
