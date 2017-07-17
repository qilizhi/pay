<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*" %>
<%
	//记录日志
	String billno = request.getParameter("billno");//订单单号
	String amount = request.getParameter("amount");//金额
	String date = request.getParameter("date");//日期
	String succ = request.getParameter("succ");//是否成功Y：成功N：失败
	String msg = request.getParameter("msg");//发卡行返回消息
	String ipsbillno = request.getParameter("ipsbillno");//IPS订单号
	String retencodetype = request.getParameter("retencodetype");//
	String Currency_type = request.getParameter("Currency_type");//币种
	String signature = request.getParameter("signature");//数字签名摘要信息
	String bankbillno = request.getParameter("bankbillno");//银行订单号
	String ipsbanktime = request.getParameter("ipsbanktime");//IPS银行创建时间
	String Attach = request.getParameter("attach");//IPS银行创建时间
	String path ="";
	Properties prop = System.getProperties();
	
	
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
	 String Mer_code=pro.getProperty("Mer_code");
	 String Mer_key=pro.getProperty("Mer_key");
	
	
	//订单支付接口的Md5摘要，原文=订单号+金额+日期+支付币种+商户证书 
	cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
	//订单加密的明文 billno+【订单编号】+ currencytype +【币种】+ amount +【订单金额】+ date +【订单日期】+ orderencodetype +【订单支付接口加密方式】+【商户内部证书字符串】 
	  String SignMD5 = b.toMD5(billno + amount +date +succ +ipsbillno +Currency_type + Mer_key).toLowerCase();
	

	
	HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute("keyHashMap");
	System.out.println("ssss:"+map);
	PayObj obj = new PayObj();
	obj = map.get(Attach);
	
	System.out.println("==================success==============billno:"+billno);
	System.out.println("==================success=============amount:"+amount);
	System.out.println("==================success=============succ:"+succ);
	
	String os = prop.getProperty("os.name");
	if(os.startsWith("win") || os.startsWith("Win"))
	 	path = request.getRealPath("/log");
	else
		path = "/ngbs/local/apache-tomcat-6.0.44/log";
	File fp = new File(path, "callbackLog.txt");
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.write("billno=" + billno + ";amount=" + amount + ";date="
			+ date + ";succ=" + succ + ";msg=" + msg);
	bfwriter.newLine();
	bfwriter.write("ipsbillno=" + ipsbillno + ";retencodetype="
			+ retencodetype + ";signature=" + signature
			+ ";bankbillno=" + bankbillno + ";ipsbanktime="
			+ ipsbanktime);
	bfwriter.newLine();
	bfwriter.write("---------------------------------------------------------");
	bfwriter.newLine();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	bfwriter.write("Logdate=" + df.format(new Date()));
	bfwriter.newLine();
	bfwriter.write("IP=" + request.getRemoteAddr());
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.flush();
	bfwriter.close();
	fwriter.close();
	
	// 签名验证
	if(signature.equals(SignMD5)){
		//验签成功处理
		if(succ.equals("Y")){
			//交易成功处理
			succ = "0";
		}else{
			//交易失败处理
			succ = "-1";
		}
	}else{
		//验签失败处理
		succ = "1";
	}
	
%>


<html>
<head>
	<title></title>
</head>

<form  id="frm1"  action="<%=obj.merchanturl %>" method="post">
	<input type="hidden" name="billno" value="<%=billno %>" />
	<input type="hidden" name="amount" value="<%=amount %>" />
	<input type="hidden" name="date" value="<%=date %>" />
	<input type="hidden" name="succ" value="<%=succ %>" />
	<input type="hidden" name="msg" value="<%=msg %>" />
	<input type="hidden" name="ipsbillno" value="<%=ipsbillno %>" />
	<input type="hidden" name="retencodetype" value="<%=retencodetype %>" />
	<input type="hidden" name="signature" value="<%=signature %>" />
	<input type="hidden" name="bankbillno" value="<%=bankbillno %>" />
	<input type="hidden" name="ipsbanktime" value="<%=ipsbanktime %>" />
	
<!-- 	<input type="submit" value="submit" /> -->
</form>
 <script language="javascript">
   document.getElementById("frm1").submit();
 </script>
</html>