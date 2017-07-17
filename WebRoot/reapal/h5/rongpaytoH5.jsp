<%-- <%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%> --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.reapal.config.*"%>
<%@ page import="com.reapal.utils.*,com.alibaba.fastjson.JSON"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.entity.obj.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.communicate.HttpClient.*" %>
<%@ page import="org.apache.http.*,org.apache.http.message.BasicNameValuePair" %>

<html>
	<head>
		<meta name="description" content="融宝支付" />
	  	<meta name="keywords" content="融宝支付" />
		<meta name="author" content="reapal" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
		<meta name="apple-mobile-web-app-title" content="融宝支付"/>
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta content="telephone=no" name="format-detection" />
		<title>融宝支付</title>
		<link rel="apple-touch-icon-precomposed" href="/apple-icon-114.png" />
	  	<link rel="apple-touch-startup-image" href="/apple-startup.png" />	
		<link type="text/css" rel="stylesheet" href="../common/css/html5/base.css" media="all" />
		<link type="text/css" rel="stylesheet" href="../common/css/html5/common.css" media="all" />
		<script type="text/javascript" src="../common/js/html5/jquery.min.js"></script>
		<script type="text/javascript" src="../common/js/html5/base.js"></script>
		<script type="text/javascript" src="../common/js/html5/cashier.js"></script>
	</head>
	<%
	
		//key，通过key获得单号，金额，时间等信息
		String key = request.getParameter("key");
		HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute("keyHashMap");
		PayObj obj = new PayObj();
		obj = map.get(key);
		
		//订单金额(保留2位小数)
// 		DecimalFormat currentNumberFormat=new DecimalFormat("#0.00"); 
// 		System.out.print("amount:"+obj.amount);
// 		String amount = currentNumberFormat.format(Double.parseDouble(obj.amount));
	
	
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
		 
		 
		 //String merchant_id=pro.getProperty("reapal_code");
		 //String reapal_key=pro.getProperty("reapal_key");
		  String merchant_id= obj.mercode;
          String reapal_key=obj.merkey;
		 
		 String form_url=pro.getProperty("reapal_form_url");
		 String notify_url=pro.getProperty("reapal_result_url");
		 String return_url=pro.getProperty("reapal_result_url");
		
		 
		
		//ReapalConfig.java中配置信息（不可以修改）
// 			String seller_email = ReapalH5Config.seller_email;
			// 签约融宝支付账号或卖家收款融宝支付帐户
			String seller_email = "820061154@qq.com";
// 			String merchant_id=ReapalH5Config.merchant_id;
// 			String notify_url = ReapalH5Config.notify_url;
// 			String return_url = ReapalH5Config.return_url;
			String transtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());//交易时间
			String currency = "156";
			String member_id = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			String member_ip = "192.168.1.1";
			String terminal_type = "mobile";
			String terminal_info = "sldfsldf";
// 			String reapal_key = ReapalH5Config.key;
// 			String sign_type=ReapalH5Config.sign_type;
			
			// 签名方式 不需修改
			String sign_type= "MD5";
			
			///////////////////////////////////////////////////////////////////////////////////
			
			//以下参数是需要通过下单时的订单数据传入进来获得
			//必填参数
			//请与贵网站订单系统中的唯一订单号匹配
			        String order_no = obj.orderNo ;
// 				   String order_no = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
			        
			        //订单名称，显示在融宝支付收银台里的“商品名称”里，显示在融宝支付的交易管理的“商品名称的列表里。
// 			        String title =new String(request.getParameter("title").getBytes("ISO-8859-1"),"utf-8");
			        String title = "商品名称";
			        
			        //订单描述、订单详细、订单备注，显示在融宝支付收银台里的“商品描述”里
// 			        String body =new String(request.getParameter("body").getBytes("ISO-8859-1"),"utf-8");
			        String body = "商品描述";
			        
			        //订单总金额，显示在融宝支付收银台里的“应付总额”里
			        String total_fee = obj.amount ;
// 			        String total_fee = request.getParameter("total_fee");
			        
			        BigDecimal amount = new BigDecimal(total_fee.toString()).movePointRight(2);

			        
			        String sHtmlText= "";
			////构造函数，生成请求URL
// 			        String sHtmlText = RongpayService.BuildFormH5( seller_email, merchant_id, notify_url,  return_url,  transtime,  currency,
// 			    			 member_id,  member_ip,  terminal_type,  terminal_info, reapal_key,  sign_type,
// 			    			 order_no,  title,  body,  amount.toString(), form_url);
			
			
			
			    	Map<String, String> sPara = new HashMap<String, String>();
					sPara.put("seller_email", seller_email);
					sPara.put("merchant_id", merchant_id);
					sPara.put("notify_url", notify_url);
					sPara.put("return_url", return_url);
					sPara.put("transtime", transtime);
					sPara.put("currency", currency);
					sPara.put("member_id", member_id);
					sPara.put("member_ip", member_ip);
					sPara.put("terminal_type", terminal_type);
					sPara.put("terminal_info", terminal_info);
					sPara.put("sign_type", sign_type);
					sPara.put("order_no", order_no);
					sPara.put("title", title);
					sPara.put("body", body);
					sPara.put("total_fee", amount.toString());
					sPara.put("payment_type", "2");
					sPara.put("pay_method", "bankPay");

					String mysign = Md5Utils.BuildMysign(sPara, reapal_key);// 生成签名结果

					sPara.put("sign", mysign);

					String json = JSON.toJSONString(sPara);

					Map<String, String> maps = DecipherH5.encryptData(json);
			
					
	     // 创建参数队列    
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	        	formparams.add(new BasicNameValuePair("merchant_id", merchant_id));  
	        	formparams.add(new BasicNameValuePair("data", maps.get("data")));  
	        	formparams.add(new BasicNameValuePair("encryptkey", maps.get("encryptkey")));  

	        String body1 =HttpClient.postForm(formparams,form_url);
			
			
		
	%>
	
	
<%= body1 %>
	<%-- <body>     
        <div class="w">
			<header id="top" class="headerred">
		    <h2>荣程商城</h2>
		    <a class="back" onclick="window.history.back()">返回</a>
		  </header>
			
		  <section class="cashier c">
		    <div class="success1">订单确认</div>
		     <div class="row-p">订单号：<span class="blue"><%=order_no%></span></div>
		     <div class="row-p">订单金额：<span class="blue"><%=total_fee%> </span></div>
<!-- 		     <div class="row-p"><span class="blue"><%=sHtmlText%></span></div> -->
		 	</section>
			<footer>
				<div class="copyright">Copyright &copy; 2015 融宝支付</div>	
			</footer>
	
		 </div>
	</body> --%>
</html>
