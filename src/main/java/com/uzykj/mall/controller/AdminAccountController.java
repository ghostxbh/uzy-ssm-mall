package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.Admin;
import com.uzykj.mall.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * 后台管理-账户页
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminAccountController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/account")
    public String goToPage(HttpSession session, Map<String, Object> map) {
        Admin admin = (Admin) session.getAttribute("ADMIN_SESSION");
        map.put("admin", admin);
        return "admin/accountManagePage";
    }

    //管理员头像上传
    @ResponseBody
    @PostMapping("/uploadAdminHeadImage")
    public String uploadAdminHeadImage(@RequestParam MultipartFile file, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "static/images/upload/adminProfilePicture/" + fileName;
        JSONObject jsonObject = new JSONObject();
        try {
            file.transferTo(new File(filePath));
            Admin admin = (Admin) session.getAttribute("ADMIN_SESSION");
            Admin update = new Admin();
            update.setAdmin_id(admin.getAdmin_id())
                    .setAdmin_profile_picture_src(fileName);
            adminService.update(update);
            jsonObject.put("success", true);
            jsonObject.put("fileName", fileName);
        } catch (IOException e) {
            log.error("文件上传失败！", e);
            jsonObject.put("success", false);
        }
        return jsonObject.toJSONString();
    }

    //更新管理员信息
    @ResponseBody
    @PutMapping("/account/{admin_id}")
    public String updateAdmin(HttpSession session,
                              @RequestParam(required = false) String admin_nickname/*管理员昵称*/,
                              @RequestParam(required = false) String admin_password/*管理员当前密码*/,
                              @RequestParam(required = false) String admin_newPassword/*管理员新密码*/,
                              @RequestParam(required = false) String admin_profile_picture_src/*管理员头像路径*/,
                              @PathVariable("admin_id") String admin_id/*管理员编号*/) {
        Admin admin = (Admin) session.getAttribute("ADMIN_SESSION");
        JSONObject jsonObject = new JSONObject();
        Admin putAdmin = new Admin();
        putAdmin.setAdmin_id(Integer.valueOf(admin_id));
        putAdmin.setAdmin_nickname(admin_nickname);

        if (admin_password != null && !admin_password.equals("") && admin_newPassword != null && !admin_newPassword.equals("")) {
            if (adminService.login(admin.getAdmin_name(), admin_password) != null) {
                putAdmin.setAdmin_password(admin_newPassword);
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", "原密码输入有误！");
                return jsonObject.toJSONString();
            }
        }
        if (admin_profile_picture_src != null && !admin_profile_picture_src.equals("")) {
            putAdmin.setAdmin_profile_picture_src(admin_profile_picture_src.substring(admin_profile_picture_src.lastIndexOf("/") + 1));
        }

        boolean yn = adminService.update(putAdmin);
        if (yn) {
            jsonObject.put("success", true);
            session.removeAttribute("ADMIN_SESSION");
            session.invalidate();
        } else {
            jsonObject.put("success", false);
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }
}
