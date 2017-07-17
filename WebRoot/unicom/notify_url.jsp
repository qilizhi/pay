<%
/* *
     * 订单校验测试请求样式：
     * <?xml version="1.0" encoding="UTF-8"?><checkOrderIdReq><orderid>000000000000000000000001</orderid><signMsg>3BDD679B092CEFBF723F198795CA67D7</signMsg><usercode>15600000000</usercode><provinceid>00001</provinceid><cityid>00001</cityid></checkOrderIdReq>
     * 
     * 支付结果通知请求样式：
     * 1. 支付失败
     * <?xml version="1.0" encoding="UTF-8"?><callbackReq><orderid>000000000000000000000001</orderid><ordertime>20160630120000</ordertime><cpid>test</cpid><appid>test</appid><fid>test</fid><consumeCode>9999999999999999999999999999</consumeCode><payfee>1000</payfee><payType>4</payType><hRet>1</hRet><status>00100</status><signMsg>BB2F5AEE289C68A1F1D0C390EF6944C4</signMsg></callbackReq>
     * 
     * 2. 支付成功
     * <?xml version="1.0" encoding="UTF-8"?><callbackReq><orderid>000000000000000000000001</orderid><ordertime>20160630120000</ordertime><cpid>test</cpid><appid>test</appid><fid>test</fid><consumeCode>9999999999999999999999999999</consumeCode><payfee>1000</payfee><payType>4</payType><hRet>0</hRet><status>00000</status><signMsg>0468FD4384E7C6FDDAFC76AE4F614D35</signMsg></callbackReq>
     * 
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.unicom.util.*"%>
<%@ page import="com.unicom.commons.*"%>
<%@ page import="com.communicate.HttpClient.*" %>
<%@ page import="org.apache.http.*,org.apache.http.message.BasicNameValuePair" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*,net.eason.engine.util.MD5Util" %>
<%


Map<String, Object> validateResponse = new LinkedHashMap<String, Object>();

//解析http请求体
Map<String, String> params = PayBeanUtils.parse(request.getInputStream());


//cp订单号
//String orderid = params.get("orderid");
//签名 MD5(orderid=XXX&Key=XXX)
/**
String signMsg = params.get("signMsg");
String usercode = params.get("usercode");
String provinceid = params.get("provinceid");
String cityid = params.get("cityid");
**/
String orderid = "00000";

//校验签名是否正确
String sign = String.format("orderid=%s&Key=%s", orderid, App.KEY);
String mySign = MD5.MD5Encode(sign);
	
	// 创建参数队列    
	/**
	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	formparams.add(new BasicNameValuePair("orderno", out_trade_no));
	formparams.add(new BasicNameValuePair("trade_no", trade_no));
	formparams.add(new BasicNameValuePair("amount", total_fee));
	formparams.add(new BasicNameValuePair("alipay_key", AlipayConfig.private_key));
	formparams.add(new BasicNameValuePair("paykey", paykey));
	formparams.add(new BasicNameValuePair("trade_status", trade_status ));
	formparams.add(new BasicNameValuePair("succ", succ));
	formparams.add(new BasicNameValuePair("code", MD5Util.md5Hex(out_trade_no + trade_no+total_fee+AlipayConfig.private_key+paykey+trade_status+succ).toUpperCase())); //
	
	HttpClient.postForm(formparams,form_url);
	
	**/
	
	/**************************************日志***********************************************/

	/**
	String path = "";
	Properties prop = System.getProperties();
	String os = prop.getProperty("os.name");
	if (os.startsWith("win") || os.startsWith("Win"))
		path = request.getRealPath("/log");
	else
		path = "/ngbs/local/apache-tomcat-6.0.44/log";
	File fp = new File(path, "result.txt");

	System.out.println("================================path:" + path);
	FileWriter fwriter = new FileWriter(fp, true);
	BufferedWriter bfwriter = new BufferedWriter(fwriter);
	request.setCharacterEncoding("UTF-8");
	bfwriter.newLine();
	bfwriter.write("=========================================================");
	bfwriter.newLine();
	bfwriter.write("trade_status:" + trade_status+";orderno=" + out_trade_no + ";amount=" + total_fee + ";succ=" + succ);
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

	**/
	/***********************************************************************************************/

	
%>
