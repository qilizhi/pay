<%@page import="com.bfb.MD5"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.entity.obj.*" %>
<%@ page import="java.io.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**************************************订单查询***********************************************/

	//平台订单号(SN或者XN其中一项必须有值)
	String SN = "201511060010028135";
	//商户订单号
	String XN = "";
	//签名值
	String SIGN ;
	//查询日期
	String DATE = "2015-11-06";
	
	
	//读取商户用户信息
	  Properties pro = new Properties();  
	 String realpath = request.getRealPath("/WEB-INF/classes");  
	 try{  
	 //读取配置文件
	 FileInputStream in = new FileInputStream(realpath+"/config.properties");  
	 pro.load(in);  
	 }  
	 catch(FileNotFoundException e){  
	     out.println(e);  
	 }  
	 catch(IOException e){out.println(e);}
	 String BN=pro.getProperty("bfb_code");
	 String Mer_key=pro.getProperty("bfb_key");
	
// 	填入商户key
	StringBuffer str = new StringBuffer();
	str.append(BN).append(Mer_key);
	
	SIGN = MD5.getMD5Str(str.toString());
	
%>
<html>
<head>
	<title>订单查询</title>
</head>
<body>
<form action="http://www.5dd.com/frontpage/OrderInfo">
	<input type="text" name="BN" value="<%=BN %>" />
	<input type="text" name="SN" value="<%=SN %>" />
	<input type="text" name="XN" value="<%=XN %>" />
	<input type="text" name="SIGN" value="<%=SIGN %>" />
	<input type="text" name="DATE" value="<%=DATE %>" />
	
	<input type="submit" value="submit" />
</form>
</body>
</html>
