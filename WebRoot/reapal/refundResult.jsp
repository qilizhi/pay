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
    //原订单号
	String orig_order_no = request.getParameter("orig_order_no");
	//退款金额
    String amount = request.getParameter("amount");
	//备注
	String note = request.getParameter("note");

	Map<String, String> map = new HashMap<String, String>();
	map.put("merchant_id", ReapalH5Config.merchant_id);
	map.put("order_no", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	map.put("orig_order_no", orig_order_no);

	BigDecimal total_fee = new BigDecimal(amount.toString()).movePointRight(2);

	map.put("amount", total_fee.toString());
	map.put("note", note);

	String url = "/fast/refund";

	String post = ReapalSubmit.buildSubmit(map, url);

	System.out.println("返回结果post==========>" + post);

	//解密返回的数据
	String res = DecipherH5.decryptData(post);
%>
<body>

	<%=res%>
</body>
</html>
