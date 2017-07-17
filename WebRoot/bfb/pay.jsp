<%@page import="com.bfb.MD5"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

//订单金额(保留2位小数)
DecimalFormat currentNumberFormat=new DecimalFormat("#0.00"); 
System.out.print("amount:"+obj.amount);
String Amount = currentNumberFormat.format(Double.parseDouble(obj.amount));

   //指令类型  网银-1，卡类-2
	String p1_md = "1";
	//商户订单号
	String p2_xn = obj.orderNo;
	//支付方式ID
	String p4_pd = obj.bankco;
	//产品名称
	String p5_name = "goods";
	//支付金额
	String p6_amount = Amount;
	//币种
	String p7_cr = "1";
	//扩展信息
	String p8_ex = key ;
	//是否通知 不通知=0，通知=1
	String p10_reply = "1";
	//调用模式    0 返回充值地址，由商户负责跳转     1 显示币付宝充值界面,跳转到充值    2 不显示币付宝充值界面 直接跳转到
	String p11_mode = "2";
	//版本号
	String p12_ver = "1";

	//签名值
	String sign;

  //订单日期
  String Date = obj.createTime;

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
	//商户ID
 //String p3_bn=pro.getProperty("bfb_code");
 //String Mer_key=pro.getProperty("bfb_key");
 String p3_bn= obj.mercode;
 String Mer_key=obj.merkey;
 
 String form_url=pro.getProperty("bfb_form_url");
 String p9_url=pro.getProperty("bfb_result_url");
 

 	StringBuffer str = new StringBuffer();
	str.append("p1_md=").append(p1_md)
	.append("&p2_xn=").append(p2_xn)
	.append("&p3_bn=").append(p3_bn)
	.append("&p4_pd=").append(p4_pd)
	.append("&p5_name=").append(p5_name)
	.append("&p6_amount=").append(p6_amount)
	.append("&p7_cr=").append(p7_cr)
	.append("&p8_ex=").append(p8_ex)
	.append("&p9_url=").append(p9_url)
	.append("&p10_reply=").append(p10_reply)
	.append("&p11_mode=").append(p11_mode)
	.append("&p12_ver=").append(p12_ver)
	.append(Mer_key);
	
	sign = MD5.getMD5Str(str.toString());


	
	// 创建参数队列    
	List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
		formparams.add(new BasicNameValuePair("p1_md", p1_md));  
		formparams.add(new BasicNameValuePair("p2_xn", p2_xn));  
		formparams.add(new BasicNameValuePair("p3_bn", p3_bn));  
		formparams.add(new BasicNameValuePair("p4_pd", p4_pd));  
		formparams.add(new BasicNameValuePair("p5_name", p5_name));  
		formparams.add(new BasicNameValuePair("p6_amount", p6_amount));  
		formparams.add(new BasicNameValuePair("p7_cr", p7_cr));  
		formparams.add(new BasicNameValuePair("p8_ex", p8_ex));  
		formparams.add(new BasicNameValuePair("p9_url", p9_url));  
		formparams.add(new BasicNameValuePair("p10_reply", p10_reply));  
		formparams.add(new BasicNameValuePair("p11_mode", p11_mode));  
		formparams.add(new BasicNameValuePair("p12_ver", p12_ver));  
		formparams.add(new BasicNameValuePair("sign", sign));  
		
		
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
	
	System.out.println("================================path:"+path);
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.write("billno=" + p2_xn + ";amount=" + Amount + ";date="+ Date+";bankco="+p4_pd+";merchanturl="+p9_url);
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
</head>
<body>
<form  id="frm1"  action="<%=form_url %>">
	<input type="hidden" name="p1_md" value="<%=p1_md %>" />
	<input type="hidden" name="p2_xn" value="<%=p2_xn %>" />
	<input type="hidden" name="p3_bn" value="<%=p3_bn %>" />
	<input type="hidden" name="p4_pd" value="<%=p4_pd %>" />
	<input type="hidden" name="p5_name" value="<%=p5_name %>" />
	<input type="hidden" name="p6_amount" value="<%=p6_amount %>" />
	<input type="hidden" name="p7_cr" value="<%=p7_cr %>" />
	<input type="hidden" name="p8_ex" value="<%=p8_ex %>" />
	<input type="hidden" name=p9_url value="<%=p9_url %>" />
	<input type="hidden" name="p10_reply" value="<%=p10_reply %>" />
	<input type="hidden" name="p11_mode" value="<%=p11_mode %>" />
	<input type="hidden" name="p12_ver" value="<%=p12_ver %>" />
	<input type="hidden" name="sign" value="<%=sign %>" />
	
<!-- 	<input type="submit" value="submit" /> -->
</form>

 <script language="javascript">
  document.getElementById("frm1").submit();
 </script>

</body>
</html>
 --%>