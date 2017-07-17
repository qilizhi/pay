package com.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * <b>LocalCache</b> 是 本地缓存类
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @since 2016年5月4日
 */
public class LocalCache {
	private static Map<String, Object> map = new HashMap<String, Object>();

	public static void put(String key, Object value) {
		map.put(key, value);
	}

	public static Object get(String key, Object defaultValue) {
		Object o = map.get(key);

		if (null == o) {
			o = defaultValue;
		}

		return o;
	}

	public static void remove(String key) {
		map.remove(key);
	}
}