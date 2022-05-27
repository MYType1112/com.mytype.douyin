package com.mytype.douyin.controller;

import com.mytype.douyin.entity.*;
import com.mytype.douyin.service.*;
import com.mytype.douyin.until.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/douyin")
public class FeedController implements CommunityConstant {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;


    @ResponseBody
    @RequestMapping(path = "/feed", method = RequestMethod.GET)
    public FeedResponse Feed(
            @RequestParam(name = "latest_time",defaultValue = "") String latestTime,
            @RequestParam(name = "token",defaultValue = "") String token) {
        Map<String, Object> infoMap = userService.userInfo(token);
        Video[] videos = videoService.GetVideosByUserId(0).toArray(new Video[0]);

        if(infoMap.containsKey("errMsg")){
            return new FeedResponse(0,null,videos,new Date().getTime());
        }
        for(Video video:videos){
            // 数量
            long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_VIDEO, video.getId());
            // 状态
            int likeStatus = likeService.findEntityLikeStatus((Integer) infoMap.get("userId"), ENTITY_TYPE_VIDEO, video.getId());

            video.setFavoriteCount((int) likeCount);
            video.setIsFavorite(likeStatus==1);
            video.getAuthor().setIsFollow(followService.hasFollowed((int) infoMap.get("userId"), ENTITY_TYPE_USER, video.getAuthor().getUserId()));
        }
        return new FeedResponse(0,null, videos, new Date().getTime());
    }

    @ResponseBody
    @RequestMapping(path = "/comment/list/", method = RequestMethod.GET)
    public CommentListResponse GetComment(
            @RequestParam(name = "token",defaultValue = "") String token,
            @RequestParam(name = "video_id",defaultValue = "") String videoId){

        System.out.println(videoId);
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_VIDEO, Integer.parseInt(videoId), 0, 10);
        return new CommentListResponse(0,"",commentList);
    }

    @ResponseBody
    @RequestMapping(path = "/comment/action/", method = RequestMethod.POST)
    public Map<String,Object> SubmitComment(
            @RequestParam(name = "token",defaultValue = "") String token,
            @RequestParam(name = "video_id",defaultValue = "") String videoId,
            @RequestParam(name = "action_type",defaultValue = "") String actionType,
            @RequestParam(name = "comment_text",defaultValue = "") String commentText,
            @RequestParam(name = "comment_id",defaultValue = "") String commentId
    ){
        Map<String,Object> res = new HashMap<>();

        Map<String, Object> infoMap = userService.userInfo(token);
        if(infoMap.containsKey("errMsg")){
            res.put("status_code",1);
            res.put("status_msg",infoMap.get("errMsg"));
        }else{
            if(actionType.equals("1")){
                Comment comment = new Comment();
                comment.setUserId((Integer) infoMap.get("userId"));
                comment.setEntityType(ENTITY_TYPE_VIDEO);
                comment.setEntityId(Integer.parseInt(videoId));
//                comment.setUser(userService.findUserById(Integer.parseInt(userId)));
                comment.setContent(commentText);
                comment.setCreateDate(new Date());
                commentService.addComment(comment);
                comment.setUser((User) infoMap.get("user"));
                res.put("status_code",0);
                res.put("comment",comment);
            }else{
                Comment comment = commentService.findCommentById(Integer.parseInt(commentId));
                if(comment==null){
                    res.put("status_code",1);
                    res.put("status_msg","删除失败！");
                }
                commentService.deleteComment(ENTITY_TYPE_VIDEO, Integer.parseInt(commentId));
                res.put("status_code",0);
                res.put("status_msg","删除成功！");
            }

        }
        return res;
    }

}
