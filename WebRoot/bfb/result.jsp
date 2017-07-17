<%@page import="com.bfb.MD5"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**************************************交易回单***********************************************/

	
	System.out.println("================================path:交易回单");
	
	//指令类型  网银-1，卡类-2
	String p1_md = request.getParameter("p1_md");
	//BFB订单号
	String p2_sn = request.getParameter("p2_sn");
	//商户订单号
	String p3_xn = request.getParameter("p3_xn");
	//支付金额
	String p4_amt = request.getParameter("p4_amt");
	//扩展信息
	String p5_ex = request.getParameter("p5_ex");
	//支付方式
	String p6_pd = request.getParameter("p6_pd");
	//状态  成功=success，失败=faile
	String p7_st = request.getParameter("p7_st");
	//通知方式 1=通知,2=显示
	String p8_reply = request.getParameter("p8_reply");
	//签名值
	String sign = request.getParameter("sign");
	
	
	HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute("keyHashMap");
	System.out.println("ssss:"+map);
	PayObj obj = new PayObj();
	obj = map.get(p5_ex);
	
	System.out.print("merchanturl:"+obj.merchanturl);
	
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
	 
	 String Mer_key=pro.getProperty("bfb_key");
	
	 String succ = "-1";
	 
	StringBuffer str = new StringBuffer();
	str.append("p1_md=");
	str.append(p1_md);
	str.append("&p2_sn=");
	str.append(p2_sn);
	str.append("&p3_xn=");
	str.append(p3_xn);
	str.append("&p4_amt=");
	str.append(p4_amt);
	str.append("&p5_ex=");
	str.append(p5_ex);
	str.append("&p6_pd=");
	str.append(p6_pd);
	str.append("&p7_st=");
	str.append(p7_st);
	str.append("&p8_reply=");
	str.append(p8_reply);
	str.append(Mer_key);
	
	
	
	String path = "";
	Properties prop = System.getProperties();
	String os = prop.getProperty("os.name");
	if(os.startsWith("win") || os.startsWith("Win"))
	 	path = request.getRealPath("/log");
	else
		path = "/ngbs/local/apache-tomcat-6.0.44/log";
	File fp = new File(path, "result.txt");
	
	System.out.println("================================path:"+path);
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.write("p2_sn=" + p2_sn + ";p3_xn=" + p3_xn + ";p4_amt="+ p4_amt+";merchanturl="+request.getRequestURL());
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
	
	
	if(sign.equals(MD5.getMD5Str(str.toString()).toUpperCase())){
		//验签成功处理
		if(p7_st.equals("success")){
			//交易成功处理
			succ = "0";
// 			out.print("success");
			
			   if(p8_reply.equals("1"))  //判断通知的P8参数是否为１，若为１，要回复币付宝success
			   {
				   out.print("success");
			   }
			
		}else{
			//交易失败处理
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
	<input type="hidden" name="p1_md" value="<%=p1_md %>" />
	<input type="hidden" name="p2_sn" value="<%=p2_sn %>" />
	<input type="hidden" name="p3_xn" value="<%=p3_xn %>" />
	<input type="hidden" name="p4_amt" value="<%=p4_amt %>" />
	<input type="hidden" name="p5_ex" value="<%=p5_ex %>" />
	<input type="hidden" name="p6_pd" value="<%=p6_pd %>" />
	<input type="hidden" name="p7_st" value="<%=p7_st %>" />
	<input type="hidden" name="p8_reply" value="<%=p8_reply %>" />
	<input type="hidden" name="sign" value="<%=sign %>" />
	<input type="hidden" name="key" value="<%=Mer_key %>" />
	<input type="hidden" name="succ" value="<%=succ %>" />
	
<!-- 	<input type="submit" value="submit" /> -->
</form>
 <script language="javascript">
   document.getElementById("frm1").submit();
 </script>
</html>