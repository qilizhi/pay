package com.heepay.Common;

import java.util.HashMap;
import java.util.Map;

public class ErrorUtil {
	private static Map<String,String> errMap;
	
	static {
		errMap = new HashMap<String,String>();
		errMap.put("0000","执行成功");
		errMap.put("E100","商户未授权或没有开通快捷支付");
		errMap.put("E101","签名验证错误");
		errMap.put("E102","数据解密错误");
		errMap.put("E103","时间戳过期");
		errMap.put("E104","输入参数验证错误");
		errMap.put("E105","银行返回的具体错误描述");
		errMap.put("E106","风险控制错误");
		errMap.put("E107","授权过期");
		errMap.put("U999","很抱歉，系统出现异常错误，确认操作是否成功");
	}
	
	public static String getErrMsg(String errCode) {
		return errMap.get(errCode);
	}
}
