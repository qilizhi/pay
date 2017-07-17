package com.heepay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.heepay.Common.GatewayHelper;
import com.heepay.HeepayModel.GatewayModel;
import com.heepay.HeepayReturn.SubmitReturn;


//提交查询
@SuppressWarnings("unused")
public class SubmitQuery {
	public static void main(String[] args) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String time = df.format(new Date());
		
		String version = "1";//	必填	版本号1  
		String agent_id = "1664502";	//必填	商家数字帐号1234567
		String agent_bill_id = "E1792014111213136013688";	//必填	商户系统内部的定单号, 长度最长50字符
		String agent_bill_time = time;	//必填	单据的时间yyyyMMddHHmmss 20091112102000
		String remark = "179";	//必填	商家数据包，原样返回, 长度最长50字符
	    
	    GatewayModel gatewayModel = new GatewayModel();
	    gatewayModel.setagent_id(agent_id);
	    gatewayModel.setversion(version);
	    gatewayModel.setagent_bill_id(agent_bill_id);
	    gatewayModel.setagent_bill_time(agent_bill_time);
	    gatewayModel.setremark(remark);
	    
	    HeepaySubmit heepaySubmit = new HeepaySubmit();
	    SubmitReturn submitReturn = new SubmitReturn();
	    
	    submitReturn = heepaySubmit.SubmitQueryString(gatewayModel);//提交组织url参数
	    if(submitReturn.is_success())
	    {
	    	System.out.println(submitReturn.get_error_message());//出现错误打印出错误信息
	    	
	    }
	    else
	    {
	    	//这里代表组织提交的汇付宝收银台url。商户可以使用此地址在浏览器中访问进行测试
	    	System.out.println(submitReturn.get_error_message());

	    	//验证签名结果
	    	
	    	System.out.println("验证签名结果：" + GatewayHelper.VerifiSign(submitReturn.get_error_message()));
	    }
	    
	}
}
