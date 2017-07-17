<%@ page trimDirectiveWhitespaces="true"%>
<%@ page  import="java.util.*,java.io.*,java.text.DecimalFormat,java.io.*,java.text.SimpleDateFormat,com.entity.obj.*,,com.hx.BankList.java" pageEncoding="UTF-8"%>
<%

//测试环境地址： http://webservice.ips. net.cn/web/Service.asmx
//正式环境地址： http://webservice.ips. com.cn/web/Service.asmx


//提交地址http://webservice.ips.net.cn/web/Service.asmx/GetBankList
String form_url= "http://webservice.ips.net.cn/web/Service.asmx/GetBankList"; 
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
 
//  String Mer_code=pro.getProperty("Mer_code");
//  String Mer_key=pro.getProperty("Mer_key");

 String Mer_code="000166";
 String Mer_key="cQ0YAtyVNoiEeKrZJ5F5Qp2pMohwspfv6XoiU3wHYtcc1YOEhJ3SjDVMylmwmbD7jMhg5ifqjX67mzNN02p8MmlSb1KIqa5XF4TEQHjZEmJmhyAYxyVJMeLWBofdxwaj";
 
cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
String SignMD5 = b.toMD5( Mer_code+Mer_key).toLowerCase();

String keyWord  = "";
String enc = "UTF-8";
try {
	String rs =Api.GetShopsBankList();
	 keyWord =	java.net.URLDecoder.decode(rs, enc);
} catch (UnsupportedEncodingException e) {
	e.printStackTrace();
}

	System.out.println(keyWord);
out.print(keyWord);
%>