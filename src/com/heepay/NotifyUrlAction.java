package com.heepay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.entity.obj.PayObj;
import com.heepay.Common.Md5Tools;
import com.util.CommonMethod;
import com.util.HttpUtil;
import com.util.LocalCache;

/**
 * 
 * <p>
 * <b>NotifyUrlAction</b> 是 支付通知接口。接收汇付宝返回的处理结果，并将结果传回平台
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @since 2016年5月4日
 */
public class NotifyUrlAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(NotifyUrlAction.class);

	public void doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();

		// 支付结果, 1=成功 其它为未知
		String result = new String(request.getParameter("result").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 支付结果信息，支付成功时为空
		// String pay_message = new String(request.getParameter("pay_message")
		// .getBytes("ISO-8859-1"), "UTF-8");

		// 商户编号 如1234567
		String agent_id = new String(request.getParameter("agent_id").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 汇付宝交易号(订单号)
		String jnet_bill_no = new String(request.getParameter("jnet_bill_no")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 商户系统内部的定单号
		String agent_bill_id = new String(request.getParameter("agent_bill_id")
				.getBytes("ISO-8859-1"), "UTF-8");

		// 30
		String pay_type = new String(request.getParameter("pay_type").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 订单实际支付金额(注意：此金额是用户的实付金额)
		String pay_amt = new String(request.getParameter("pay_amt").getBytes(
				"ISO-8859-1"), "UTF-8");

		// 商家数据包，原样返回
		String remark = new String(request.getParameter("remark").getBytes(
				"ISO-8859-1"), "UTF-8");

		// MD5签名结果
		String sign = new String(request.getParameter("sign").getBytes(
				"ISO-8859-1"), "UTF-8");

		PayObj obj = new PayObj();
		obj = (PayObj) LocalCache.get(agent_bill_id, "");
		String notify_url = obj.loopbackaddress;
		String key = obj.transferkey;

		// 首先验证签名的正确性
		StringBuffer str = new StringBuffer();
		str.append("result=").append(result).append("&");
		str.append("agent_id=").append(agent_id).append("&");
		str.append("jnet_bill_no=").append(jnet_bill_no).append("&");
		str.append("agent_bill_id=").append(agent_bill_id).append("&");
		str.append("pay_type=").append(pay_type).append("&");
		str.append("pay_amt=").append(pay_amt).append("&");
		str.append("remark=").append(remark).append("&");
		str.append("key=").append(key);

		logger.info("heepay response str is : " + str);

		String signstr = Md5Tools.MD5(str.toString()).toLowerCase();

		logger.info("signstr :" + signstr);
		logger.info("sign :" + sign);

		String res = "";
		if (signstr.equals(sign)) {
			logger.info("验证签名正确");
			// 返回平台
			JSONObject json1 = new JSONObject();
			json1.put("result", result);
			json1.put("agent_id", agent_id);
			json1.put("jnet_bill_no", jnet_bill_no);
			json1.put("agent_bill_id", agent_bill_id);
			json1.put("pay_type", pay_type);
			json1.put("pay_amt", pay_amt);
			json1.put("remark", remark);

			JSONArray ja = new JSONArray();
			ja.add(json1);

			JSONObject json = new JSONObject();
			json.put("data", ja);

			byte[] requestData = CommonMethod.getByteUTF8(json);

			res = HttpUtil.post(requestData, notify_url);
		} else {
			logger.info("验证签名失败");
			res = "error";
		}

		// 返回的信息，不能包含HTML等标签，所有内容只有 ok 或 error 。 (不能有其他任何输出（包括空格、空行）) 。
		if (!("error".equals(res) || "ok".equals(res))) {
			res = "error";
		}

		if ("ok".equals(res)) {
			LocalCache.remove(agent_bill_id);
		}

		// 向汇付宝返回数据
		writeBack(res);

		/************************************** 日志 ***********************************************/
		// add by hewentian 不知道为什么要存到文件里面
		
		String path = "";
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if (os.startsWith("win") || os.startsWith("Win"))
			path = request.getRealPath("/log");
		else
			path = "/ngbs/local/apache-tomcat-6.0.44/log";
		File fp = new File(path, "result.txt");

		if (!fp.getParentFile().exists()) {
			fp.getParentFile().mkdirs();
		}
		if (!fp.exists()) {
			fp.createNewFile();
		}

		System.out.println("================================path:" + path);
		FileWriter fwriter = new FileWriter(fp, true);
		BufferedWriter bfwriter = new BufferedWriter(fwriter);
		request.setCharacterEncoding("UTF-8");
		bfwriter.newLine();
		bfwriter.write("=========================================================");
		bfwriter.newLine();
		bfwriter.write("result:" + result + ";jnet_bill_no=" + jnet_bill_no
				+ ";pay_amt=" + pay_amt);
		bfwriter.newLine();
		bfwriter.write("---------------------------------------------------------");
		bfwriter.newLine();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		bfwriter.write("Logdate=" + df.format(new Date()));
		bfwriter.newLine();
		bfwriter.write("IP=" + request.getRemoteAddr());
		bfwriter.newLine();
		bfwriter.write("=========================================================");
		bfwriter.newLine();
		bfwriter.flush();
		bfwriter.close();
		fwriter.close();
	}
}