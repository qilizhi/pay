<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,java.io.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.DecimalFormat"%> 
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*" %>
<%@ page import="com.communicate.HttpClient.*" %>
<%@ page import="org.apache.http.*,org.apache.http.message.BasicNameValuePair" %>
<%

/**************************************交易下单***********************************************/

//key，通过key获得单号，金额，时间等信息
String key = request.getParameter("key");
HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute("keyHashMap");
PayObj obj = new PayObj();
obj = map.get(key);
System.out.println(obj);
//商户订单编号
String Billno = obj.orderNo;

//订单金额(保留2位小数)
DecimalFormat currentNumberFormat=new DecimalFormat("#0.00"); 
String Amount = currentNumberFormat.format(Double.parseDouble(obj.amount));
//订单日期
String Date = obj.createTime;
//是否银行直连
String DoCredit = obj.docredit;
//银行代码
// String Bankco = obj.bankco;
String Bankco = "00018";     

//支付成功返回url
String Merchanturl = obj.merchanturl;
//支付失败返回url
String FailUrl = obj.failurl;
//支付结果错误返回的商户URL  建议传空
String ErrorUrl = obj.errorurl;
//Server to Server 返回页面URL
String ServerUrl =obj.serverurl;
//支付卡种
String Gateway_Type ="01"; //01：借记卡，04：IPS账户支付，08：IB支付,16:电话支付,64:储值卡支付
//币种  RMB 人民币
String Currency_Type = "RMB" ;
//语言
String Lang = "GB";
//商户数据包
String Attach =key;// 存放商户自己的信息，当订单返回的时候原封不动的返回给商户
//订单支付接口加密方式
String OrderEncodeType = "5";//  5:md5摘要
//交易返回接口加密方式 
String RetEncodeType = "12";//17:md5摘要 16:md5withRsa
//返回方式
String Rettype ="1";// 1:有Server to Server 0:无Server to Server



//读取配置文件信息
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
 catch(IOException e){
	 out.println(e);
}
 //String Mer_code=pro.getProperty("Mer_code");
 //String Mer_key=pro.getProperty("Mer_key");
 
 String Mer_code= obj.mercode;
 String Mer_key=obj.merkey;
 
 String form_url=pro.getProperty("hx_form_url");
 String result_url=pro.getProperty("hx_result_url");


//订单支付接口的Md5摘要，原文=订单号+金额+日期+支付币种+商户证书 
cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
//订单加密的明文 billno+【订单编号】+ currencytype +【币种】+ amount +【订单金额】+ date +【订单日期】+ orderencodetype +【订单支付接口加密方式】+【商户内部证书字符串】 
String SignMD5 = b.toMD5("billno"+Billno +"currencytype"+Currency_Type+"amount"+ Amount + "date" +Date +"orderencodetype"+OrderEncodeType + Mer_key).toLowerCase();


// 创建参数队列    
List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
	formparams.add(new BasicNameValuePair("Mer_code", Mer_code));  
	formparams.add(new BasicNameValuePair("Billno", Billno));  
	formparams.add(new BasicNameValuePair("Amount", Amount));  
	formparams.add(new BasicNameValuePair("Date", Date));  
	formparams.add(new BasicNameValuePair("Currency_Type", Currency_Type));  
	formparams.add(new BasicNameValuePair("Gateway_Type", Gateway_Type));  
	formparams.add(new BasicNameValuePair("Lang", Lang));  
	formparams.add(new BasicNameValuePair("Merchanturl", Merchanturl));  
	formparams.add(new BasicNameValuePair("FailUrl", FailUrl));  
	formparams.add(new BasicNameValuePair("ErrorUrl", ErrorUrl));  
	formparams.add(new BasicNameValuePair("Attach", Attach));  
	formparams.add(new BasicNameValuePair("OrderEncodeType", OrderEncodeType));  
	formparams.add(new BasicNameValuePair("RetEncodeType", RetEncodeType));  
	formparams.add(new BasicNameValuePair("Rettype", Rettype));  
	formparams.add(new BasicNameValuePair("ServerUrl", ServerUrl));  
	formparams.add(new BasicNameValuePair("SignMD5", SignMD5));  
	formparams.add(new BasicNameValuePair("DoCredit", DoCredit));  
	formparams.add(new BasicNameValuePair("Bankco", Bankco));  


String body =HttpClient.postForm(formparams,form_url);


/**************************************记录日志***********************************************/
	
	String path = "";
	Properties prop = System.getProperties();
	String os = prop.getProperty("os.name");
	if(os.startsWith("win") || os.startsWith("Win"))
	 	path = request.getRealPath("/log");
	else
		path = "/ngbs/local/apache-tomcat-6.0.44/log";
	File fp = new File(path, "postLog.txt");
	
	
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.write("billno=" + Billno + ";amount=" + Amount + ";date="+ Date+";docredit=" +DoCredit +";bankco="+Bankco+";merchanturl="+Merchanturl);
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
%>

<%= body %>

<%-- 
<html>
  <head>
    <title>支付中间跳转</title>
    <meta http-equiv="content-Type" content="text/html; charset=gb2312" />
  </head>
  <body>
    <form action="<%=hx_form_url%>" method="post" id="frm1">
      <input type="text" name="Mer_code" value="<%=Mer_code%>">
      <input type="text" name="Billno" value="<%=Billno%>">
      <input type="text" name="Amount" value="<%=Amount%>" >
      <input type="text" name="Date" value="<%=Date%>">
      <input type="text" name="Currency_Type" value="<%=Currency_Type%>">
      <input type="text" name="Gateway_Type" value="<%=Gateway_Type%>">
      <input type="text" name="Lang" value="<%=Lang%>">
      <input type="text" name="Merchanturl" value="<%=hx_resulturl%>">
      <input type="text" name="FailUrl" value="<%=hx_resulturl%>">
      <input type="text" name="ErrorUrl" value="<%=hx_resulturl%>">
      <input type="text" name="Attach" value="<%=Attach%>">
      <input type="text" name="OrderEncodeType" value="<%=OrderEncodeType%>">
      <input type="text" name="RetEncodeType" value="<%=RetEncodeType%>">
      <input type="text" name="Rettype" value="<%=Rettype%>">
      <input type="text" name="ServerUrl" value="<%=ServerUrl%>">
      <input type="text" name="SignMD5" value="<%=SignMD5%>">
      <input type="text" name="DoCredit" value="<%=DoCredit%>">
      <input type="text" name="Bankco" value="<%=Bankco%>">
      <input type="submit" value="submit" />
    </form>
    <script language="javascript">
       document.getElementById("frm1").submit();
    </script>
  </body>
</html> 
 --%>