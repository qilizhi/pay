package com.tencent.business;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSONObject;

import org.xml.sax.SAXException;

import com.tencent.PayInfo;
import com.tencent.WXPay;
import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;
import com.tencent.common.XMLParser;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.util.PropertiesUtil;


public class ScanPayBusinessTest {
	 private static String key = "0fbe59a7fef70d2e457266f5586f84b6";

	   	//微信分配的公众号ID（开通公众号之后可以获取到）
	   	private static String appID = "wxee3e6d3312bc7802";

	   	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	   	private static String mchID = "1467128202";

	   	//受理模式下给子商户分配的子商户号
	   	private static String subMchID = "";

	   	//HTTPS证书的本地路径
	   	private static String certLocalPath = "D:\\apiclient_cert.p12";

	   	//HTTPS证书密码，默认密码等于商户号MCHID
	   	private static String certPassword = "1467128202";
	   	private static String notify_url="";
public static void main(String[] args) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, IllegalAccessException, ParserConfigurationException, SAXException {
	
	String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	System.out.println("web init ... ");    
    //系统的初始化工作    
    //--------------------------------------------------------------------
    //第一步：初始化SDK（只需全局初始化一次即可）
    //--------------------------------------------------------------------
    System.out.println("WXPay  init ing....");
    PropertiesUtil p=PropertiesUtil.getInstance();
    key= p.getValue("weixin_key");
    appID=p.getValue("weixin_appID");
    mchID=p.getValue("weixin_mchID");
    certLocalPath= p.getValue("weixin_certLocalPath");
    certPassword=p.getValue("weixin_mchID");
    subMchID=p.getValue("weixin_subMchID");
    notify_url=p.getValue("weixin_notify_url");
    WXPay.initSDKConfiguration(
                //签名算法需要用到的秘钥
               key,
                //公众账号ID，成功申请公众账号后获得
               appID,
                //商户ID，成功申请微信支付功能之后通过官方发出的邮件获得
               mchID ,
                //子商户ID，受理模式下必填；
               subMchID,
                //HTTP证书在服务器中的路径，用来加载证书用
                certLocalPath,
                //HTTP证书的密码，默认等于MCHID
              certPassword,
              notify_url
        );
    System.out.println("WXPay  init success");
    System.out.println("weixin configure init:"+  Configure.getAppid()+":"
    +Configure.getCertLocalPath()+":"
    +Configure.getCertPassword()+":"
    +Configure.getKey()+":"
    +Configure.getMchid()+":"
    +Configure.getSubMchid());
/*  //--------------------------------------------------------------------
    //第二步：准备好提交给API的数据(scanPayReqData)
    //--------------------------------------------------------------------
    //1）创建一个可以用来生成数据的bridge，这里用的是一个专门用来测试用的Bridge，商户需要自己实现
        BridgeForScanPayBusinessTest bridge = new BridgeForScanPayBusinessTest();
        //2）从bridge里面拿到数据，构建提交被扫支付API需要的数据对象
        ScanPayReqData scanPayReqData = new ScanPayReqData(
                //这个是扫码终端设备从用户手机上扫取到的支付授权号，有效期是1分钟
                bridge.getAuthCode(),
                //要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
                bridge.getBody(),
                //支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
                bridge.getAttach(),
                //商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
                bridge.getOutTradeNo(),
                //订单总金额，单位为“分”，只能整数
                bridge.getTotalFee(),
                //商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
                bridge.getDeviceInfo(),
                //订单生成的机器IP
                bridge.getSpBillCreateIP(),
                //订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
                bridge.getTimeStart(),
                //订单失效时间，格式同上
                bridge.getTimeExpire(),
                //商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
                bridge.getGoodsTag()
        );

        //--------------------------------------------------------------------
        //第三步：准备好一个用来处理各种结果分支的监听器(resultListener)
        //--------------------------------------------------------------------
        //这个是Demo里面写的一个默认行为，商户需要根据自身需求来进行完善
        DefaultScanPayBusinessResultListener resultListener = new DefaultScanPayBusinessResultListener();

        //--------------------------------------------------------------------
        //搞定以上三步后执行业务逻辑
        //--------------------------------------------------------------------
        WXPay.doScanPayBusiness(scanPayReqData,resultListener);*/
	PayInfo pay=PayInfo.createPayInfo("192.168.0.23","qilizhi","P20524552114","T2016050214545","actach",66);
	String sign=Signature.getSign(pay);
	pay.setSign(sign);

	HttpsRequest http=new HttpsRequest();
	
	//System.out.append(xml);
	String result=http.sendPost(url,pay);
	Map<String,Object>map=XMLParser.getMapFromXML(result);
	String jsonr=JSONObject.fromObject(map).toString();
	System.out.println(result);
	System.out.println(jsonr);
	 //HTTP请求器
  
	
}

}
