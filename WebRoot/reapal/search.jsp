<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.reapal.config.*"%>
<%@ page import="com.reapal.utils.*"%>
<%@page import="java.text.SimpleDateFormat"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>融宝快捷支付结果查询接口</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	    <style type="text/css">
<!--
.STYLE1 {
	font-size: 10px;
	font-weight: bold;
}
.STYLE3 {font-size: 16px}
.STYLE4 {
	font-size: large;
	font-weight: bold;
}
.form-star {
	PADDING-RIGHT: 0px; PADDING-LEFT: 3px; FONT-SIZE: 12px; PADDING-BOTTOM: 3px; VERTICAL-ALIGN: top; WIDTH: 1%; COLOR: #ff0000; PADDING-TOP: 12px
}
-->
    </style>
  </head>
  <body>

  	 <div align="center">				
			   <table width="50%" border="1" cellpadding="0" cellspacing="0" bordercolor="#000000" style="padding: 5px; margin-top: 50px;" >
					<tr>
						<td height="40" align="left"> <div align="center"><img src="picture/reapal.png" /></div></td>
					</tr>
					<tr>
						<td height="40" align="left"><div align="center" class="STYLE4">融宝快捷支付结果查询接口</div></td>
					</tr>
					<tr>
						<td height="40"> 
						   <div style=" margin-left:100px">
						   	<form action="cannelCardResult.jsp" method="post" >		        
                                     <table width="100%" border="0" cellspacing="0" cellpadding="0">
				                    <TR>                      
				                      <TD height="40" align="right">商户号:</TD>
				                      <TD class=form-star>* </TD>
				                      <TD style="padding:5px;"><input type="text" id="merchant_id" name = "merchant_id" value="<%=ReapalH5Config.merchant_id%>"/></TD>
				                    </TR>
				                     <TR>                  
				                      <TD height="40" align="right">商户订单号:</TD>
				                      <TD class=form-star>* </TD>
				                      <TD style="padding:5px;"><input type="text" id="order_no" name = "order_no"  /></TD>
				                    </TR>           
				                     <TR>                      
				                      <TD height="40" align="right"></TD>
				                      <TD class=form-star></TD>
				                      <TD style="padding:5px;"><input  name="submit" type="submit" value="   提交  " /></TD>
				                    </TR>
				                    </table>     
						   	   </form>
                           </div>                      </td>
					</tr>
					<tr>
						<td height="40" align="left"><div align="center" class="STYLE1">融宝支付<span class="STYLE3">.</span>版权所有</div></td>
					</tr>					
                 </table>			
  </div>    	
    
  </body>
</html>
