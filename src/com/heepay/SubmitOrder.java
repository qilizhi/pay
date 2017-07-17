package com.heepay;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.heepay.HeepayModel.GatewayModel;
import com.heepay.HeepayReturn.SubmitReturn;


/*备注
 * 汇付宝平台使用的gb2312编码
 * 如果商户平台使用的UTF-8编码，商户需要转码传给汇付宝平台
 * 例如：URLEncoder.encode("文档", "gb2312"));
 * 此demo只给商户提供参数组织的过程，业务实现并没有。因此需要商户对实际应用进行实际的处理
 * 
 */

public class SubmitOrder {
	public static void main(String[] args) throws Exception {
		
		URLEncoder.encode("","");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String time = df.format(new Date());
		String agent_id = "1664502";             //商户编号 如1001  替换商户的商户号
		String version = "1";                   //当前接口版本号
	    String pay_type = "30";                 //微信支付
	    String pay_code = "";                  //空字符串
	    String agent_bill_id = "A" + time + "I";//商户系统内部的定单号（要保证唯一）。长度最长50字符
	    String pay_amt = "0.1";				//订单总金额 不可为空，取值范围（0.01到10000000.00），单位：元，小数点后保留两位。
	    String notify_url = "https://heepay.com/payheepay/notifyurl.aspx";					//支付后返回的商户处理页面，URL参数是以http://或https://开头的完整URL地址(后台处理) 提交的url地址必须外网能访问到,否则无法通知商户。值可以为空，但不可以为null。
	    String return_url = "https://heepay.com/payheepay/returnurl.aspx";					//支付后返回的商户显示页面，URL参数是以http:// 或https://开头的完整URL地址(前台显示)，原则上：该参数与notify_url提交的参数不一致。值可以为空，但不可以为null。
	    String user_ip = "192_168_564_123";//"192_168_564_123";		//用户所在客户端的真实IP其中的“.”替换为“_” 。如 127_127_12_12。因为近期我司发现用户在提交数据时，user_ip在网络层被篡改，导致签名错误，所以我们规定使用这种格式。
	    String agent_bill_time = time;			//提交单据的时间yyyyMMddHHmmss 如：20100225102000该参数共计14位，当时不满14位时，在后面加0补足14位
	    String goods_name = "youci";			//商品名称, 长度最长50字符，不能为空（不参加签名）
	    String goods_num = "1";					//产品数量,长度最长20字符（不参加签名）
	    String remark = "mark";					//商户自定义 原样返回,长度最长50字符，可以为空。（不参加签名）
	    //String is_test = "1";
	    String is_phone="1";                    //是否使用手机端微信支付，1=是，微信扫码支付不用传本参数
	    String is_frame="0";                    //1（默认值）=使用微信公众号支付，0=使用WAP微信支付
	    String goods_note = "note"; 			//支付说明, 长度50字符（不参加签名）
	    
	    GatewayModel gatewayModel = new GatewayModel();
	    gatewayModel.setagent_id(agent_id);
	    gatewayModel.setversion(version);
	    gatewayModel.setpay_type(pay_type);
	    gatewayModel.setpay_code(pay_code);
	    gatewayModel.setagent_bill_id(agent_bill_id);
	    gatewayModel.setpay_amt(pay_amt);
	    gatewayModel.setnotify_url(notify_url);
	    gatewayModel.setreturn_url(return_url);
	    gatewayModel.setuser_ip(user_ip);
	    gatewayModel.setagent_bill_time(agent_bill_time);
	    gatewayModel.setgoods_name(goods_name);
	    gatewayModel.setgoods_num(goods_num);
	    gatewayModel.setremark(remark);
	    gatewayModel.setgoods_note(goods_note);
	    gatewayModel.setis_phone(is_phone);
	    gatewayModel.setis_frame(is_frame);
	    
	    HeepaySubmit heepaySubmit = new HeepaySubmit();
	   
		SubmitReturn submitReturn = new SubmitReturn();
	    
	    submitReturn = heepaySubmit.SubmitUrl(gatewayModel);//提交组织URL参数
	    if(submitReturn.is_success())
	    {
	    	System.out.println(submitReturn.get_error_message());//出现错误打印出错误信息
	    }
	    else
	    {
	    	//这里代表组织提交的汇付宝收银台URL。商户可以使用此地址在浏览器中访问进行测试
	    	System.out.println(submitReturn.get_error_message());
	    }
	    
	  // byte[] b = {114,101,116,95,99,111,100,101,61,48,48,48,48,38,114,101,116,95,109,115,103,61,38,97,103,101,110,116,95,105,100,61,49,56,53,48,55,55,56,38,104,121,95,98,105,108,108,95,110,111,61,50,56,54,56,52,38,115,116,97,116,117,115,61,49,38,98,97,116,99,104,95,110,111,61,55,55,56,50,48,49,52,49,50,50,53,48,57,52,57,51,48,38,98,97,116,99,104,95,97,109,116,61,49,46,48,48,13,10,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,38,98,97,116,99,104,95,110,117,109,61,49,38,100,101,116,97,105,108,95,100,97,116,97,61,55,55,56,50,48,49,52,49,50,50,53,48,57,52,57,51,48,94,54,50,49,52,56,51,53,51,49,48,50,52,56,54,55,55,94,63,63,63,63,94,49,46,48,48,48,48,94,83,94,38,101,120,116,95,112,97,114,97,109,49,61,63,63,63,63,63,63,63,63,38,107,101,121,61,69,68,69,57,50,50,53,49,56,50,50,70,52,68,50,55,65,48,51,56,55,57,54,52};
		//System.out.println(Md5Tools.MD5(str));
		//System.out.println(URLDecoder.decode(new String(b, "utf-8"), "gb2312"));
		
	}
}