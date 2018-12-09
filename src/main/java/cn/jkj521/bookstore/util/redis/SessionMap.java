package cn.jkj521.bookstore.util.redis;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class SessionMap {
	
	private static Map<String, HttpSession> sessionMap = new HashMap<>();

	public static Map<String, HttpSession> getSessionMap() {
		return sessionMap;
	}

	public static void setSessionMap(Map<String, HttpSession> sessionMap) {
		SessionMap.sessionMap = sessionMap;
	}
}
