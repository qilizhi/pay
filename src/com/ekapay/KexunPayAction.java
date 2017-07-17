package com.ekapay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ekapay.service.EkapayService;
import com.ekapay.util.ConfigUtil;
import com.ekapay.util.DataHelper;
import com.util.StringUtils;
import com.heepay.BaseAction;

/**
 * 科讯支付
 */
public class KexunPayAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(KexunPayAction.class);

	/**
	 * 1.商户发给网关接口的参数
	 *  参数名称		参数含义		可空	加入签名	   参数说明
	 * 	parter		商户ID	 	 N	  Y	         商户在网关系统上的商户号
	 * 	key			商户秘钥	 	 N	  Y	         商户在网关系统上的商秘钥
	 * 	type		银行类型	 	 N	  Y	         参照附录1中的对应值(支付宝wap(1003),微信wap(1005))
	 * 	value		支付金额		 N	  Y	         订单支付金额（元）
	 * 	orderid		订单编号		 N	  Y	         订单编号
	 * 	callbackurl	通知商户Url	 N	  Y    通知商户充值状态的地址，该地址不能带任何参数，否则异步通知会不成功
	 *  hrefbackurl	充值回调Url	 Y	  N    通知回调Url:充值后商户的跳转地址(友好提示,实际结果已通知商户Url为准)，该地址不能带任何参数，否则回调时会不成功
	 *  payerIp	支付用户IP			 Y	  N	         支付端IP
	 * 	attach	备注消息	 		 Y	  N	         备注消息,下行中会原样返回。若该值包含中文，请注意编码
	 */
	public void doPay() {
		HttpServletRequest request = ServletActionContext.getRequest();

		logger.info("【科讯支付】请求支付参数, request:{" + JSONObject.fromObject(request.getParameterMap()).toString() + "}");

		String key = request.getParameter("key");
		if (StringUtils.isEmpty(key)) {
			writeError("商户秘钥不能为空");
			return;
		}

		String parter = request.getParameter("parter");
		if (StringUtils.isEmpty(parter)) {
			writeError("商户号不能为空");
			return;
		}

		String type = request.getParameter("type");
		if (StringUtils.isEmpty(type)) {
			writeError("支付类型不能为空");
			return;
		}

		String value = request.getParameter("value");
		if (StringUtils.isEmpty(value)) {
			writeError("支付金额不能为空");
			return;
		}

		String callbackurl = request.getParameter("callbackurl");
		if (StringUtils.isEmpty(callbackurl)) {
			writeError("充值通知url不能为空");
			return;
		}

		String hrefbackurl = request.getParameter("hrefbackurl");
		if (StringUtils.isEmpty(hrefbackurl)) {
			writeError("充值回调url不能为空");
			return;
		}

		String payerIp = request.getParameter("payerIp");
		if (StringUtils.isEmpty(payerIp)) {
			writeError("支付用户IP不能为空");
			return;
		}

		String orderid = request.getParameter("orderid");
		if (StringUtils.isEmpty(orderid)) {
			writeError("订单编号不能为空");
			return;
		}

		Map<String, String> parmar = new HashMap<String, String>();
		parmar.put("parter", parter);
		parmar.put("type", type);
		parmar.put("value", value);
		parmar.put("orderid", orderid);
		parmar.put("callbackurl", callbackurl);
		parmar.put("hrefbackurl", hrefbackurl);
		parmar.put("payerIp", payerIp);// 支付端IP
		parmar.put("attach", StringUtils.defaultEmpty(request.getParameter("attach")));// 扩展参数
		parmar.put("key", key);

		String postData = EkapayService.buildPayParmar(parmar);
		
		String requestPostUrl = ConfigUtil.getPayUrl() + postData;
		logger.info("【科讯支付】请求支付URL, request url:{" + requestPostUrl + "}");
		writeSuccess(requestPostUrl);
	}

	/**
	 * 2.网关接口异步返回(callbackurl)给商户的参数
	 *  参数名称			参数含义	     加入签名	   	参数说明
	 *  orderid			商户订单号		Y		1.支付成功 2.支付失败
	 *  opstate			订单结果		Y		0：处理成功   -1：请求参数无效  -2：签名错误
	 *  ovalue			订单金额		Y		订单实际支付金额，单位元
	 *  sysorderid		科讯订单号		N		此次订单过程中科讯支付接口系统内的订单号
	 *  completiontime	完成时间		N		此次订单过程中科讯支付接口系统内的订单结束时间。 格式为年/月/日 时：分：秒，如2010/04/05 21:50:58
	 *  attach			备注信息		N		备注信息，上行中attach原样返回
	 *  msg				订单结果说明	N		订单结果说明
	 *  sign			md5签名字符串	-		发送给商户的签名字符串小写
	 *  
	 *  {"sign":["e20ec6c5b938db3af2434f5c52150460"],"systime":["2016/12/12 18:05:35"],"attach":[""],"ovalue":["1"],"opstate":["0"],"sysorderid":["B201612121803325041"],"orderid":["161212180305012"],"msg":["1"]}
	 */
	public void callback() {
		HttpServletRequest request = ServletActionContext.getRequest();

		logger.info("【科讯支付】通知中转平台充值回调结果, response:{" + JSONObject.fromObject(request.getParameterMap()).toString() + "}");

		String orderid = request.getParameter("orderid");
		if (StringUtils.isEmpty(orderid)) {
			writeBack("orderid不能为空");
			return;
		}

		String opstate = request.getParameter("opstate");
		if (StringUtils.isEmpty(opstate)) {
			writeBack("opstate不能为空");
			return;
		}

		String ovalue = request.getParameter("ovalue");
		if (StringUtils.isEmpty(ovalue)) {
			writeBack("ovalue不能为空");
			return;
		}

		String sign = request.getParameter("sign");
		if (StringUtils.isEmpty(sign)) {
			writeBack("sign不能为空");
			return;
		}

		writeBack("opstate=0");// 写通知回去
		
		String attach = StringUtils.defaultEmpty(request.getParameter("attach"));// 扩展参数
		String completiontime = request.getParameter("completiontime");
		if (StringUtils.isEmpty(completiontime)) {
			completiontime = StringUtils.defaultEmpty(request.getParameter("systime"));
		}

		StringBuffer postData = new StringBuffer();
		postData.append("orderid=" + orderid);
		postData.append("&opstate=" + opstate);
		postData.append("&ovalue=" + ovalue);
		postData.append("&sysorderid=" + request.getParameter("sysorderid"));
		postData.append("&completiontime=" + completiontime);
		postData.append("&attach=" + attach);
		postData.append("&msg=" + request.getParameter("msg"));
		postData.append("&sign=" + sign);

		String response = DataHelper.RequestPostUrl(ConfigUtil.getCashCallBackUrl(), postData.toString());
		logger.info("【科讯支付】中转平台通知现金平台充值结果, response:{" + response + "}");
	}

	public static void main(String[] args) {
		Map<String, String> parmar = new HashMap<String, String>();
		parmar.put("parter", "1581");
		parmar.put("type", "1005");
		parmar.put("value", "1");
		parmar.put("orderid", "VG" + System.currentTimeMillis());
		parmar.put("callbackurl", "");// 通知商户Url:通知商户充值状态的地址，该地址不能带任何参数，否则异步通知会不成功
		parmar.put("hrefbackurl", "");// 通知回调Url:充值后商户的跳转地址(友好提示,实际结果已通知商户Url为准)，该地址不能带任何参数，否则回调时会不成功
		parmar.put("payerIp", "127.0.0.1");// 支付端IP
		parmar.put("attach", "");
		parmar.put("key", "b492cba5793340e08485d7b384848057");

		String postData = EkapayService.buildPayParmar(parmar);
		System.out.println(ConfigUtil.getPayUrl() + postData);
	}

}
