package com.uzykj.mall.util.pay.wx;

import com.uzykj.mall.util.DateUtil;
import com.uzykj.mall.util.HttpUtil;
import com.uzykj.mall.util.pay.wx.util.WxpayUtil;
import com.uzykj.mall.util.pay.ali.AlipayConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信参数配置类
 *
 * @author xu
 * @Date 2018-10-20
 */
public class WxpayConfig {
    private static Logger logger = LogManager.getLogger(WxpayConfig.class.getName());
    private static final String FUNCNAME = "[weixin pay config]";

    public static String APP_ID;// 服务应用ID
    public static String APP_SECRET;// 服务应用密匙
    public static String MCH_ID;// 商户号
    public static String TOKEN;// 配置TOKEN
    public static String API_KEY;// API密钥
    public static String SIGN_TYPE;// 签名加密方式
    public static String CERT_PATH;// 微信支付证书
    public static String SM_NOTIFYURL; // 扫码支付回调
    public static String H5_NOTIFYURL; // H5支付回调

    static {
        Properties prop = new Properties();
        try {
            String path = AlipayConfig.class.getClassLoader().getResource("weixin.properties").getPath();
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);
            APP_ID = prop.getProperty("APP_ID").toString();
            APP_SECRET = prop.getProperty("APP_SECRET").toString();
            TOKEN = prop.getProperty("TOKEN").toString();
            MCH_ID = prop.getProperty("MCH_ID").toString();
            API_KEY = prop.getProperty("API_KEY").toString();
            SIGN_TYPE = prop.getProperty("SIGN_TYPE").toString();
            CERT_PATH = prop.getProperty("CERT_PATH").toString();
            SM_NOTIFYURL = prop.getProperty("SM_NOTIFYURL").toString();
            H5_NOTIFYURL = prop.getProperty("H5_NOTIFYURL").toString();
        } catch (IOException e) {
            logger.error(FUNCNAME, e);
        }
    }

    /**
     * 微信基础接口地址
     */
    // 获取token接口(GET)
    public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // oauth2授权接口(GET)
    public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    // 菜单创建接口（POST）
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 菜单查询（GET）
    public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    // 菜单删除（GET）
    public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
     * 微信支付接口地址
     */
    // 微信支付统一接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 微信退款接口(POST)
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 订单查询接口(POST)
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 关闭订单接口(POST)
    public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // 退款查询接口(POST)
    public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    // 接口调用上报接口(POST)
    public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

    /**
     * 基础参数
     *
     * @param packageParams
     */
    public static void commonParams(SortedMap<Object, Object> packageParams) {
        // 账号信息
        String appid = WxpayConfig.APP_ID; // appid
        String mch_id = WxpayConfig.MCH_ID; // 商业号
        // 生成随机字符串
        String currTime = DateUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = WxpayUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        packageParams.put("appid", appid);// 公众账号ID
        packageParams.put("mch_id", mch_id);// 商户号
        packageParams.put("nonce_str", nonce_str);// 随机字符串
    }

    /**
     * 该接口主要用于扫码原生支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX)，减小二维码数据量，提升扫描速度和精确度
     *
     * @param urlCode
     */
    public static void shorturl(String urlCode) {
        try {
            String key = WxpayConfig.API_KEY; // key
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("long_url", urlCode);// URL链接
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.SHORT_URL, requestXML);
            Map map = WxpayUtil.doXMLParse(resXml);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("return_code");
                if ("SUCCESS".equals(resultCode)) {
                    urlCode = (String) map.get("short_url");
                }
            }
        } catch (Exception e) {
            logger.error(FUNCNAME + "short url", e);
        }
    }


}
