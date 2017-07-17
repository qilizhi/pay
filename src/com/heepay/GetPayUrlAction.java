package com.heepay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.eason.engine.util.MD5Util;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.entity.obj.PayObj;
import com.heepay.Common.DataHelper;
import com.heepay.HeepayModel.GatewayModel;
import com.heepay.HeepayReturn.SubmitReturn;
import com.util.LocalCache;
import com.util.PropertiesUtil;

/**
 * 
 * <p>
 * <b>GetPayUrlAction</b> 是 中转用，获取汇付宝支付URL
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @since 2016年5月5日
 */
public class GetPayUrlAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GetPayUrlAction.class);

	public void doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String agent_id = request.getParameter("agent_id");  //商户编号 如1001  替换商户的商户号
		String version = request.getParameter("version");  //当前接口版本号
		String pay_type = request.getParameter("pay_type");  ////微信支付 =30   支付宝22 
		String pay_code = request.getParameter("pay_code");  ////空字符串
		String agent_bill_id = request.getParameter("agent_bill_id");  ///商户系统内部的定单号（要保证唯一）。长度最长50字符
		String pay_amt = request.getParameter("pay_amt");  ////订单总金额 不可为空，取值范围（0.01到10000000.00），单位：元，小数点后保留两位。
		String notify_url = request.getParameter("notify_url");  //支付后返回的商户处理页面，URL参数是以http://或https://开头的完整URL地址(后台处理) 提交的url地址必须外网能访问到,否则无法通知商户。值可以为空，但不可以为null。
		String return_url = request.getParameter("return_url");  //支付后返回的商户显示页面，URL参数是以http:// 或https://开头的完整URL地址(前台显示)，原则上：该参数与notify_url提交的参数不一致。值可以为空，但不可以为null。
		String user_ip = request.getParameter("user_ip");  //用户所在客户端的真实IP其中的“.”替换为“_” 。如 127_127_12_12。因为近期我司发现用户在提交数据时，user_ip在网络层被篡改，导致签名错误，所以我们规定使用这种格式。
		String agent_bill_time =request.getParameter("agent_bill_time"); 	//提交单据的时间yyyyMMddHHmmss 如：20100225102000该参数共计14位，当时不满14位时，在后面加0补足14位
		String goods_name = request.getParameter("goods_name");  //商品名称, 长度最长50字符，不能为空（不参加签名）
		String goods_num = request.getParameter("goods_num");  //产品数量,长度最长20字符（不参加签名）
		String remark = request.getParameter("remark");  //商户自定义 原样返回,长度最长50字符，可以为空。（不参加签名）
		String goods_note = request.getParameter("goods_note");  //支付说明, 长度50字符（不参加签名）

		String key = request.getParameter("key");  //key
		String loopbackaddress = PropertiesUtil.getInstance().getValue("heepay_callback_url");  //key

		String is_phone = request.getParameter("is_phone");    //是否使用手机端微信支付，1=是，微信扫码支付不用传本参数
		String is_frame = request.getParameter("is_frame");       //1（默认值）=使用微信公众号支付，0=使用WAP微信支付
		String code = request.getParameter("code");       //code

		logger.info("agent_id:"+agent_id);
		logger.info("goods_note:"+goods_note);
		logger.info("agent_bill_id:"+agent_bill_id);
		logger.info("is_phone:"+is_phone);
		logger.info("is_frame:"+is_frame);
		logger.info("user_ip:"+user_ip);
		logger.info("goods_name:"+goods_name);
		logger.info("code:"+code);

		String md5code = MD5Util.md5Hex(agent_id + key + version+pay_type+pay_code+agent_bill_id+pay_amt + user_ip+ agent_bill_time + goods_name +goods_num + remark).toUpperCase();

		logger.info("md5code:"+md5code);

		String res= "";

		if(md5code.equals(code)){
			
			DataHelper d = new DataHelper();
			d.sign_key = key;

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
			
			logger.info("submitReturn-----:"+submitReturn.is_success());
			if(submitReturn.is_success())
			{
				logger.info(submitReturn.get_error_message());//出现错误打印出错误信息
			}
			else
			{
				//这里代表组织提交的汇付宝收银台URL。商户可以使用此地址在浏览器中访问进行测试
				logger.info(submitReturn.get_error_message());
				
				// https://pay.heepay.com/Payment/Index.aspx?version=1&pay_type=22&pay_code=&agent_id=2060516&agent_bill_id=160421201908008&pay_amt=100&notify_url=http://192.168.0.23:8085/Pay/heepay/notify_url.jsp&return_url=http://192.168.0.23:8085/Pay/heepay/notify_url.jsp&user_ip=127_0_0_1&agent_bill_time=20160421201908&goods_name=&goods_num=1&remark=160421201908008&goods_note=note&is_phone=1&is_frame=0&sign=1eeb03ca229e5e2ad6e22dcf68893536
					res = submitReturn.get_error_message();
				  res = res.replace("&", "&amp;") ;  // 转义 & 符号
			}
			
			
			
//			String keyHashMap = "keyHashMap";
//			HashMap<String, PayObj> map = (HashMap<String, PayObj>)application.getAttribute(keyHashMap);
//			if (map == null) {
//			 map = new HashMap<String, PayObj>();
//			 application.setAttribute(keyHashMap, map);
//			}
			PayObj payObj = new PayObj();
			payObj.mercode = agent_id ;
			payObj.transferkey = key ;
			payObj.loopbackaddress = loopbackaddress ;
			
			LocalCache.put(agent_bill_id, payObj);
			
			logger.info("agent_id-----:"+agent_id);
			logger.info("key-----:"+key);
			logger.info("loopbackaddress-----:"+loopbackaddress);
			

		}else{
			res = "非法请求！";
		}
		// byte[] b = {114,101,116,95,99,111,100,101,61,48,48,48,48,38,114,101,116,95,109,115,103,61,38,97,103,101,110,116,95,105,100,61,49,56,53,48,55,55,56,38,104,121,95,98,105,108,108,95,110,111,61,50,56,54,56,52,38,115,116,97,116,117,115,61,49,38,98,97,116,99,104,95,110,111,61,55,55,56,50,48,49,52,49,50,50,53,48,57,52,57,51,48,38,98,97,116,99,104,95,97,109,116,61,49,46,48,48,13,10,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,32,38,98,97,116,99,104,95,110,117,109,61,49,38,100,101,116,97,105,108,95,100,97,116,97,61,55,55,56,50,48,49,52,49,50,50,53,48,57,52,57,51,48,94,54,50,49,52,56,51,53,51,49,48,50,52,56,54,55,55,94,63,63,63,63,94,49,46,48,48,48,48,94,83,94,38,101,120,116,95,112,97,114,97,109,49,61,63,63,63,63,63,63,63,63,38,107,101,121,61,69,68,69,57,50,50,53,49,56,50,50,70,52,68,50,55,65,48,51,56,55,57,54,52};
		//System.out.println(Md5Tools.MD5(str));
		//System.out.println(URLDecoder.decode(new String(b, "utf-8"), "gb2312"));

		
		writeBack(res);

		/**************************************记录日志***********************************************/	
		// add by hewentian 不知道为什么要存到文件里面
		
			String path = "";
			Properties prop = System.getProperties();
			String os = prop.getProperty("os.name");
			if(os.startsWith("win") || os.startsWith("Win"))
			 	path = request.getRealPath("/Log");
			else
				path = "/ngbs/local/apache-tomcat-6.0.44/log";
			File fp = new File(path, "KeyLog.txt");
			if (!fp.getParentFile().exists()) {
				fp.getParentFile().mkdirs();
			}
			if (!fp.exists()) {
				fp.createNewFile();
			}
			
			System.out.println("================================"+path);
			
			FileWriter fwriter = new FileWriter(fp, true);
			BufferedWriter bfwriter = new BufferedWriter(fwriter);
			request.setCharacterEncoding("UTF-8");
			bfwriter.newLine();
			bfwriter.write("======================================================================");
			bfwriter.newLine();
			bfwriter.write("agent_bill_id=" + agent_bill_id + ";pay_amt=" + pay_amt + ";agent_bill_time="+ agent_bill_time);
			bfwriter.newLine();
//		 	bfwriter.write("key=" + key);
			bfwriter.newLine();
			bfwriter.write("URL=" + res);
			bfwriter.newLine();
			bfwriter.write("---------------------------------------------------------");
			bfwriter.newLine();
			bfwriter.write("IP=" + request.getRemoteAddr());
			bfwriter.newLine();
			bfwriter.write("=======================================================================");
			bfwriter.newLine();
			bfwriter.flush();
			bfwriter.close();
			fwriter.close();


		// System.out.println("================================res:"+res);
		
	}




	
	
}