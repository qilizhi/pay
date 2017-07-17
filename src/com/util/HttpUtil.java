package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.heepay.Common.Md5Tools;

/**
 * 
 * <p>
 * <b>HttpUtil</b> 是 Http 工具类
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @since 2016年5月4日
 */
public class HttpUtil {
	/**
	 * 
	 * 发送HTTP请求，以POST方式
	 * 
	 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
	 * @date 2016年5月4日 下午8:01:22
	 * @param sendData
	 *            要发送的数据，以UTF-8编码的字节数组
	 * @param url
	 *            将数据POST到的目的地址
	 * @return POST后目的地址返回的数据
	 */
	public static String post(byte[] sendData, String url) {
		String res = "";

		CloseableHttpClient hc = null;

		try {
			hc = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(url);
			HttpEntity entity = new ByteArrayEntity(sendData,
					ContentType.DEFAULT_BINARY);
			httpPost.setEntity(entity);

			HttpResponse hr = hc.execute(httpPost);

			if (hr.getStatusLine().getStatusCode() != 200) {
				res = "请求失败";
			} else {
				HttpEntity entity2 = hr.getEntity();
				res = EntityUtils.toString(entity2, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		} finally {
			try {
				hc.close();
			} catch (IOException e) {
				e.printStackTrace();
				res = e.getMessage();
			}
		}

		return res;
	}

	/**
	 * 类表单方式发送POST请求
	 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
	 * @date 2016年5月5日 上午9:17:56
	 * @param nameValuePairs 类表格参数
	 * @param url 将数据POST到的目的地址
	 * @return
	 */
	public static String post(List<NameValuePair> nameValuePairs, String url) {
		String res = "";

		CloseableHttpClient hc = null;

		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(
					nameValuePairs, "UTF-8");
			hc = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(uefEntity);

			HttpResponse hr = hc.execute(httpPost);

			if (hr.getStatusLine().getStatusCode() != 200) {
				res = "请求失败";
			} else {
				HttpEntity entity2 = hr.getEntity();
				res = EntityUtils.toString(entity2, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = e.getMessage();
		} finally {
			try {
				hc.close();
			} catch (IOException e) {
				e.printStackTrace();
				res = e.getMessage();
			}
		}

		return res;
	}

	public static void main(String[] args) {

//		JSONObject json1 = new JSONObject();
//		json1.put("loginname", "wentian.he");
//		json1.put("time", System.currentTimeMillis());
//		json1.put("msg", "xueyi.zhao");
//		json1.put("section", "test send data");
//
//		JSONArray ja = new JSONArray();
//		ja.add(json1);
//
//		JSONObject json = new JSONObject();
//		json.put("data", ja);
//
//		byte[] requestData = CommonMethod.getByteUTF8(json);
//
//		post(requestData,
//				"http://192.168.0.90:8080/cash/cashApi/saveErrorInfo.action");
		
		
		//////////////////////////////////////////////////////////////
		
		String result = "1";
		String agent_id = "2060516";
		String jnet_bill_no = "3";
		String agent_bill_id = "160505161832003";
		String pay_type = "22";
		String pay_amt = "100";
		String remark = "160505141031006";
		String sign = "129A4DD897964647ACCB801D";
		
		StringBuffer str = new StringBuffer();
		str.append("result=").append(result).append("&");
		str.append("agent_id=").append(agent_id).append("&");
		str.append("jnet_bill_no=").append(jnet_bill_no).append("&");
		str.append("agent_bill_id=").append(agent_bill_id).append("&");
		str.append("pay_type=").append(pay_type).append("&");
		str.append("pay_amt=").append(pay_amt).append("&");
		str.append("remark=").append(remark).append("&");
		str.append("key=").append(sign);


		String signstr = Md5Tools.MD5(str.toString()).toLowerCase();
		
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
		 nameValuePairs.add(new BasicNameValuePair("result", result));
		 nameValuePairs.add(new BasicNameValuePair("agent_id", agent_id));
		 nameValuePairs.add(new BasicNameValuePair("jnet_bill_no", jnet_bill_no));
		 nameValuePairs.add(new BasicNameValuePair("agent_bill_id", agent_bill_id));
		 nameValuePairs.add(new BasicNameValuePair("pay_type", pay_type));
		 nameValuePairs.add(new BasicNameValuePair("pay_amt", pay_amt));
		 nameValuePairs.add(new BasicNameValuePair("remark", remark));
		 nameValuePairs.add(new BasicNameValuePair("sign", signstr));
	        
	        
		post(nameValuePairs,
				"http://192.168.0.90:8086/Pay/heepayNotifyUrl.action");
	}
}