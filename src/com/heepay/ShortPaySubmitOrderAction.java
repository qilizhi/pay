package com.heepay;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.entity.obj.PayObj;
import com.heepay.Common.AES;
import com.heepay.Common.DataHelper;
import com.heepay.Common.ErrorUtil;
import com.heepay.Common.Md5Tools;
import com.heepay.HeepayReturn.HeepayXMLReturn;
import com.util.LocalCache;
import com.util.PropertiesUtil;

public class ShortPaySubmitOrderAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ShortPaySubmitOrderAction.class);
	
	private String agent_id;
	private String version;
	private String user_identity;
	private String hy_auth_uid;
	private String device_type;
	private String device_id;
	private String custom_page;
	private String display;
	private String return_url;
	private String notify_url;
	private String agent_bill_id;
	private String agent_bill_time;
	private String pay_amt;
	private String goods_name;
	private String goods_note;
	private String goods_num;
	private String user_ip;
	private String ext_param1;
	private String ext_param2;
	private String auth_card_type;
	private String sign_key;
	private String data_key;
	private String url;
	private String timestamp;

	public void doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		agent_id = request.getParameter("agent_id"); //给商户分配的唯一标识
		version = "1"; //版本号，固定为 1
		user_identity = request.getParameter("user_identity"); //商户用户唯一标识
		hy_auth_uid = request.getParameter("hy_auth_uid"); //用户快捷支付的授权码
		device_type = request.getParameter("device_type"); //设备类型： 0=Wap 1=Web 2=AndroidApp
		device_id = request.getParameter("device_id"); //设备唯一标识
		custom_page = "0"; //终端显示样式：0= Wap（默认）
		display = "0"; //自定义支付页面：0=否，支付页面使用汇付宝提供（默认）
		return_url = request.getParameter("return_url"); //支付页面返回地址：custom_page = 0：表示支付完成后的返回地址
		notify_url = request.getParameter("notify_url"); //异步通知地址
		agent_bill_id = request.getParameter("agent_bill_id"); //商户订单号，必须唯一
		agent_bill_time = request.getParameter("agent_bill_time"); //商户订单时间，格式为“yyyyMMddhhmmss”,例 20130910170601
		pay_amt = request.getParameter("pay_amt"); //支付金额，单位：元，保留二位小数
		goods_name = request.getParameter("goods_name"); //商品名称
		goods_note = request.getParameter("goods_note"); //商品描述
		goods_num = request.getParameter("goods_num"); //商品数量
		user_ip = request.getParameter("user_ip"); //用户IP地址
		ext_param1 = request.getParameter("ext_param1"); //商户保留，支付完成后的通知会将该参数返回给商户
		ext_param2 = request.getParameter("ext_param2"); //商户保留，支付完成后的通知会将该参数返回给商户
		auth_card_type = "0";
		sign_key = request.getParameter("sign_key");
		data_key = request.getParameter("data_key");
		url = PropertiesUtil.getInstance().getValue("heepay_submitOrder_url");
		Calendar calTime=Calendar.getInstance();
		timestamp = String.valueOf(calTime.getTimeInMillis()); //时间戳
		
		//String loopbackaddress = request.getParameter("loopbackaddress");
		PayObj payObj = new PayObj();
		payObj.loopbackaddress = PropertiesUtil.getInstance().getValue("heepay_callback_url");
		payObj.transferkey = data_key;
		
		LocalCache.put(agent_id, payObj);
		
		logger.info("agent_id=" + agent_id + "&version=" + version + "&user_identity=" + user_identity
				 + "&hy_auth_uid=" + hy_auth_uid + "&device_type=" + device_type + "&device_id=" + device_id
				 + "&custom_page=" + custom_page + "&display=" + display + "&return_url=" + return_url
				 + "&notify_url=" + notify_url + "&agent_bill_id=" + agent_bill_id + "&agent_bill_time=" + agent_bill_time
				 + "&pay_amt=" + pay_amt + "&goods_name=" + goods_name + "&goods_note=" + goods_note
				 + "&goods_num=" + goods_num + "&user_ip=" + user_ip + "&ext_param1=" + ext_param1
				 + "&ext_param2=" + ext_param2 + "&auth_card_type=" + auth_card_type + "&sign_key=" + sign_key
				 + "&data_key=" + data_key + "&timestamp=" + timestamp + "&url=" + url);
		
		String postData = GetSubmitOrderRequestData(); // 组装get请求URL
		String payheepReturnxml = DataHelper.RequestPostUrl(url, postData, DataHelper.UTF8Encode); // 根据提交订单的请求URL返回的XML数据
		logger.info(payheepReturnxml);
		HeepayXMLReturn payheepReturn = DataHelper.GetRetuenXmlContent(payheepReturnxml);

		boolean isSucess = payheepReturn.get_is_success();// 是否成功.
		if (!isSucess) {
			writeBack("ret_code=" + payheepReturn.get_retcode() + "&ret_msg=" + payheepReturn.get_retmsg());
			return;
		}

		String encrypt_data = payheepReturn.get_encryptdata();// 为返回XML的加密的DATA
		String payheepReturnData = AES.Decrypt(encrypt_data, data_key);
		Map<String,String> mapValues = DataHelper.URLRequestParams(payheepReturnData);
		String sign = mapValues.get("sign");
		String requestEncryptData = mapValues.get("encrypt_data");
		Map<String, String> mapResultData = new HashMap<String, String>();
        mapResultData.put("agent_id", agent_id); //参考文档 汇付宝分配给商户的ID
        mapResultData.put("encrypt_data", requestEncryptData);
        mapResultData.put("sign", sign);
        String params = DataHelper.GetSortQueryString(mapResultData);
		writeBack(payheepReturnData + "&params=" + params.replaceAll("&", "%26").replaceAll("=", "%3D"));
	}
	
	private String GetSubmitOrderRequestData() throws Exception {
		// 对应的值请参考文档 DEMO中的数据为参考
		Map<String, String> mapData = new HashMap<String, String>();
		mapData.put("agent_id", agent_id);
		mapData.put("version", "1");
		mapData.put("user_identity", user_identity);
		mapData.put("hy_auth_uid", hy_auth_uid);
		mapData.put("device_type", device_type);
		mapData.put("device_id", device_id);
		mapData.put("custom_page", "0");
		mapData.put("display", "0");
		mapData.put("return_url", return_url);
		mapData.put("notify_url", notify_url);
		mapData.put("agent_bill_id", agent_bill_id);
		mapData.put("agent_bill_time", agent_bill_time);
		mapData.put("pay_amt", pay_amt);
		mapData.put("goods_name", goods_name);
		mapData.put("goods_note", goods_note);
		mapData.put("goods_num", goods_num);
		mapData.put("user_ip", user_ip);
		mapData.put("ext_param1", ext_param1);
		mapData.put("ext_param2", ext_param2);
		mapData.put("auth_card_type", "0");
		mapData.put("timestamp", timestamp);
		mapData.put("key", sign_key);// 签名需要加入KEY
		String getSignData = DataHelper.GetSortQueryToLowerString(mapData);// 生成SIGN 需要将商户签名KEY加入后MD5
		String sign = Md5Tools.MD5(getSignData).toLowerCase();
		// 平台之间的转码
		DataHelper.TranferCharsetEncode(mapData);
		// 生成 encrypt_data
		String[] filterKeys = { "agent_id", "key" };
		String getEncryptData = DataHelper.GetSortFilterQueryString(mapData, filterKeys);
		// AES加密后 的URLENCODE字符串
		String encrypt_data = URLEncoder.encode(AES.Encrypt(getEncryptData, data_key), DataHelper.UTF8Encode);

		Map<String, String> mapResultData = new HashMap<String, String>();
		mapResultData.put("agent_id", agent_id); // 参考文档 汇付宝分配给商户的ID
		mapResultData.put("encrypt_data", encrypt_data);
		mapResultData.put("sign", sign);
		return DataHelper.GetSortQueryString(mapResultData);
	}
}
