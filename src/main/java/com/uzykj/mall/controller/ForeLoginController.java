package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * 前台-登陆页
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class ForeLoginController {
    @Autowired
    private UserService userService;

    // 转到前台-登录页
    @GetMapping()
    public String goToPage() {
        return "fore/loginPage";
    }

    // 登陆验证-ajax
    @ResponseBody
    @PostMapping("/doLogin")
    public String checkLogin(HttpSession session, @RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);

        if (user != null) {
            session.setAttribute("USER_SESSION", user);
            session.setAttribute("USER_ID", user.getUser_id());
            jsonObject.put("success", true);
        }
        return jsonObject.toJSONString();
    }

    // 退出当前账号
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        if (user != null) {
            session.removeAttribute("USER_SESSION");
            session.removeAttribute("USER_ID");
            session.invalidate();
        }
        return "redirect:/";
    }

}
