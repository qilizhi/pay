package com.heepay;

import java.io.UnsupportedEncodingException;

import com.heepay.Common.DataHelper;
import com.heepay.Common.GatewayHelper;
import com.heepay.HeepayModel.GatewayModel;
import com.heepay.HeepayReturn.SubmitReturn;



public class HeepaySubmit {
	
	//提交构建提交URL
	public SubmitReturn SubmitUrl(GatewayModel gatewayModel) throws UnsupportedEncodingException
	{
		SubmitReturn submitReturn = new SubmitReturn();
		
		String sign = GatewayHelper.SignMd5(DataHelper.sign_key,gatewayModel);//构建签名
		String StrUrl = GatewayHelper.GatewaySubmitUrl(sign,gatewayModel);//构建提交地址
		
		//此处默认为成功
		//需要加判断，后期完善，需要处理好异常情况
		submitReturn.set_success(false);
		submitReturn.set_error_message(StrUrl);
		
		return submitReturn;
	}
	
	public SubmitReturn SubmitQueryString(GatewayModel gatewayModel) throws UnsupportedEncodingException
	{
		String url = "https://query.heepay.com/Payment/Query.aspx";//此为测试地址，商户应使用文档中的正式地址
		
		SubmitReturn submitReturn = new SubmitReturn();
		
		String sign = GatewayHelper.SignQueryMd5(DataHelper.sign_key,gatewayModel);//构建签名
		String QureyStr = GatewayHelper.GatewaySubmitQuery(sign,gatewayModel);//构建提交地址
		String returnMessge = DataHelper.RequestPostUrl(url,QureyStr);
		//此处默认为成功
		//需要加判断，后期完善，需要处理好异常情况
		submitReturn.set_success(false);
		submitReturn.set_error_message(returnMessge);
		
		return submitReturn;
	}
}
