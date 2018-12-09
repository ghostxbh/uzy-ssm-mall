package cn.jkj521.bookstore.controller.fore;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import cn.jkj521.bookstore.controller.BaseController;
import cn.jkj521.bookstore.entity.User;
import cn.jkj521.bookstore.service.UserService;
import cn.jkj521.bookstore.util.redis.JedisClient;
import cn.jkj521.bookstore.util.redis.SerializeUtil;
import cn.jkj521.bookstore.util.redis.SessionMap;
import cn.yunzhf.accounting.user.entity.UzUser;

/**
 * 前台天猫-登陆页
 */
@Controller
public class ForeLoginController extends BaseController {
	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	private JedisClient jedisClient;

	// 转到前台天猫-登录页
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String goToPage(HttpSession session, Map<String, Object> map) {
		logger.info("转到前台天猫-登录页");
		return "fore/loginPage";
	}

	// 登陆验证-ajax
	@ResponseBody
	@RequestMapping(value = "login/doLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String checkLogin(HttpSession session, @RequestParam String username, @RequestParam String password) {
		logger.info("用户验证登录");
		User user = userService.login(username, password);

		JSONObject jsonObject = new JSONObject();
		if (user == null) {
			logger.info("登录验证失败");
			jsonObject.put("success", false);
		} else {
			logger.info("登录验证成功,用户ID传入会话");
			session.setAttribute("userId", user.getUser_id());
			jsonObject.put("success", true);
		}
		return jsonObject.toJSONString();
	}

	// 退出当前账号
	/*@RequestMapping(value = "login/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		Object o = session.getAttribute("userId");
		if (o != null) {
			session.removeAttribute("userId");
			session.invalidate();
			logger.info("登录信息已清除，返回用户登录页");
		}
		return "redirect:/";
	}*/

	/**
	 * 查询Redis
	 * 
	 * @param sign
	 * @param session
	 * @param response
	 *            张智方 2018年9月28日
	 */
	@RequestMapping("getsign")
	public String getsign(String sign,HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		System.out.println(sign);
		byte[] bs = jedisClient.get(sign.getBytes());
		// System.out.println(bs);
		// String uid = jedisClient.get(sign);
		UzUser user = (UzUser) SerializeUtil.unserialize(bs);
		session.setAttribute("userId", user.getId().toString());
		session.setAttribute("user", user);
		session.setAttribute("sign", sign);
		session.setMaxInactiveInterval(18000);
		Map<String, HttpSession> sessionMap = SessionMap.getSessionMap();
		sessionMap.put(sign, session);
		return "redirect:/";
	}

	/**
	 * 退出登录,清除session
	 * 
	 * @param sign
	 * @param session
	 * @param response
	 * @return 张智方 2018年9月29日
	 */
	@RequestMapping("logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader("sign");
		System.out.println(header);
		Map<String, HttpSession> sessionMap = SessionMap.getSessionMap();
		HttpSession httpSession = sessionMap.get(header);
		if (httpSession != null) {
			Object o = httpSession.getAttribute("userId");
			if (o != null) {
				httpSession.removeAttribute("userId");
				httpSession.invalidate();
				logger.info("登录信息已清除，返回用户登录页");
				System.out.println("注销完成");
			}
		}
	}
	
}
