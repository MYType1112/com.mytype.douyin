package com.mytype.douyin.controller;

import com.mytype.douyin.entity.User;
import com.mytype.douyin.service.LikeService;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.until.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/douyin/favorite")
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> like(
            @RequestParam(name = "user_id",defaultValue = "") String userId,
            @RequestParam(name = "token",defaultValue = "") String token,
            @RequestParam(name = "video_id",defaultValue = "") String videoId,
            @RequestParam(name = "action_type",defaultValue = "") String actionType
//          int entityType, int entityId, int entityUserId, int postId
           ) {
//        User user = hostHolder.getUser();
        Map<String,Object> res = new HashMap<>();
        Map<String, Object> infoMap = userService.userInfo(token);
        if(infoMap.containsKey("errMsg")) {
            res.put("status_code", 1);
            res.put("status_msg", infoMap.get("errMsg"));
            return res;
        }
        User user = (User) infoMap.get("user");

        // 点赞
        likeService.like(user.getUserId(), ENTITY_TYPE_VIDEO, Integer.parseInt(videoId), 0);

//        // 数量
//        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_VIDEO, Integer.parseInt(videoId));
//        // 状态
//        int likeStatus = likeService.findEntityLikeStatus(user.getUserId(), ENTITY_TYPE_VIDEO, Integer.parseInt(videoId));
//        // 返回的结果
        res.put("status_code", 0);
        res.put("status_msg", "点赞成功！");


        return res;
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(name = "user_id",defaultValue = "") String userId,
            @RequestParam(name = "token",defaultValue = "") String token
    ) {
//        User user = hostHolder.getUser();
        Map<String,Object> res = new HashMap<>();
        Map<String, Object> infoMap = userService.userInfo(token);
        if(infoMap.containsKey("errMsg")) {
            res.put("status_code", 1);
            res.put("status_msg", infoMap.get("errMsg"));
            return res;
        }
        res.put("status_code", 0);
        res.put("status_msg", "获取点赞列表暂未实现！");


        return res;
    }

}
