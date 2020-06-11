package com.uzykj.mall.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * http请求(这里用户获取订单url生成二维码)
 * 创建者 科帮网
 * 创建时间	2017年7月31日
 *
 */
public class HttpUtil {
	private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
	private final static String DEFAULT_ENCODING = "UTF-8";

	public static String postData(String urlStr, String data) {
		return postData(urlStr, data, null);
	}

	public static String postData(String urlStr, String data, String contentType) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(CONNECT_TIMEOUT);
			if (contentType != null)
				conn.setRequestProperty("content-type", contentType);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
			if (data == null)
				data = "";
			writer.write(data);
			writer.flush();
			writer.close();

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}
