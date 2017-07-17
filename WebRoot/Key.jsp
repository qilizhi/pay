<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,java.io.*,net.eason.engine.util.MD5Util" pageEncoding="UTF-8"%>
<%@ page import="java.text.DecimalFormat"%> 
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*" %>
<%
String res = "0";
String errInfo = "";
try{
String keyHashMap = "keyHashMap";
HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute(keyHashMap);
if (map == null) {
 map = new HashMap<String, PayObj>();
 application.setAttribute(keyHashMap, map);
}
//String key = String.valueOf(System.currentTimeMillis())+String.valueOf(new Random().nextInt(10000000));
PayObj payObj = new PayObj();


String serviceIp = request.getParameter("IP");//订单单号
String billno = request.getParameter("billno");//订单单号
String amount = request.getParameter("amount");//金额
String datetime = request.getParameter("datetime");//日期
String mercode = request.getParameter("mercode");//商户code
String transferkey = request.getParameter("transferkey");//中转密钥
String loopbackaddress = request.getParameter("loopbackaddress");//回调地址
String platformid = request.getParameter("platformid");//平台ID
String paykey = request.getParameter("paykey");//平台ID
String code = request.getParameter("code");//平台ID


//读取配置信息
Properties pro = new Properties();
String realpath = request.getRealPath("/WEB-INF/classes");
try {
	//读取配置文件
	FileInputStream in = new FileInputStream(realpath + "/config.properties");
	pro.load(in);
} catch (FileNotFoundException e) {
	out.println(e);
} catch (IOException e) {
	out.println(e);
}

String platformid1= pro.getProperty("platformid");
String paykey1 = pro.getProperty("paykey");

String md5code =MD5Util.md5Hex(serviceIp+billno+amount+datetime+transferkey+loopbackaddress+platformid1+paykey1).toUpperCase();


String key = billno ;

String ipStr = request.getRemoteAddr();
	System.out.println("===========       serviceIp:"+serviceIp);
	System.out.println("===========       ipStr:"+ipStr);

/**	
if(!ipStr.equals(serviceIp)){
	res = "1";
	errInfo = "IP地址不合法！";
	System.out.println("===========       ipStr: false");
}else{
	**/
	if(md5code.equals(code)){
		payObj.orderNo = billno;
		payObj.amount = amount ;
		payObj.datetime = datetime ;
		payObj.mercode = mercode ;
		payObj.transferkey = transferkey ;
		payObj.loopbackaddress = loopbackaddress ;
		payObj.platformid = platformid ;
		payObj.paykey = paykey ;
		payObj.code = code ;
	
		map.put(key, payObj);
	
		res = key;
		errInfo = "SUCCESS";
	}else{
		res = "1";
		errInfo = "密钥验证不通过！";
		System.out.println("===========    密钥验证不通过");
	}

//}
System.out.println("===========  mercode:"+mercode);
System.out.println("===========       code:"+code);
System.out.println("===========md5code:"+md5code);
System.out.println("===========          res:"+res);


/**************************************记录日志***********************************************/	
	String path = "";
	Properties prop = System.getProperties();
	String os = prop.getProperty("os.name");
	if(os.startsWith("win") || os.startsWith("Win"))
	 	path = request.getRealPath("/Log");
	else
		path = "/ngbs/local/apache-tomcat-6.0.44/log";
	File fp = new File(path, "KeyLog.txt");
	
	System.out.println("================================"+path);
	
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("======================================================================");
	bfwriter.newLine();
	bfwriter.write("billno=" + billno + ";amount=" + amount + ";datetime="+ datetime + ";res="+ res  + ";errInfo="+ errInfo + ";loopbackaddress="+ loopbackaddress );
	bfwriter.newLine();
	bfwriter.write("key=" + key);
	bfwriter.newLine();
	bfwriter.write("---------------------------------------------------------");
	bfwriter.newLine();
	bfwriter.write("IP=" + request.getRemoteAddr());
	bfwriter.newLine();
	bfwriter.write("=======================================================================");
	bfwriter.newLine();
	bfwriter.flush();
	bfwriter.close();
	fwriter.close();
	
}catch(Exception ex){
	res = "1";
}
// HttpServletResponse response = this.getResponse();
// PrintWriter out = response.getWriter();
// out.write(res);
// out.flush();
// out.close();    

// res = "jsonpCallback"+"({\"result\":\""+res+"\"})";//返回必须是json字符串'
out.print(res);
System.out.println("================================res:"+res);
%>