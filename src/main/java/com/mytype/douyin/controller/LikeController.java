package com.mytype.douyin.controller;

import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.Video;
import com.mytype.douyin.service.LikeService;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.service.VideoService;
import com.mytype.douyin.until.CommunityConstant;
import com.mytype.douyin.until.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/douyin/favorite")
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = "/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> like(
            @RequestParam(name = "video_id",defaultValue = "") String videoId) {
        User user = hostHolder.getUser();
        Map<String,Object> res = new HashMap<>();
//        Map<String, Object> infoMap = userService.userInfo(token);
        if(user==null) {
            res.put("status_code", 1);
            res.put("status_msg", "请先登录！");
            return res;
        }
        Video video = videoService.findVideoById(Integer.parseInt(videoId));
        // 点赞
        likeService.like(user.getUserId(), ENTITY_TYPE_VIDEO, Integer.parseInt(videoId), video.getAuthorId());

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
        User user = hostHolder.getUser();
        Map<String,Object> res = new HashMap<>();
//        Map<String, Object> infoMap = userService.userInfo(token);
        if(user==null) {
            res.put("status_code", 1);
            res.put("status_msg", "请先登录！");
            return res;
        }
        int favoriteCount = (int) likeService.findFavoriteCount(Integer.parseInt(userId), ENTITY_TYPE_VIDEO);

        List<Video> videoList = likeService.findFavorite(Integer.parseInt(userId), 0, favoriteCount);
        if (videoList.size() != 0) {
            for (Video video : videoList) {
//                user.setIsFollow(followService.hasFollowed((int) infoMap.get("userId"), ENTITY_TYPE_USER, Integer.parseInt(userId)));
                video.setIsFavorite(true);
            }
        }
        res.put("status_code", 0);
        res.put("status_msg", "获取点赞列表成功！");
        res.put("video_list", videoList);


        return res;
    }

}
