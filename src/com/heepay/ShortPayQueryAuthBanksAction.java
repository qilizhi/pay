package com.heepay;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.heepay.Common.AES;
import com.heepay.Common.DataHelper;
import com.heepay.Common.ErrorUtil;
import com.heepay.Common.Md5Tools;
import com.heepay.HeepayReturn.HeepayXMLReturn;
import com.util.PropertiesUtil;

public class ShortPayQueryAuthBanksAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ShortPayQueryAuthBanksAction.class);
	
	private String agent_id;
	private String timestamp;
	private String version;
	private String user_identity;
	private String sign_key;
	private String data_key;
	private String url;

	public void doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Calendar calTime=Calendar.getInstance();
		long time= calTime.getTimeInMillis();//以1970年1月1日0:0:0为基线的毫秒
		
		agent_id = request.getParameter("agent_id"); // 给商户分配的唯一标识
		timestamp = String.valueOf(time); // 时间戳
		version = "1"; // 版本号，固定为1
		user_identity = request.getParameter("user_identity"); // 商户用户唯一标识
		sign_key = request.getParameter("sign_key");
		data_key = request.getParameter("data_key");
		url = PropertiesUtil.getInstance().getValue("heepay_shortPayQueryAuthBanks_url");
		
		logger.info("agent_id=" + agent_id + "&timestamp=" + timestamp + "&version=" + version 
				+ "&user_identity=" + user_identity + "&sign_key=" + sign_key
				+ "&data_key=" + data_key + "&url=" + url);

		Map<String, String> mapData = new HashMap<String, String>();
		mapData.put("agent_id", agent_id);
		mapData.put("version", version);
		mapData.put("timestamp", timestamp);
		mapData.put("user_identity", user_identity);
		mapData.put("key", sign_key);// 签名需要加入KEY
		String getSignData = DataHelper.GetSortQueryToLowerString(mapData); // 生成SIGN 需要将商户签名KEY加入后MD5
		String sign = Md5Tools.MD5(getSignData).toLowerCase();

		DataHelper.TranferCharsetEncode(mapData); // 平台之间的转码

		String[] filterKeys = { "agent_id", "key" };
		String getEncryptData = DataHelper.GetSortFilterQueryString(mapData, filterKeys); // 生成 encrypt_data

		String encrypt_data = URLEncoder.encode(AES.Encrypt(getEncryptData, data_key), DataHelper.UTF8Encode); // AES加密后的URLENCODE字符串

		Map<String, String> mapResultData = new HashMap<String, String>();
		mapResultData.put("agent_id", agent_id); // 参考文档 汇付宝分配给商户的ID
		mapResultData.put("encrypt_data", encrypt_data);
		mapResultData.put("sign", sign);
		String postData = DataHelper.GetSortQueryString(mapResultData);

		String payheepReturnxml = DataHelper.RequestPostUrl(url, postData, DataHelper.UTF8Encode);
		HeepayXMLReturn payheepReturn = DataHelper.GetRetuenXmlContent(payheepReturnxml);
		
		//是否成功.
		boolean isSucess = payheepReturn.get_is_success();
		if(!isSucess) {
			writeBack("ret_code=" + payheepReturn.get_retcode() + "&ret_msg=" + payheepReturn.get_retmsg());
			return;
		}
		encrypt_data=payheepReturn.get_encryptdata(); //为返回XML的加密的DATA
		writeBack(AES.Decrypt(encrypt_data, data_key));
	}
}
