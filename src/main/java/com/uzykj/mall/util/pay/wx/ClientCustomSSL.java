package com.uzykj.mall.util.pay.wx;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * 退款认证
 * 创建者 科帮网
 * 创建时间	2017年7月31日
 *
 */
public class ClientCustomSSL {
	 public static String doRefund(String url,String data) throws Exception {  
        /** 
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的 
         */  
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");  
        File certfile = ResourceUtils.getFile("classpath:cert"+ Constants.SF_FILE_SEPARATOR + WxpayConfig.CERT_PATH);
        FileInputStream instream = new FileInputStream(certfile);
        try {  
            keyStore.load(instream, WxpayConfig.MCH_ID.toCharArray());
        } finally {  
            instream.close();  
        }  
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, WxpayConfig.MCH_ID.toCharArray())
                .build();  
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,  
                new String[] { "TLSv1" },  
                null,  
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)  
                .build();
		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				@SuppressWarnings("rawtypes")
                HttpEntity entity = (HttpEntity) response.getEntity();
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume((org.apache.http.HttpEntity) entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}  
}
