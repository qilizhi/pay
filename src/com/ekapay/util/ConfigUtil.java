package com.ekapay.util;

/**
 * 获取配置文件地址
 */
public abstract class ConfigUtil {

	/*************************************************************************************************************/
	/*********************************** TODO 科讯支付相关     **********************************************************/
	/*************************************************************************************************************/
	
	/**
	 * 【科讯支付】科讯支付网址
	 */
	public static String getPayUrl() {
		return EkaPayConfig.getInstance().getValue("kexun_pay_url");
	}
	
	/**
	 * 【科讯支付】中转回调现金网址
	 */
	public static String getCashCallBackUrl() {
		return EkaPayConfig.getInstance().getValue("cash_call_back_url");
	}

	/*************************************************************************************************************/
	/*********************************** TODO 海鸥支付相关     **********************************************************/
	/*************************************************************************************************************/

	/**
	 * 【海鸥支付】中转回调现金网址
	 */
	public static String getHaioCallBackUrl() {
		return EkaPayConfig.getInstance().getValue("haio_call_back_url");
	}
	/*************************************************************************************************************/
	/*********************************** TODO 101支付相关     **********************************************************/
	/*************************************************************************************************************/

	/**
	 * 【101支付】101支付网址
	 */
	public static String getPuxunPayUrl() {
		return EkaPayConfig.getInstance().getValue("puxun_pay_url");
	}
	
	/**
	 *【101支付】中转回调现金网址
	 */
	public static String getPuxunCallBackUrl() {
		return EkaPayConfig.getInstance().getValue("puxun_call_back_url");
	}
	
	/**
	 * 【七星云支付】七星云支付网址
	 */
	public static String getXingPayUrl() {
		return EkaPayConfig.getInstance().getValue("xing_pay_url");
	}
	
	/**
	 *【七星云支付】中转回调现金网址
	 */
	public static String getXingCallBackUrl() {
		return EkaPayConfig.getInstance().getValue("xing_call_back_url");
	}
	/**
	 *【闪付支付】中转回调现金网址
	 */
	public static String getSanFuCallBackUrl(){
		return EkaPayConfig.getInstance().getValue("sanfu_call_back_url");
	}
	
	
	
}
