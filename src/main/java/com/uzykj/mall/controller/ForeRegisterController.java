package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.Address;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.service.AddressService;
import com.uzykj.mall.service.UserService;
import com.uzykj.mall.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/register")
public class ForeRegisterController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    //转到前台-用户注册页
    @GetMapping()
    public String goToPage(Map<String, Object> map) {
        String addressId = "110000";
        String cityAddressId = "110100";
        // 获取地址TREE
        List<Address> addressList = addressService.getRoot();
        List<Address> cityAddress = addressService.getList(null, addressId);
        List<Address> districtAddress = addressService.getList(null, cityAddressId);

        map.put("addressList", addressList);
        map.put("cityList", cityAddress);
        map.put("districtList", districtAddress);
        map.put("addressId", addressId);
        map.put("cityAddressId", cityAddressId);
        return "fore/register";
    }

    //前台-用户注册-ajax
    @ResponseBody
    @PostMapping("/doRegister")
    public String register(
            @RequestParam(value = "user_name") String user_name  /*用户名 */,
            @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
            @RequestParam(value = "user_password") String user_password  /*用户密码*/,
            @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
            @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
            @RequestParam(value = "user_address") String user_address  /*用户所在地 */
    ) throws ParseException {
        // 验证用户名是否存在
        Integer count = userService.getTotal(new User().setUser_name(user_name));
        if (count > 0) {
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "用户名已存在，请重新输入！");
            return object.toJSONString();
        }

        String encode = Md5Util.MD5Encode(user_password, "UTF-8");
        User user = new User()
                .setUser_name(user_name)
                .setUser_nickname(user_nickname)
                .setUser_password(encode)
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_homeplace(new Address().setAddress_areaId("130000"));

        if (userService.add(user)) {
            JSONObject object = new JSONObject();
            object.put("success", true);
            return object.toJSONString();
        } else {
            throw new RuntimeException();
        }
    }
}
