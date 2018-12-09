package club.aiit.bookstore.controller.fore;

import java.util.Map;

import javax.annotation.Resource;

import javax.servlet.http.HttpSession;

import club.aiit.bookstore.entity.User;
import club.aiit.bookstore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import club.aiit.bookstore.controller.BaseController;


/**
 * 前台天猫-登陆页
 */
@Controller
public class ForeLoginController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;



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
	@RequestMapping(value = "login/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		Object o = session.getAttribute("userId");
		if (o != null) {
			session.removeAttribute("userId");
			session.invalidate();
			logger.info("登录信息已清除，返回用户登录页");
		}
		return "redirect:/";
	}

}
