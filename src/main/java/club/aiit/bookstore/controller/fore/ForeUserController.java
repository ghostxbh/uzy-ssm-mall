package club.aiit.bookstore.controller.fore;

import club.aiit.bookstore.entity.Address;
import club.aiit.bookstore.entity.User;
import club.aiit.bookstore.service.AddressService;
import club.aiit.bookstore.service.UserService;
import com.alibaba.fastjson.JSONObject;
import club.aiit.bookstore.controller.BaseController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ForeUserController extends BaseController {
    @Resource(name = "addressService")
    private AddressService addressService;
    @Resource(name = "userService")
    private UserService userService;

    //转到前台天猫-用户详情页
    @RequestMapping(value = "userDetails", method = RequestMethod.GET)
    public String goToUserDetail(HttpSession session, Map<String, Object> map, HttpServletResponse response) {
        logger.info("检查用户是否登录");
        Object userId = checkUser(session);
        User user = null;
        if(userId!=null){
            logger.info("获取用户信息");
            user = userService.get(Integer.parseInt(userId.toString()));
            map.put("user", user);
        }
        logger.info("获取用户所在地区级地址");
        String districtAddressId = user.getUser_address().getAddress_areaId();
        Address districtAddress = addressService.get(districtAddressId);
        logger.info("获取市级地址信息");
        Address cityAddress = addressService.get(districtAddress.getAddress_regionId().getAddress_areaId());
        logger.info("获取其他地址信息");
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

    //前台天猫-用户更换头像
    @ResponseBody
    @RequestMapping(value = "user/uploadUserHeadImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String uploadUserHeadImage(@RequestParam MultipartFile file, HttpSession session
    ) {
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名：{}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/userProfilePicture/" + fileName;
        logger.info("文件上传路径：{}", filePath);
        JSONObject jsonObject = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传成功！");
            jsonObject.put("success", true);
            jsonObject.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败！");
            e.printStackTrace();
            jsonObject.put("success", false);
        }
        return jsonObject.toJSONString();
    }

    //前台天猫-用户详情更新
    @RequestMapping(value = "user/update", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String userUpdate(HttpSession session, Map<String, Object> map, HttpServletResponse response,
                             @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
                             @RequestParam(value = "user_realname") String user_realname  /*真实姓名*/,
                             @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
                             @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
                             @RequestParam(value = "user_address") String user_address  /*用户所在地 */,
                             @RequestParam(value = "user_profile_picture_src", required = false) String user_profile_picture_src /* 用户头像*/,
                             @RequestParam(value = "user_password") String user_password/* 用户密码 */
    ) throws ParseException, UnsupportedEncodingException {
        logger.info("检查用户是否登录");
        Object userId = checkUser(session);
        if (userId != null) {
            logger.info("获取用户信息");
            User user = userService.get(Integer.parseInt(userId.toString()));
            map.put("user", user);
        } else {
            try {
                response.sendRedirect("/bookstore/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("创建用户对象");
        if (user_profile_picture_src != null && user_profile_picture_src.equals("")) {
            user_profile_picture_src = null;
        }
        User userUpdate = new User()
                .setUser_id(Integer.parseInt(userId.toString()))
                .setUser_nickname(new String(user_nickname.getBytes("ISO8859-1"), "UTF-8"))
                .setUser_realname(new String(user_realname.getBytes("ISO8859-1"), "UTF-8"))
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_profile_picture_src(user_profile_picture_src)
                .setUser_password(user_password);
        logger.info("执行修改");
        if (userService.update(userUpdate)) {
            logger.info("修改成功!跳转到用户详情页面");
            return "redirect:/userDetails";
        }
        throw new RuntimeException();
    }
}
