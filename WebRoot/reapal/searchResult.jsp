<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.reapal.config.*"%>
<%@ page import="com.reapal.utils.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.alibaba.fastjson.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <%
      //商户订单号
	  String order_no = request.getParameter("order_no").trim();
  	
    Map<String, String> map = new HashMap<String, String>();
	map.put("merchant_id", ReapalH5Config.merchant_id);
	map.put("version", ReapalH5Config.version);
	map.put("order_no", order_no);
	
	String url = "/fast/search";

	String post = ReapalSubmit.buildSubmit(map, url);

    System.out.println("返回结果post==========>" + post);
    
    //解密返回的数据
    String res = DecipherH5.decryptData(post);
  %>
  <body>
		
		<%=res%>
  </body>
</html>
