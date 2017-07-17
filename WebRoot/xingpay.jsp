<%@page import="com.puxun.util.DigestUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setCharacterEncoding("UTF-8");//传值编码
response.setContentType("text/html;charset=UTF-8");//设置传输编码


String PostUrl = "http://www.7xingpay.com/hspay/node";
String p1_MerId="24";								// 商户编号 是  Max(11)
String key="vR05jbBmxPJwlvVhMkuIuOqru7vtDw3u"; 		//商户密钥
String p0_Cmd = "Buy";									// 业务类型 是  Max(20)  固定值“Buy” . 1 

String p2_Order = request.getParameter("p2_Order")+"";	//  商户订单号 否  Max(50) 若不为””，提交的订单号必须在自身账户交易中唯一;为 ””
String p3_Amt = request.getParameter("p3_Amt") + "";	//支付金额 否  Max(20)  单位:元，精确到分.此参数为空则无法直连(如直连会报错：抱歉，交易金额太小。),必须到易宝网关让消费者输入金额 4 
String p4_Cur = "CNY";									//  交易币种 是  Max(10)  固定值 ”CNY”. 5 
String p5_Pid = new String("iphone".getBytes(),"UTF-8");									// 商品名称 否  Max(20) 用于支付时显示在易宝支付网关左侧的订单产品信息.
String p6_Pcat = new String("ee".getBytes(),"UTF-8");									// 商品种类 否  Max(20)  商品种类.
String p7_Pdesc = new String("goods".getBytes(),"UTF-8"); 									//商品描述 否  Max(20)  商品描述.
String p8_Url = "http://localhost/test";	    // 商户接收支付成功数据的地址 否  Max(200) 支付成功后易宝支付会向该地址发送两次成功通知
String pa_MP = new String("7xing".getBytes(),"UTF-8");	    // 商户扩展信息 否  Max(200)  返回时原样返回，此参数如用到中文，请注意转码. 11 
String pd_FrpId = request.getParameter("pd_FrpId") + "";  //支付通道编码 否  Max(50) 默认为 ”” ，到易宝支付网关，易宝支付网关默认显示已开通的全部支付通道.
String pr_NeedResponse = "1"; 							// 应答机制 否  Max(1) 固定值为“1”: 需要应答机制; 收到易宝支付服务器点对点支付成功通知，必须回写以”success”（无关大小写）开头的字符串，即使您收到成功通知时发现该订单已经处理过，也要正确回写”success”，否则易宝支付将认为您的系统没有收到通知，启动重发机制，直到收到”success”为止。 
String hmac = "";										// 签名数据    Max(32) 产生hmac需要两个参数，并调用相关API.

String sbOld = "";
sbOld += p0_Cmd;
sbOld += p1_MerId;
sbOld += p2_Order;
sbOld += p3_Amt;
sbOld += p4_Cur;
sbOld += p5_Pid;
sbOld += p6_Pcat;
sbOld += p7_Pdesc;
sbOld += p8_Url;
sbOld += pa_MP;
sbOld += pd_FrpId;
sbOld += pr_NeedResponse;

hmac = DigestUtil.hmacSign(sbOld, key); //数据签名

String result = "";
result += PostUrl;
result += "?p0_Cmd=" + p0_Cmd;
result += "&p1_MerId=" + p1_MerId;
result += "&p2_Order=" + p2_Order;
result += "&p3_Amt=" + p3_Amt;
result += "&p4_Cur=" + p4_Cur;
result += "&p5_Pid=" + p5_Pid;
result += "&p6_Pcat=" + p6_Pcat;
result += "&p7_Pdesc=" + p7_Pdesc;
result += "&p8_Url=" + p8_Url;
result += "&pa_MP=" +pa_MP;
result += "&pd_FrpId=" + pd_FrpId;
result += "&pr_NeedResponse=" + pr_NeedResponse;
result += "&hmac=" + hmac;
System.out.print("请求开始!url:"+result);
response.sendRedirect(result);
System.out.print("请求成功!");
%>
