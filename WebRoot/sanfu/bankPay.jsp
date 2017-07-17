<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ page import="com.sanfu.md5"%>
<%@ page import="com.sanfu.getErrorInfo"%>
<jsp:useBean id='oMD5' scope='request' class='com.sanfu.md5'/>
<jsp:useBean id='oInfo' scope='request' class='com.sanfu.getErrorInfo'/>
<%  
Logger logger = Logger.getLogger("sanfu/bankPay.jsp");
// 接收表单提交参数
	request.setCharacterEncoding("UTF-8");	
	String MemberID = request.getParameter("MemberID");//商户号
	String PayID = request.getParameter("PayID");//支付渠道
	 SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA);
	String TradeDate =new String(formatter2.format(new Date()));;//下单日期
	//String TradeDate = request.getParameter("TradeDate");//下单日期
	String TransID = request.getParameter("TransID");//商户流水号
	String OrderMoney = request.getParameter("OrderMoney");//订单金额
	if (!"".equals(OrderMoney)) 
	{	BigDecimal a;
		a = new BigDecimal(OrderMoney).multiply(BigDecimal.valueOf(100)); //使用分进行提交
		OrderMoney=String.valueOf(a.setScale(0));
	}
	else{
		OrderMoney = ("0");
	}
	String RequestType = request.getParameter("RequestType");
	if("1".equals(RequestType)) {
		PayID = "";
	}
	String ProductName = request.getParameter("ProductName");//商品名称
	String Amount = request.getParameter("Amount");//商品数量
	String Username = request.getParameter("Username");//支付用户名称
	String AdditionalInfo = request.getParameter("AdditionalInfo");//订单附加信息
	String PageUrl = request.getParameter("PageUrl");//通知商户页面端地址
	String ReturnUrl = request.getParameter("ReturnUrl");//服务器底层通知地址
	String NoticeType = request.getParameter("NoticeType");//通知类型	
	//String Md5key = "abcdefg";///////////md5密钥（KEY）
	//String Md5key = "b90dfd16a96e4435";///////////md5密钥（KEY）
	String Md5key = request.getParameter("MemberKey");///////////md5密钥（KEY）
	
	String MARK = "|";
	String md5 =new String(MemberID+MARK+PayID+MARK+TradeDate+MARK+TransID+MARK+OrderMoney+MARK+PageUrl+MARK+ReturnUrl+MARK+NoticeType+MARK+Md5key);//MD5签名格式
	String Signature = oMD5.getMD5ofStr(md5);//计算MD5值
	String payUrl="http://gw.3yzf.com/v4.aspx";//借贷混合
	String TerminalID = "10000001"; 
	String InterfaceVersion = "4.0";
	String KeyType = "1";
	String pr="MemberID="+MemberID+"&"
	+"PayID="+PayID+"&"
	+"TransID="+TransID+"&"
	+"OrderMoney="+OrderMoney+"&"
	+"RequestType="+RequestType+"&"
	+"ProductName="+ProductName+"&"
	+"Amount="+Amount+"&"
	+"Username="+Username+"&"
	+"AdditionalInfo="+AdditionalInfo+"&"
	+"PageUrl="+PageUrl+"&"
	+"NoticeType="+NoticeType+"&"+"payUrl="+payUrl+"&"
	+"ReturnUrl="+ReturnUrl+"&"
	+"TerminalID="+TerminalID+"&"
	+"InterfaceVersion="+InterfaceVersion+"&"
	+"KeyType="+KeyType;
	System.out.println("[闪付银联支付]接收的param:"+pr);
	logger.info("[闪付银联支付]接收的param:"+pr);
	session.setAttribute("OrderMoney",OrderMoney); //设置提交金额的Session
 %>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>充值接口-提交信息处理</title>
</head>

<body onload="pay.submit()">
<form method="post" name="pay" id="pay" action="<%=payUrl%>">
<TABLE>
<TR>
	<TD>
	<input name='MemberID' type='hidden' value= "<%=MemberID%>"/>
	<input name='TerminalID' type='hidden' value= "<%=TerminalID%>"/>
	<input name='InterfaceVersion' type='hidden' value= "<%=InterfaceVersion%>"/>
	<input name='KeyType' type='hidden' value= "<%=KeyType%>"/>
	<input name='PayID' type='hidden' value= "<%=PayID%>"/>		
	<input name='TradeDate' type='hidden' value= "<%=TradeDate%>" />
	<input name='TransID' type='hidden' value= "<%=TransID%>" />
	<input name='OrderMoney' type='hidden' value= "<%=OrderMoney%>"/>
	<input name='ProductName' type='hidden' value= "<%=ProductName%>"/>
	<input name='Amount' type='hidden' value= "<%=Amount%>"/>
	<input name='Username' type='hidden' value= "<%=Username%>"/>
	<input name='AdditionalInfo' type='hidden' value= "<%=AdditionalInfo%>"/>
	<input name='PageUrl' type='hidden' value= "<%=PageUrl%>"/>
	<input name='ReturnUrl' type='hidden' value= "<%=ReturnUrl%>"/>	
	<input name='Signature' type='hidden' value="<%=Signature%>"/>
	<input name='NoticeType' type='hidden' value= "<%=NoticeType%>"/>
	</TD>
</TR>
</TABLE>
	
</form>	

</body>
</html>
