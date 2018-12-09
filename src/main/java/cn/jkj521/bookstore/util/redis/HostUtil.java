package cn.jkj521.bookstore.util.redis;

import javax.servlet.ServletRequest;

public class HostUtil {


	public static String host = "http://localhost:8080/";
	//public static String host = "http://www.jkj521.cn/";
	public static String host(ServletRequest request){
		try {
			//String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
			return basePath;
		} catch (Exception e) {
			e.printStackTrace();
			return "cuole";
		}

	}
}
