<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
	<meta name="description" content="�ڱ�֧��" />
  	<meta name="keywords" content="�ڱ�֧��" />
	<meta name="author" content="reapal" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="apple-mobile-web-app-title" content="�ڱ�֧��"/>
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta content="telephone=no" name="format-detection" />
	<title>�ڱ�֧��</title>
	<link rel="apple-touch-icon-precomposed" href="/apple-icon-114.png" />
  	<link rel="apple-touch-startup-image" href="/apple-startup.png" />	
	<link type="text/css" rel="stylesheet" href="../common/css/html5/base.css" media="all" />
	<link type="text/css" rel="stylesheet" href="../common/css/html5/common.css" media="all" />
	<script type="text/javascript" src="../common/js/html5/jquery.min.js"></script>
	<script type="text/javascript" src="../common/js/html5/base.js"></script>
	<script type="text/javascript" src="../common/js/html5/cashier.js"></script>
	
	<script language=JavaScript>
		function CheckForm()
		{
			if (document.rongpayment.rongorder.value.length == 0) {
				alert("��Ʒ���Ʋ�����Ϊ�գ���������Ʒ����.");
				document.rongpayment.rongorder.focus();
				return false;
			}
			if (document.rongpayment.rongmoney.value.length == 0) {
				alert("�����븶����.");
				document.rongpayment.rongmoney.focus();
				return false;
			}
			var reg	= new RegExp(/^\d*\.?\d{0,2}$/);
			if (! reg.test(document.rongpayment.rongmoney.value))
			{
		        alert("����ȷ���븶����");
				document.rongpayment.rongmoney.focus();
				return false;
			}
			if (Number(document.rongpayment.rongmoney.value) < 0.01) {
				alert("����������С��0.01.");
				document.rongpayment.rongmoney.focus();
				return false;
			}
		} 
	</script>
</head>
<body>
<div class="w">
	  <header id="top" class="headerred">
	    <h2>�ٳ��̳�</h2>
	    <a class="back" onclick="window.history.back()">����</a>
	  </header>
	
  	  <section class="cashier c">
	    <form name=rongpayment onSubmit="CheckForm();" action=rongpaytoH5.jsp method=post target="_blank">
	        <p class="t">
	            <label for="cardNo">��Ʒ����</label>
	            <input type="text" class="input" name=title id="subject"  />
	        </p>
	        
	        <p class="t">
	            <label for="cardNo">��Ʒ����</label>
	            <input type="text" class="input" name="body" id="body" />
	        </p>
	        
	        <p class="t">
	            <label for="cardNo">������</label>
	            <input type="text" class="input" name="total_fee" id="Rongmoney" />
	        </p>
	        <input type="submit" class="button_p2p" value="ȷ�ϸ���"/>
	    </form>
 	   </section>
	<footer>
		<div class="copyright">Copyright &copy; 2015 �ڱ�֧��</div>	
	</footer>
</div>
</body>
</html>

