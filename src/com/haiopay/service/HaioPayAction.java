package com.haiopay.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.haiopay.common.MD5Util;
import com.haiopay.common.utils;
import com.heepay.BaseAction;
import com.util.HttpUtil;
import com.util.PropertiesUtil;
import com.util.StringUtils;

import net.sf.json.JSONObject;

public class HaioPayAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(HaioPayAction.class);

	public void dopay() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter out;
		try {
			logger.info("【海鸥支付】请求支付参数, request:{" + JSONObject.fromObject(request.getParameterMap()).toString() + "}");
			String gateway = PropertiesUtil.getInstance().getValue("haio_pay_gateway"); // "提交网关
			String userid =PropertiesUtil.getInstance().getValue("haio_pay_userid"); // "商户ID
			String key = PropertiesUtil.getInstance().getValue("haio_pay_key"); // "商户密钥request.getParameter("key");
			String orderid = request.getParameter("orderid"); // 商户订单号
			String price = request.getParameter("amt"); // 交易金额
			String callback = PropertiesUtil.getInstance().getValue("haio_pay_callback"); // 同步跳转地址
			String payvia = request.getParameter("banktype"); // 交易方式
			String timespan = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()); // 时间戳
			String notify =  PropertiesUtil.getInstance().getValue("haio_pay_notify"); 																									// 不可为空
			String bankType="";
			
			if(("alipay").equals(payvia)){
				bankType ="alipay";
			}
			if("alipaywap".equals(payvia)){
				bankType="alipaywap";
			}
			if(("tenpaywap").equals(payvia)){
				bankType="tenpay";	
			}
			if(("weixin").equals(payvia)){
				bankType="weixin";
			}
			if("wxwap".equals(payvia)){
				bankType="wxwap";
			}
			if(("kuaijie").equals(payvia)){
				bankType="bank";
			}
			if(bankType==""){
				bankType="bank";
			}
																												
			String custom = ""; // 自定义数据 可空
			String format = ""; // 响应方式 1表求获取扫码地址 否为为跳转到平台支付页 当为1时就 模拟发起 POST
			String sign = ""; // 签名数据 Max(32)
			String param = "userid=" + userid + "&orderid=" + orderid + "&price=" + price + "&payvia=" + bankType+"&notify="+notify
					+ "&callback="  + "&key=" + key;
			sign = utils.MD5(utils.MD5(param) + key); // 数据签名

			StringBuffer form = new StringBuffer();
			form.append("<form action='" + gateway + "' method='post' id='frm'>");
			form.append("<input type='hidden' name='userid' value='" + userid + "'/>");
			form.append("<input type='hidden' name='orderid' value='" + orderid + "'/>");
			form.append("<input type='hidden' name='price' value='" + price + "'/>");
			form.append("<input type='hidden' name='callback' value=''/>");
			form.append("<input type='hidden' name='notify' value='"+notify+"'/>");
			form.append("<input type='hidden' name='payvia' value='" + bankType + "'/>");
			form.append("<input type='hidden' name='timespan' value='" + timespan + "'/>");
			form.append("<input type='hidden' name='custom' value='" + custom + "'/>");
			form.append("<input type='hidden' name='format' value='" + format + "'/>");
			form.append("<input type='hidden' name='sign' value='" + sign.toLowerCase() + "'/>");
			form.append("</form>");
			form.append("<script type='text/javascript'>document.getElementById('frm').submit()</script>");
			// Response.Write(form);
			response.setContentType("text/html; charset=utf-8");
			
			out = response.getWriter();
			out.write(form.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

	public void callback() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		System.out.println("海鸥支付 回调页面");
		System.out.println("//**************************   海鸥支付 回调页面 run start  **************************//");
		try {
			request.setCharacterEncoding("UTF-8");// 传值编码
			response.setContentType("text/html;charset=UTF-8");// 设置传输编码
			String reqp="【海鸥支付】海鸥-->>通知-->>中转平台,充值回调结果, params:{"
					+ JSONObject.fromObject(request.getParameterMap()).toString()
					+ "}";
			logger.info(reqp);System.out.println(reqp);
			//获取支付回调返回值
			String userid = PropertiesUtil.getInstance().getValue("haio_pay_userid"); // 商户编号												// Max(8)
			String key = PropertiesUtil.getInstance().getValue("haio_pay_key"); // 商户密钥
			String orderid = request.getParameter("orderid"); // 商户订单号
			String billno = request.getParameter("billno"); // 海鸥平台订单号
			String price = request.getParameter("price"); // 实际支付金额
			String payvia = request.getParameter("payvia"); // 支付类型
			String state = request.getParameter("state"); // 支付状态
			String ext = request.getParameter("ext"); // 平台附加数据
			String custom = request.getParameter("custom"); // 商户自定义数据
			String timespan = request.getParameter("timespan"); // 时间戳
			String sign = request.getParameter("sign"); // 数据签名
			String url = PropertiesUtil.getInstance().getValue("haio_pay_url");
			if(!StringUtils.notEmpty(orderid)){
				logger.error(new Date()+"交易订单号为空");
			}
			/**
			 * 1.判断支付状态，状态有误时 不处理订单。
			 * 2，校验数据签名，如果签名数据正确，则调用查账接口
			 */
			if (!"1".equals(state)) {
				logger.error(orderid+"交易状态有误");
				} else {
						List<NameValuePair> formparams = new ArrayList<NameValuePair>();
						formparams.add(new BasicNameValuePair("userid",userid));
						formparams.add(new BasicNameValuePair("orderid",orderid));
						formparams.add(new BasicNameValuePair("billno",billno));
						formparams.add(new BasicNameValuePair("price",price));
						formparams.add(new BasicNameValuePair("payvia",payvia));
						formparams.add(new BasicNameValuePair("state",state));
						formparams.add(new BasicNameValuePair("timespan",timespan));
						formparams.add(new BasicNameValuePair("key",key));
						formparams.add(new BasicNameValuePair("sign",sign));
						String responseStr = HttpUtil.post(formparams,url);
						logger.info("【海鸥支付】中转平台通知现金平台充值结果, response:{" + responseStr + "}");
							
				}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		writeSuccess();
	}
	public static void main(String[] args) {
		String param = "userid=" + "8880255" + "&orderid=" + "170707152704009" + "&billno=" + "20170707152710650487N" + "&price=" + "1.00"
				+ "&payvia=" + "WEIXIN" + "&state=" + "1" + "&timespan=" + "20170707171501" + "&key=" + "318efaa6848b427f8d6094fccd0d45dc";
		System.out.println(param);
		String sign = utils.MD5(utils.MD5(param) + "318efaa6848b427f8d6094fccd0d45dc"); // 数据签名
		System.out.println("haio=9a8e32c3858b5f82cec419da05787db0");
		System.out.println("haio="+sign);
		System.out.println("test="+MD5Util.MD5Encode(MD5Util.MD5Encode(param) + "318efaa6848b427f8d6094fccd0d45dc")); // 数据签名
	}
}

