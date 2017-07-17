<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
System.out.println("weixin 回调页面");
System.out.println("//**************************   weixin 回调页面l  run start  **************************//");
request.setCharacterEncoding("UTF-8");//传值编码
response.setContentType("text/html;charset=UTF-8");//设置传输编码
//这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
InputStream is =  request.getInputStream();
Document document = builder.parse(is);
//获取到document里面的全部结点
NodeList allNodes = document.getFirstChild().getChildNodes();
Node node;
Map<String, Object> map = new HashMap<String, Object>();
int i=0;
while (i < allNodes.getLength()) {
    node = allNodes.item(i);
    if(node instanceof Element){
        map.put(node.getNodeName(),node.getTextContent());
    }
    i++;
}
/* {"is_subscribe":"Y",
	"appid":"wxee3e6d3312bc7802",
	"fee_type":"CNY",
	"nonce_str":"yupneyg6gt1gk7kj",
	"out_trade_no":"T201705112448",
	"device_info":"WEB",
	"transaction_id":"4000492001201705120593273439",
	"trade_type":"NATIVE",
	"sign":"0E2CBD29BF93109D055FADBFEEB0FAA3",
	"result_code":"SUCCESS",
	"mch_id":"1467128202",
	"total_fee":"1",
	"attach":"attach",
	"time_end":"20170512211929",
	"openid":"oL506wXiUTbNs1UEOTyUQfw-5DF4",
	"return_code":"SUCCESS",
	"bank_type":"GRCB_CREDIT",
	"cash_fee":"1"} */

System.out.println("weixin 扫码支付返加的数据map:["+JSONObject.fromObject(map).toString()+"]");

%>
