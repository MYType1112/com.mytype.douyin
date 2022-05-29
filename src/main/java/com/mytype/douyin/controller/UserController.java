package com.mytype.douyin.controller;


import com.alibaba.fastjson.JSON;
import com.mytype.douyin.entity.Response;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.UserLoginResponse;
import com.mytype.douyin.entity.UserResponse;
import com.mytype.douyin.service.FollowService;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.until.CommunityConstant;
import com.mytype.douyin.until.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/douyin/user")
public class UserController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Response register(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {
        Map<String, Object> map = userService.register(username, password);
        if (map.containsKey("userId")) {
            Map<String, Object> login = userService.login(username, password);
            int userId = (int) login.get("userId");
            return new UserLoginResponse(0,null, userId, (String) login.get("ticket"));

        } else {
            StringBuilder msg = new StringBuilder();
            if(map.containsKey("usernameMsg")){
                msg.append(map.get("usernameMsg"));
            }
            if(map.containsKey("passwordMsg")){
                msg.append(map.get("passwordMsg"));
            }
            return new UserLoginResponse(1, msg.toString());

        }
    }

    @ResponseBody
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Response login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {
        Map<String, Object> login = userService.login(username, password);
        if (login.containsKey("userId")) {
            int userId = (int) login.get("userId");
            return new UserLoginResponse(0, null,userId, (String) login.get("ticket"));

        } else {
            StringBuilder msg = new StringBuilder();
            if(login.containsKey("usernameMsg")){
                msg.append(login.get("usernameMsg"));
            }
            if(login.containsKey("passwordMsg")){
                msg.append(login.get("passwordMsg"));
            }
            return new UserLoginResponse(1,msg.toString());

        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Response UserInfo() {
//        Map<String, Object> InfoMap = userService.userInfo(token);
        User user = hostHolder.getUser();
        if (user==null) {
//            int userId = (int) InfoMap.get("userId");
            return new UserLoginResponse(1, "获取用户信息失败，请尝试重新登陆！");
        }
        return new UserResponse(0, "获取用户信息失败", user);
    }

}
