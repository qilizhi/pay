package com.tencent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tencent.common.Configure;
import com.util.PropertiesUtil;
    
public class InitListener implements ServletContextListener {    
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
    public void contextDestroyed(ServletContextEvent sce) {    
        System.out.println("web exit ... ");   
     
    }    
    
    public  void  contextInitialized(ServletContextEvent sce) {    
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
    }    
}    