package com.tencent;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.util.PropertiesUtil;

public class PayInfo implements Serializable{
	 private String appid;
	 private String mch_id;
	 private String device_info;
	 private String nonce_str;
	 private String sign;
	 private String body;
	 private String attach;
	 private String out_trade_no;
	 private int total_fee;
	 private String spbill_create_ip;
	 private String notify_url;
	 private String trade_type;
	 //private String openid;
	 private String product_id;
	 /**
	  * 创建统一下单的xml的java对象
	  * @param productID 商品ID
	  * @param spbill_create_ip 用户的ip地址
	  * @param openId 用户的openId
	  * @param outTradeNo 商户订单号
	  * @param attch 附加数据
	  * @param  totalFee 标价金额 分为单位
	  * @param body 商品描述
	  * @return
	  */
	  public static PayInfo createPayInfo(String spbill_create_ip,String productID,String body,String outTradeNo,String attch,Integer totalFee) {
	   PayInfo payInfo = new PayInfo();
	   payInfo.setAppid(Configure.getAppid());
	   payInfo.setDevice_info("WEB");
	   payInfo.setMch_id(Configure.getMchid());
	   payInfo.setNonce_str(RandomStringGenerator.getRandomStringByLength(16));
	   payInfo.setBody(body);
	   payInfo.setAttach(attch);
	   //payInfo.setOut_trade_no("SDZ".concat("A").concat(DateFormatUtils.format(new Date(), "MMddHHmmss")));
	   payInfo.setOut_trade_no(outTradeNo);
	   payInfo.setTotal_fee(totalFee);
	   payInfo.setSpbill_create_ip(spbill_create_ip);
	   payInfo.setNotify_url(Configure.notify_url);
	   payInfo.setTrade_type("NATIVE");
	 //  payInfo.setOpenid(openId);
	   payInfo.setProduct_id(productID);
	   return payInfo;
	  }
	 //下面是get,set方法 .
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	/*public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}*/
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	 
	 }