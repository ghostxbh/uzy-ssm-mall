package com.uzykj.mall.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * 基控制器
 * @author ghostxbh
 * @date 2018-06-12
 */
public class BaseController {
    protected Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    //检查管理员权限
    protected Object checkAdmin(HttpSession session) {
        Object adminId = session.getAttribute("adminId");
        if (adminId == null) {
            logger.info("无管理权限，返回管理员登陆页");
            return null;
        }
        logger.info("权限验证成功，管理员ID：{}", adminId);
        return adminId;
    }

    //检查用户是否登录
    protected Object checkUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            logger.info("用户未登录");
            return null;
        }
        logger.info("用户已登录，用户ID：{}", userId);
        return userId;
    }
}
