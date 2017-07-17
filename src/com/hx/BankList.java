package com.hx;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;

import net.eason.engine.http.Const;
import net.eason.engine.http.HttpManager;
import net.eason.engine.http.UrlDO;
import net.eason.engine.http.HttpDO.HttpResult;

public class BankList {
	
	/**
	 * 获取商户银行卡列表
	 * @return
	 */
	public static String GetShopsBankList() {
		
		 String Mer_code="000166";
		 String Mer_key="cQ0YAtyVNoiEeKrZJ5F5Qp2pMohwspfv6XoiU3wHYtcc1YOEhJ3SjDVMylmwmbD7jMhg5ifqjX67mzNN02p8MmlSb1KIqa5XF4TEQHjZEmJmhyAYxyVJMeLWBofdxwaj";
		
		String url= "http://webservice.ips.net.cn/web/Service.asmx/GetBankList"; 
		
		cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
		String SignMD5 = b.toMD5( Mer_code+Mer_key).toLowerCase();
		
//		String ip = SystemUtil.getGameServerIP();
		String urlString = url+"?MerCode="+Mer_code+"&SignMD5="+SignMD5;
		int timeout = 1000 * 5;
		
		return readGetAsString(urlString, "UTF-8", timeout);
	}
	
	public static String readGetAsString(String address, String charset, int timeout) {
		try {
			UrlDO urlDO = new UrlDO(address, Const.REQUESTTYPE_GET, null, timeout * 1000, 20l);
			HttpManager httpManager = HttpManager.getInstance();
			httpManager.addUrl(urlDO);
			HttpResult result = urlDO.getHttpResult();
			if (result != null)
				return result.getResultBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		String rs =GetShopsBankList();
		
		String enc = "UTF-8";
		try {
			String keyWord =	java.net.URLDecoder.decode(rs, enc);
			System.out.println(keyWord);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}		
	
	HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,HttpStatus.SC_OK, "OK");
//	 response.addHeader("Set-Cookie","c1=a; path=/; domain=localhost");
//	 response.addHeader("Set-Cookie","c2=b; path=\"/\", c3=c; domain=\"localhost\"");
//	 Header h1 = response.getFirstHeader("Set-Cookie");
//	 System.out.println(h1);
//	 Header h2 = response.getLastHeader("Set-Cookie");
//	 System.out.println(h2);
//	 Header[] hs = response.getHeaders("Set-Cookie");
//	 System.out.println(hs.length);
}
