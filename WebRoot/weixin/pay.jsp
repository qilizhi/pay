<%@page import="java.io.OutputStream"%>
<%@page import="com.tencent.common.ZxingUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.tencent.common.XMLParser"%>
<%@page import="com.tencent.common.Configure"%>
<%@page import="com.tencent.common.HttpsRequest"%>
<%@page import="com.tencent.common.Signature"%>
<%@page import="com.tencent.PayInfo"%>
<%
/**************************************交易下单***********************************************/
	//获取请求参数
	request.setCharacterEncoding("UTF-8");//传值编码
	response.setContentType("text/html;charset=UTF-8");//设置传输编码
	String spbill_create_ip=request.getParameter("spbill_create_ip");//机器ip 是否必传：Y
	String attach=request.getParameter("attach");//附加数据  是否必传：N
	String body=request.getParameter("body");//商品描述  是否必传：Y
	String product_id=request.getParameter("product_id");//商品ID 是否必传：Y
	String out_trade_no=request.getParameter("out_trade_no");//商户订单号 是否必传：Y
	String total_fee=request.getParameter("total_fee");//标价金额 是否必传：Y
	System.out.println("pay.jsp request params["+
	"spbill_create_ip:"+spbill_create_ip+"|"+
	"attach:"+attach+"|"+
	"body:"+body+"|"+
	"product_id:"+product_id+"|"+
	"out_trade_no:"+out_trade_no+"|"+
	"total_fee:"+total_fee+"|"+
	"]");
	System.out.println("=============参数处理================");
	PayInfo pay=PayInfo.createPayInfo(spbill_create_ip,product_id,body,out_trade_no,attach,total_fee);
	String sign=Signature.getSign(pay);
	pay.setSign(sign);
	HttpsRequest http=new HttpsRequest();
	System.out.println("=============参数处理================");
	System.out.println("=============开始下单================");
	String result=http.sendPost(Configure.UNIFIEDORDER_API,pay);
	System.out.println("==============返回xml结果============");
	System.out.println(result);
	Map<String,Object>map=XMLParser.getMapFromXML(result);
	String QRString="";
	String jsonr=JSONObject.fromObject(map).toString();
	System.out.println("==============转换xml成JSON结果============");
	System.out.println(jsonr);
	if("SUCCESS".equalsIgnoreCase(map.get("result_code").toString())){
		QRString=map.get("code_url").toString();
	}
	 
%>
<% 
response.getWriter().write(jsonr);
/*out.clear();
 response.setContentType("image/jpeg");
//response.addHeader("pragma","NO-cache");
response.addHeader("Cache-Control","no-cache");
response.addDateHeader("Expries",0);
ServletOutputStream output=response.getOutputStream();
ZxingUtil.createQRCodeToOutStream(QRString,output);
//output.flush();
out.clear();  
out = pageContext.pushBody();   */
%>
