package com.heepay.Common;

import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES {
	public static String Encrypt(String data,String base64Key) throws Exception 
	{
		try 
		{ 
		   byte[] key = Base64.decodeBase64(base64Key); 
		   byte[] iv = Arrays.copyOf(key, 16);
	       Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");  
	       int blockSize = cipher.getBlockSize();        
	       byte[] dataBytes = data.getBytes();      
	       int plaintextLength = dataBytes.length;         
	       if (plaintextLength % blockSize != 0) 
	       {
	    	   plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));  
	       }
	       byte[] plaintext = new byte[plaintextLength];
	       System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length); 
	       SecretKeySpec keyspec = new SecretKeySpec(key, "AES");  
	       IvParameterSpec ivspec = new IvParameterSpec(iv); 
	       cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);   
	       byte[] encrypted = cipher.doFinal(plaintext);      
	       return Base64.encodeBase64String(encrypted); 
	    } catch (Exception e) 
	    { 
	       e.printStackTrace();          
	       return null;     
	   }     
	} 
	

	public static String Decrypt(String base64Data,String base64Key) throws Exception 
	{ 
	     try 
	     { 
	    	 byte[] data = Base64.decodeBase64(base64Data);
	    	 byte[] key =Base64.decodeBase64(base64Key); 
			 byte[] iv = Arrays.copyOf(key, 16);
			 Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding"); 
	         SecretKeySpec keyspec = new SecretKeySpec(key, "AES"); 
	         IvParameterSpec ivspec = new IvParameterSpec(iv); 
	         cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec); 
	         ArrayList<Byte> arr = new ArrayList<Byte>();
	         byte[] original = cipher.doFinal(data); 
	         for(int i=0;i<original.length;i++)
	         {
	        	 byte val = original[i];
	        	 if(val != 0)
	        	 {
	        		 arr.add(val);
	        	 }
	         }
	         
	         Object[] arrObj = arr.toArray();
	         byte[] arrByte  = new byte[arrObj.length];
	         for(int i=0;i<arrObj.length;i++)
	         {
	        	 arrByte[i] = Byte.parseByte(arrObj[i].toString());
	         }
	         
	         String originalString = new String(arrByte,DataHelper.UTF8Encode); 
	         return originalString; 
	     } 
	     catch (Exception e) 
	     { 
	         e.printStackTrace(); 
	         return null; 
	     }
	} 
}
