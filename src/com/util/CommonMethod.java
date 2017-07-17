package com.util;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * <b>CommonMethod</b> 是 常用的方法工具类
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @since 2016年5月4日
 */
public class CommonMethod {
	/**
	 * 获取JSON的UTF-8的BYTES
	 * 
	 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
	 * @date 2016年5月4日 下午8:09:20
	 * @param json
	 * @return
	 */
	public static byte[] getByteUTF8(JSONObject json) {
		try {
			return json.toString().trim().getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}