package com.util;

public class StringUtils {

	/**
	 * </br><b>title : </b> 获取字符串长度 </br><b>description:</b> 获取字符串长度,为null则返回0
	 * 
	 * @param s 需要获取长度的字符串
	 * @return 字符串的长度
	 */
	public static int length(String s) {
		return (s == null ? 0 : s.trim().length());
	}

	/**
	 * </br><b>title : </b> 验证字符串为空 </br><b>description:</b>
	 * 验证字符串是否为空,true为null,否则为false
	 * 
	 * @param s 需要判断的字符串
	 * @return 字符串是否为空
	 */
	public static boolean isEmpty(String s) {
		return (length(s) == 0);
	}

	/**
	 * </br><b>title : </b> 验证字符串不为空 </br><b>description:</b>
	 * 验证字符串是否不为空,不为空返回true,否则为false
	 * 
	 * @param s 需要判断的字符串
	 * @return 字符串是否不为空
	 */
	public static boolean notEmpty(String s) {
		return (length(s) != 0);
	}

	/**
	 * </br><b>title : </b> 获取字符串默认值 </br><b>description:</b>
	 * 不为空返回字符串,否则返回默认值
	 * 
	 * @param s 需要判断的字符串
	 * @param def 默认值
	 */
	public static String defaultValue(String s, String def) {
		return isEmpty(s) ? def : s;
	}

	/**
	 * </br><b>title : </b> 获取字符串默认值 </br><b>description:</b>
	 * 不为空返回字符串,否则返回空字符串
	 * 
	 * @param s 需要判断的字符串
	 * @param def 默认值
	 */
	public static String defaultEmpty(String s) {
		return defaultValue(s, "");
	}

	public static String formatString(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}

	public static Boolean hasText(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String[] strSplit(String str, String split) {
		if (!StringUtils.hasText(str))
			return new String[] {};
		return str.split(split);
	}
}
