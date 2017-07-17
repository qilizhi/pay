<%@page import="com.bfb.DesUtil"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="com.bfb.MD5"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**************************************委托结算***********************************************/

	//结算类型：1、自助结算(暂不开放)；2、委托结算
	String SAT = "2";
	//商户号：商户开户后在商户后台查询。
	String BN = "25771756";
	//提交数据串，格式：订单号(字母和数字或下划线(_)组成,4到32位,当前批次不能重复.),卡号,户名,银行名称,联行号,开户行全称,开户行所在省,开户行所在市,是否公司账户(0:私人账户;1:公司账户),结算金额(1.00),手续费承担方式(0:收款人承担;1:申请人承担)。
	//每个字段间用英文逗号(,)分割，多条记录间用单管道符(|)分割。
	//数据组装好后采用DES加密，密钥在商户后台获取，向量统一为BEBE_PAY
	String DATA = "123456,6212273100000633789,王非,中国工商银行,103624447018,中国农业银行广西贵港市平南县支行,广西壮族自治区,南宁市,0,5.00,0|51243,6212273100000633789,宁夏,中国工商银行,103624447018,中国农业银行广西贵港市平南县支行,广西壮族自治区,南宁市,0,10.00,0";
	//签名
	String SIGN = "";
	//DESiv
	String desiv = "BEBE_PAY";
	//商户key
	String key = "9913c52362ef4f329cd27783dc5528c7";
	
	//组装参数生成SIGN
	StringBuffer str = new StringBuffer();
	str.append(DATA.replace(" ", "+")).append(key);
	SIGN = MD5.getMD5Str(str.toString());
	
	//DES加密
	byte[] keyByte = key.substring(0, 8).getBytes();
    byte[] ivByte = desiv.getBytes();
    byte[] data = DesUtil.CBCEncrypt(DATA.replace(" ", "+").getBytes("utf-8"), keyByte, ivByte);
    //加密转换  
    BASE64Encoder enc = new BASE64Encoder(); 
    DATA = enc.encode(data); 
  
%>
<html>
<head>
	<title>test</title>
</head>
<body>
<form action="http://www.5dd.com/frontpage/payout" >
	<input type="text" name="SAT" value="<%=SAT %>" />
	<input type="text" name="BN" value="<%=BN %>" />
	<input type="text" name="DATA" value="<%=DATA %>" />
	<input type="text" name="SIGN" value="<%=SIGN %>" />
	
	<input type="submit" value="submit" />
</form>
</body>
</html>
