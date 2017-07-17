<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="./js/jquery-1.8.0.js"></script>
	<script type="text/javascript" src="./js/jquery.qrcode.js"></script>
	<script type="text/javascript" src="./js/utf.js"></script>
	<script>

function sQrcode(qdata){

	$("#showqrcode").empty().qrcode({		// 调用qQcode生成二维码
			render : "canvas",    			// 设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
			text : qdata,    				// 扫描了二维码后的内容显示,在这里也可以直接填一个网址或支付链接
			width : "200",              	// 二维码的宽度
			height : "200",             	// 二维码的高度
			background : "#ffffff",     	// 二维码的后景色
			foreground : "#000000",     	// 二维码的前景色
			src: ""    						// 二维码中间的图片
		});	
		
}	
	
$(document).ready(function(){

	jQuery( function($){
        var url = "http://chaxun.1616.net/s.php?type=ip&output=json&callback=?&_=" + Math.random();
        $.getJSON(url, function(data){
            document.getElementById("client_ip").value = data.Ip;  // 获取客户端IP
        });
	});

	$("#submit").click(function(){
		
 		var formParam = $("#ScanPay").serialize();		// 序列化表单内容为字符串  
		alert("提交的表单参数数据：" + formParam);

		$.ajax({  										// jQuery Ajax 异步提交数据
			type:"post",      							// 数据提交方式
         	url:"/Pay/zhifuPay.action",  						// 提交的url地址  						
         	data:formParam,   							// 提交的数据
         	dataType:"text",  							// 返回的数据类型
         	success:function(data,textStatus){			// 回调成功
         	
         	 			$("#xmldata").text(data);        	 		
         	 			var resp_code = $(data).find("resp_code").text();
         	 			var result_code = $(data).find("result_code").text();
         	 			if( resp_code == "SUCCESS" && result_code == "0" ){         	 		
         	 				var qrcode = $(data).find("qrcode").text();
         	 				sQrcode(qrcode);
         	 			}else if ( resp_code == "SUCCESS" && result_code == "1"  ){
         	 				$("#showqrcode").text("订单已存在!");
         	 				document.getElementById("showqrcode").style.color="red";
							document.getElementById("showqrcode").style.fontSize="200%";
         	 			}  
         	 			      	 		
     				},
         	error:function(){       					// 回调失败
         			    $("#xmldata").text("程序异常，XML数据返回失败!");         			
         			}
    	});      		
	});
});
	
	</script>
  </head>
  
  <body>
	<table>
		<tr>
  			<td>
  				<form id="ScanPay">
					<!-- <div>商 家 号(merchant_code)：<input Type="text" Name="merchant_code" id="merchant_code" value="1111110166"> * </div> -->
					<div>商 家 号(merchant_code)：<input Type="text" Name="merchant_code" id="merchant_code" value="2060226623"> * </div>
					<div>业务类型(service_type)：
						<select name="service_type" id="service_type">
							<option value="weixin_scan">weixin_scan</option>
							<option value="alipay_scan">alipay_scan</option>
							<option value="zhb_scan">zhb_scan</option>
						</select> *	</div>												
					<div>服务器异步通知地址(notify_url)：<input Type="text" Name="notify_url" id="notify_url" value="http://148.66.1.171:8085/Pay/zhifu/Notify_Url.jsp"> * </div>				
					<div>接口版本(interface_version)：<input Type="text" Name="interface_version" id="interface_version" value="V3.1"/> * </div>	
					<div>客户端IP(client_ip)：<input Type="text" Name="client_ip" id="client_ip"/> * </div>																	
					<div>密钥(merchant_private_key)：<input Type="text" Name="merchant_private_key"  value="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALpJStZUgs8SrJbP2Zo8sqVKp3kJd4pypRixQwvEfQL0fB6ICCmvG7x5NMp19u1mQC32NZZao4bNL+Nixj7aZHH7BycAtrDQeNZzByVMf/fr6lMrfHpj3f63SB5mT8zPhXjLJm5RqaVjM3C6E6vwVkw9ZJkZTE7m3B+saCceUaHFAgMBAAECgYAOnPM+7vA3+DoLtpBWVg6ZgsqyZvi2ppmm3zjoMmiwE6es6XTieDcPyN5IR4qwQsYkFN1NRxzu1Se9iaENiyo32FKXhyG0gUsOAjRRh2eNTyJ0yV8lNl8CnL4zwq6wKK2YT0y3Fm/Wik9+EbvQMyc/N444D970dfLIW0fI6l63fQJBAPB207Z6x1NtLabTasedAJUFg63k2wbNi+bQ4biX8Hq7sMzlWRDdIKKGsMGy9cg11Yogv7WFF56UxKZTOaLlEj8CQQDGUmMKCSNzaDbzn7+2LiwJ+JVWdMlEDkIgWO+mebk0+yEiAZ+6QSQw9m2/BNd4xhDgtPRTJUN8s5iq8s7M28L7AkEA8Cq+aZNXYcu5vNWL4LK+0hgf3J6m47SH9pDJ4URfuzWa8AphWqaA4dME13GWadDa5oqu7u1vGcQcZMLa3gdMoQJBAIlNrs3a59VVuH3RAsyhiNHabaHutw1MBdIZYMnUCjq5BwZhM3Gi+eiAJBkUxA0Uq8i231TVPrUOLktkUXKZT5MCQHPQ/ykkAF1YHZ/2Ja+SmarhXyJTfvEMlVUBEOYMMcW35+Ttvof8mc264ix87ip5YaKXiMmWoAULhdO3aPnQfv0="/> * </div>																	
					<div>签名方式(sign_type)：
						<select name="sign_type" id="sign_type">
							<option value="RSA-S">RSA-S</option>
							<option value="RSA">RSA</option>
						</select> *	</div>																	
					<div>商户订单号(order_no)：<input Type="text" Name="order_no" id="order_no" value="<%=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())%>"> * </div>
					<div>商户订单时间(order_time)：<input Type="text" Name="order_time" id="order_time" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"/>* </div>		
					<div>商户订单金额(order_amount)：<input Type="text" Name="order_amount" id="order_amount" value="0.01"> * </div>		
					<div>商品名称(product_name)：<input Type="text" Name="product_name" id="product_name" value="iPhone"> * 	</div>			
				</form>
				<button id="submit">提交支付参数</button> 			
  			</td>
  			<td><div id="showqrcode"><img src=""></div></td>
		</tr>
		<tr>
			<td colspan="2">
				<div>返回的XML数据：</div>
				<textarea rows="12" cols="90" id="xmldata"></textarea>
			</td>
		</tr>
	</table>
  </body>
</html>
