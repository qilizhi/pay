package com.bfb;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 *******************************************************************************
 * 
 * <br>(c) Copyright 2014 币币科技有限公司
 *
 * <br>文 件  名：DES.java
 * <br>系统名称：bbpay-java-demo
 * <br>模块名称：(请更改成该模块名称)
 * <br>创 建  人：张杰
 * <br>创建日期：2015年7月20日 下午6:18:32
 * <br>修 改 人：(修改了该文件，请填上修改人的名字)
 * <br>修改日期：(请填上修改该文件时的日期)
 * <br>版     本： V1.0.0
 *******************************************************************************  
 */
public class DesUtil {
	
	/**
	 * 加密函数
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return 返回加密后的数据
	 */
	public static byte[] CBCEncrypt(byte[] data, byte[] key, byte[] iv) {

		try {
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 若采用NoPadding模式，data长度必须是8的倍数
			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密匙初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(data);

			return encryptedData;
		} catch (Exception e) {
			System.err.println("DES算法，加密数据出错!");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解密函数
	 * 
	 * @param data
	 *            解密数据
	 * @param key
	 *            密钥
	 * @return 返回解密后的数据
	 */
	public static byte[] CBCDecrypt(byte[] data, byte[] key, byte[] iv) {
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// using DES in CBC mode
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 若采用NoPadding模式，data长度必须是8的倍数
			// Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

			// 用密匙初始化Cipher对象
			IvParameterSpec param = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(data);

			return decryptedData;
		} catch (Exception e) {
			System.err.println("DES算法，解密出错。");
			e.printStackTrace();
		}

		return null;
	}
}
