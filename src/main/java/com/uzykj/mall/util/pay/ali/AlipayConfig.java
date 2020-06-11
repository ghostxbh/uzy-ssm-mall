package com.uzykj.mall.util.pay.ali;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * 支付宝参数配置
 *
 * @author ghostxbh
 * @Date 2018-10-20
 */
public class AlipayConfig {
    private static Logger logger = LogManager.getLogger(AlipayConfig.class.getName());
    private static final String FUNCNAME = "[alipay config]";

    public static String pid;
    public static String appId;
    public static String notifyUrl;
    public static String returnUrl;
    public static String aliGateway;
    public static String alipayPublicKey;
    public static String merchantPublicKey;
    public static String merchantPrivateKey;

    public static final String paramType = "JSON";
    public static final String signType = "RSA2";
    public static final String charset = "UTF-8";

    static {
        Properties prop = new Properties();
        try {
            String path = AlipayConfig.class.getClassLoader().getResource("zhifubao.properties").getPath();
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);
            pid = prop.getProperty("pid");
            appId = prop.getProperty("app_id");
            merchantPrivateKey = prop.getProperty("merchant_private_key");
            merchantPublicKey = prop.getProperty("merchant_public_key");
            alipayPublicKey = prop.getProperty("alipay_public_key");
            aliGateway = prop.getProperty("alipay_gateway");
            notifyUrl = prop.getProperty("notify_url");
            returnUrl = prop.getProperty("return_url");
        } catch (Exception e) {
            logger.error(FUNCNAME, e);
        }
    }

    public AlipayConfig() {
    }

    /**
     * 单例实例
     */
    private static class SingleHolder {
        private static final AlipayClient ALIPAYCLIENT = new DefaultAlipayClient(aliGateway,
                appId, merchantPrivateKey, paramType, charset, alipayPublicKey, signType);
    }

    /**
     * 获取 alipay client
     */
    public static AlipayClient getAlipayClient() {
        return SingleHolder.ALIPAYCLIENT;
    }
}
