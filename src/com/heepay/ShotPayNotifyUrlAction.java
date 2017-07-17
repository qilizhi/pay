package com.heepay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.entity.obj.PayObj;
import com.heepay.Common.AES;
import com.heepay.Common.DataHelper;
import com.heepay.Common.Md5Tools;
import com.util.CommonMethod;
import com.util.HttpUtil;
import com.util.LocalCache;


public class ShotPayNotifyUrlAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ShotPayNotifyUrlAction.class);

	public void doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		logger.info("http://192.168.0.23:8085/Pay/ShortPayNotifyUrl.action?" + request.getQueryString());
		
		String agent_id = request.getParameter("agent_id"); // 给商户分配的唯一标识
		PayObj obj = new PayObj();
		obj = (PayObj) LocalCache.get(agent_id, "");
		String notify_url = obj.loopbackaddress;
		String data_key = obj.transferkey;
//		String notify_url = "http://192.168.0.23:8080/cash/cashApi/heepayNotify.action";
//		String data_key = "5PDyssDdEJSwG5Fc908wKChznYqHBEUSR8zg7Sw8ux8=";
		
		String encrypt_data = request.getParameter("encrypt_data"); // 加密数据
		String payheepReturnData = AES.Decrypt(encrypt_data, data_key);
		logger.info(payheepReturnData);
		Map<String,String> mapValues = DataHelper.URLRequestParams(payheepReturnData);
		
		String agent_bill_id = mapValues.get("agent_bill_id"); // 商户订单号
		String agent_bill_time = mapValues.get("agent_bill_time"); // 商户订单时间，格式为“yyyyMMddhhmmss”,例 20130910170601
		String hy_bill_no = mapValues.get("hy_bill_no"); // 汇元网订单号
		String hy_deal_time = mapValues.get("hy_deal_time"); // 汇元网订单处理时间，格式为“yyyyMMddhhmmss”,例 20130910170601
		String hy_deal_note = mapValues.get("hy_deal_note"); // 处理描述
		String pay_amt = mapValues.get("pay_amt"); // 支付金额，单位：元，保留二位小数
		String real_amt = mapValues.get("real_amt"); // 实际支付金额，单位：元，保留二位小数
		String status = mapValues.get("status"); // 订单状态：SUCCESS：支付成功 WFPAYMENT：等待支付 CLOSED：取消
		String ext_param1 = mapValues.get("ext_param1"); // 商户保留，支付完成后的通知会将该参数返回给商户
		String ext_param2 = mapValues.get("ext_param2"); // 商户保留，支付完成后的通知会将该参数返回给商户

		String res = "";
		// 返回平台
		JSONObject json1 = new JSONObject();
		json1.put("agent_id", agent_id);
		json1.put("agent_bill_id", agent_bill_id);
		json1.put("agent_bill_time", agent_bill_time);
		json1.put("hy_bill_no", hy_bill_no);
		json1.put("hy_deal_time", hy_deal_time);
		json1.put("hy_deal_note", hy_deal_note);
		json1.put("pay_amt", pay_amt);
		json1.put("real_amt", real_amt);
		json1.put("status", status);
		json1.put("ext_param1", ext_param1);
		json1.put("ext_param2", ext_param2);

		JSONArray ja = new JSONArray();
		ja.add(json1);

		JSONObject json = new JSONObject();
		json.put("ShotPayData", ja);

		byte[] requestData = CommonMethod.getByteUTF8(json);

		res = HttpUtil.post(requestData, notify_url);
		

		// 返回的信息，不能包含HTML等标签，所有内容只有 ok 或 error 。 (不能有其他任何输出（包括空格、空行）) 。
		if (!("error".equals(res) || "ok".equals(res))) {
			res = "error";
		}

		// 向汇付宝返回数据
		writeBack(res);
	}
}