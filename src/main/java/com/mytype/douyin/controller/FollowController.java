package com.mytype.douyin.controller;

import com.mytype.douyin.entity.User;
import com.mytype.douyin.service.FollowService;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.until.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/douyin")
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/relation/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> follow(
            @RequestParam(name = "token", defaultValue = "") String token,
            @RequestParam(name = "to_user_id", defaultValue = "") String entityId,
            @RequestParam(name = "action_type", defaultValue = "") String actionType
//            int entityType, int entityId
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> infoMap = userService.userInfo(token);
        if (infoMap.containsKey("errMsg")) {
            res.put("status_code", 1);
            res.put("status_msg", infoMap.get("errMsg"));
            return res;
        }
        User user = (User) infoMap.get("user");

        if (actionType.equals("1")) {
            followService.follow(user.getUserId(), ENTITY_TYPE_USER, Integer.parseInt(entityId));
            res.put("status_code", 0);
            res.put("status_msg", "已关注!");
            return res;
        } else {
            followService.unfollow(user.getUserId(), ENTITY_TYPE_USER, Integer.parseInt(entityId));
            res.put("status_code", 0);
            res.put("status_msg", "已取消关注!");
            return res;
        }
    }

    @RequestMapping(path = "/relation/follow/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getFollowees(
            @RequestParam(name = "user_id", defaultValue = "") String userId,
            @RequestParam(name = "token", defaultValue = "") String token
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> infoMap = userService.userInfo(token);
        if (infoMap.containsKey("errMsg")) {
            res.put("status_code", 1);
            res.put("status_msg", infoMap.get("errMsg"));
            return res;
        }
        int followeeCount = (int) followService.findFolloweeCount(Integer.parseInt(userId), ENTITY_TYPE_USER);

        List<User> userList = followService.findFollowees(Integer.parseInt(userId), 0, followeeCount);
        if (userList != null) {
            for (User user : userList) {
//                user.setIsFollow(followService.hasFollowed((int) infoMap.get("userId"), ENTITY_TYPE_USER, Integer.parseInt(userId)));
                user.setIsFollow(true);
            }
        }
        res.put("status_code", 0);
        res.put("status_msg", "获取关注列表成功！");
        res.put("user_list", userList);

        return res;
    }

    @RequestMapping(path = "/relation/follower/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getFollowers(
            @RequestParam(name = "user_id", defaultValue = "") String userId,
            @RequestParam(name = "token", defaultValue = "") String token
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> infoMap = userService.userInfo(token);
        if (infoMap.containsKey("errMsg")) {
            res.put("status_code", 1);
            res.put("status_msg", infoMap.get("errMsg"));
            return res;
        }

        int followerCount = (int) followService.findFollowerCount(ENTITY_TYPE_USER, Integer.parseInt(userId));

//        List<Map<String, Object>> userList = followService.findFollowers(Integer.parseInt(userId), 0, followerCount);
        List<User> userList = followService.findFollowers(Integer.parseInt(userId), 0, followerCount);

        if (userList != null) {
//            for (Map<String, Object> map : userList) {
//                User u = (User) map.get("user");
//                u.setIsFollow(followService.hasFollowed((int) infoMap.get("userId"), ENTITY_TYPE_USER, Integer.parseInt(userId)));
//            }
            for (User user : userList) {
                user.setIsFollow(followService.hasFollowed((int) infoMap.get("userId"), ENTITY_TYPE_USER, Integer.parseInt(userId)));
            }
        }
        res.put("status_code", 0);
        res.put("status_msg", "获取粉丝列表成功！");
        res.put("user_list", userList);

        return res;
    }
}

