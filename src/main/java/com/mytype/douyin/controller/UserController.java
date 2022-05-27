package com.mytype.douyin.controller;


import com.alibaba.fastjson.JSON;
import com.mytype.douyin.entity.Response;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.UserLoginResponse;
import com.mytype.douyin.entity.UserResponse;
import com.mytype.douyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/douyin/user")
public class UserController {

    @Autowired
    private UserService userService;

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
    public Response UserInfo(
            @RequestParam(name = "token") String token) {
        Map<String, Object> InfoMap = userService.userInfo(token);
        if (InfoMap.containsKey("userId")) {
//            int userId = (int) InfoMap.get("userId");
            return new UserResponse(0, (User) InfoMap.get("user"));

        } else {
            StringBuilder msg = new StringBuilder();
            if(InfoMap.containsKey("errMsg")){
                msg.append(InfoMap.get("usernameMsg"));
            }
            return new UserLoginResponse(1,msg.toString());
        }
    }

}
