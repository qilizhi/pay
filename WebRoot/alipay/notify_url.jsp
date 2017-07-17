<%
/* *
 功能：支付宝服务器异步通知页面
 版本：3.3
 日期：2012-08-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.communicate.HttpClient.*" %>
<%@ page import="org.apache.http.*,org.apache.http.message.BasicNameValuePair" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.entity.obj.*,net.eason.engine.util.MD5Util" %>
<%

System.out.println("//**************************   notify_url  run start  **************************//");

	//获取支付宝POST过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//商户订单号	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

	System.out.println("   ******************  notify_url   ReceiveMessage   start   ******************   ");
	
	//支付宝交易号	
	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//交易状态
	String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
	
	
	System.out.println("trade_status:" + trade_status);
	
	
	// 商户网站唯一订单号
	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
	//交易金额
	String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"), "UTF-8");
	
	System.out.println("out_trade_no:" + out_trade_no);
	System.out.println("total_fee:" + total_fee);
	
	
	System.out.println("total_fee:----------1" );
	//读取商户用户信息
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
	System.out.println("total_fee:----------2" );
	//AlipayConfig.partner = pro.getProperty("alipay_code");
	//AlipayConfig.private_key = pro.getProperty("alipay_key");
	//String form_url = pro.getProperty("alipay_result_url");
	
		HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute("keyHashMap");
		PayObj obj = new PayObj();
		obj = map.get(out_trade_no);
	
	System.out.println("total_fee:----------3" );
	AlipayConfig.partner = obj.mercode ;
	AlipayConfig.private_key = obj.transferkey ;
	String form_url = obj.loopbackaddress;
	
	System.out.println("total_fee:----------4" );

	String paykey = pro.getProperty("paykey");
	
	
	System.out.println("===========       obj.mercode:"+obj.mercode);
	System.out.println("===========       obj.transferkey:"+obj.transferkey);
	System.out.println("===========       obj.loopbackaddress:"+obj.loopbackaddress);
	System.out.println("===========       AlipayConfig.partner:"+AlipayConfig.partner);
	System.out.println("===========       AlipayConfig.private_key:"+AlipayConfig.private_key);
	String succ = "1" ;   

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
	System.out.println("   ********************   notify_url   verify   start  ********************   ");
	
	if(AlipayNotify.verify(params)){//验证成功
		System.out.println("succ: 验证成功");
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码

		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
		
		if(trade_status.equals("TRADE_FINISHED")){
			//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
			//注意：
			//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
			 succ = "0";
		} else if (trade_status.equals("TRADE_SUCCESS")){
			//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
			//注意：
			//付款完成后，支付宝系统发送该交易状态通知
			//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
			
			  succ = "0";
			
				// 创建参数队列    
				/**
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				formparams.add(new BasicNameValuePair("orderno", out_trade_no));
				formparams.add(new BasicNameValuePair("amount", total_fee));
				formparams.add(new BasicNameValuePair("alipay_key", AlipayConfig.private_key));
				formparams.add(new BasicNameValuePair("succ", succ));

				HttpClient.postForm(formparams,form_url);
				**/
				
				
		} else if (trade_status.equals("TRADE_CLOSED")){
			System.out.println("CLOSED: 交易关闭");
			succ = "2";
		}

		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
		out.println("success");	//请不要修改或删除

		//////////////////////////////////////////////////////////////////////////////////////////
	}else{//验证失败
		out.println("fail");
		System.out.println("succ: 验证失败");
	}
	
	
	// 创建参数队列    
	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	formparams.add(new BasicNameValuePair("orderno", out_trade_no));
	formparams.add(new BasicNameValuePair("trade_no", trade_no));
	formparams.add(new BasicNameValuePair("amount", total_fee));
	formparams.add(new BasicNameValuePair("alipay_key", AlipayConfig.private_key));
	formparams.add(new BasicNameValuePair("paykey", paykey));
	formparams.add(new BasicNameValuePair("trade_status", trade_status ));
	//formparams.add(new BasicNameValuePair("favorable", favorable));
	formparams.add(new BasicNameValuePair("succ", succ));
	formparams.add(new BasicNameValuePair("code", MD5Util.md5Hex(out_trade_no + trade_no+total_fee+AlipayConfig.private_key+paykey+trade_status+succ).toUpperCase())); //
	
	HttpClient.postForm(formparams,form_url);
	
	
	System.out.println("//**************************   notify_url  run end  **************************//");
	/**************************************日志***********************************************/

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

	/***********************************************************************************************/

	
%>
