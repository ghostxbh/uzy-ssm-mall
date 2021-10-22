package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.Address;
import com.uzykj.mall.entity.UpResult;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.entity.enums.ImageTypeEnum;
import com.uzykj.mall.entity.enums.ProductImageStoreEnum;
import com.uzykj.mall.service.AddressService;
import com.uzykj.mall.service.UserService;
import com.uzykj.mall.util.FileUtil;
import com.uzykj.mall.util.qiniu.QiniuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class ForeUserController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @Value("${storeService.use}")
    private String storeUse;
    @Value("${storeService.local.local_file_path}")
    private String localFilePath;
    @Value("${storeService.local.local_file_prefix}")
    private String localFileUrl;

    //转到前台-用户详情页
    @GetMapping("userDetails")
    public String goToUserDetail(HttpSession session, Map<String, Object> map) {
        User user = (User) session.getAttribute("USER_SESSION");
        map.put("user", user);

        // 获取用户所在地区级地址
        String districtAddressId = user.getUser_address().getAddress_areaId();
        Address districtAddress = addressService.get(districtAddressId);
        Address cityAddress = addressService.get(districtAddress.getAddress_regionId().getAddress_areaId());
        List<Address> addressList = addressService.getRoot();
        List<Address> cityList = addressService.getList(null, cityAddress.getAddress_regionId().getAddress_areaId());
        List<Address> districtList = addressService.getList(null, cityAddress.getAddress_areaId());

        map.put("addressList", addressList);
        map.put("cityList", cityList);
        map.put("districtList", districtList);
        map.put("addressId", cityAddress.getAddress_regionId().getAddress_areaId());
        map.put("cityAddressId", cityAddress.getAddress_areaId());
        map.put("districtAddressId", districtAddressId);
        return "fore/userDetails";

    }

    //前台-用户更换头像
    @ResponseBody
    @PostMapping("user/uploadUserHeadImage")
    public String uploadUserHeadImage(@RequestParam MultipartFile file, HttpSession session) {
        JSONObject object = new JSONObject();
        if (!file.isEmpty()) {
            try {
                String originalFileName = file.getOriginalFilename();
                if (storeUse.equals(ProductImageStoreEnum.qiniu.toString())) {
                    UpResult upload = QiniuUtil.upload(file.getInputStream(), originalFileName, QiniuUtil.MALL_ZONE);
                    if (upload != null) {
                        log.info("七牛云上传路径：" + upload.zoneName + upload.fileName);
                        String fileUrl = QiniuUtil.getFileUrl(upload.fileName, QiniuUtil.MALL_DOMAIN);
                        object.put("success", true);
                        object.put("fileUrl", fileUrl);
                    }
                } else if (storeUse.equals(ProductImageStoreEnum.local.toString())) {
                    // 转存文件
                    assert originalFileName != null;
                    String fileName = FileUtil.generNewFileName(originalFileName);
                    String filePath = FileUtil.generLocalFilePath(session, localFilePath, ImageTypeEnum.userProfilePicture.toString());
                    log.info("文件本地上传路径：" + filePath + fileName);
                    FileUtil.createDirectory(filePath);
                    file.transferTo(new File(filePath + fileName));
                    String fileUrl = FileUtil.generFileUrl(localFileUrl, ImageTypeEnum.userProfilePicture.toString(), fileName);
                    object.put("success", true);
                    object.put("fileUrl", fileUrl);
                }
            } catch (IOException e) {
                log.error("文件上传失败！", e);
            }
        }
        return object.toJSONString();
    }

    //前台-用户详情更新
    @PostMapping("user/update")
    public String userUpdate(HttpSession session, Map<String, Object> map, HttpServletResponse response,
                             @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
                             @RequestParam(value = "user_realname") String user_realname  /*真实姓名*/,
                             @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
                             @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
                             @RequestParam(value = "user_address") String user_address  /*用户所在地 */,
                             @RequestParam(value = "user_profile_picture_src", required = false) String user_profile_picture_src /* 用户头像*/,
                             @RequestParam(value = "user_password") String user_password/* 用户密码 */) throws ParseException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        if (user_profile_picture_src != null && user_profile_picture_src.equals("")) {
            user_profile_picture_src = null;
        }
        User userUpdate = new User()
                .setUser_id(userId)
                .setUser_nickname(new String(user_nickname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                .setUser_realname(new String(user_realname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_profile_picture_src(user_profile_picture_src)
                .setUser_password(user_password);

        if (userService.update(userUpdate)) {
            log.info("修改成功!跳转到用户详情页面");
            return "redirect:/userDetails";
        }
        throw new RuntimeException();
    }
}
