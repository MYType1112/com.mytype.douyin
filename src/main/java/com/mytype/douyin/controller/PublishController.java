package com.mytype.douyin.controller;


import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.Video;
import com.mytype.douyin.entity.VideoListResponse;
import com.mytype.douyin.until.CommunityUtil;
import com.mytype.douyin.until.HostHolder;
import com.mytype.douyin.until.VideoImage;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.mytype.douyin.entity.Response;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/douyin/publish")
public class PublishController {

    private static final Logger logger = LoggerFactory.getLogger(PublishController.class);

    @Value("${douyin.path.upload}")
    private String uploadPath;

    @Value("${douyin.path.domain}")
    private String domain;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(path = "/action", method = RequestMethod.POST)
    public Response Action(
            @RequestParam(name = "data") MultipartFile videoFile,
            @RequestParam(name = "title", defaultValue = "") String title) throws FrameGrabber.Exception {

//        Map<String, Object> InfoMap = userService.userInfo(token);
        User user = hostHolder.getUser();
        if (user==null) {
            return new Response(1, "???????????????");
        }
        if (videoFile == null) {
            return new Response(1, "???????????????");
        }
        String fileName = videoFile.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return new Response(1, "????????????????????????");
        }

        // ?????????????????????
        fileName = CommunityUtil.generateUUID() + suffix;
        // ??????????????????????????? /D:/work/workspace/douyin/target/classes/static/uploadVideo/
        String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/uploadVideo/";
        File dest = new File(basePath+fileName);

        try {
            // ????????????
            videoFile.transferTo(dest);
        } catch (IOException e) {
            logger.error("??????????????????: " + e.getMessage());
            System.out.println(e.getMessage());
            return new Response(1, "??????????????????,?????????????????????!");
        }

        String imagePath = VideoImage.randomGrabberFFmpegImage((basePath + fileName).substring(1), 2);

//      127.0.0.1:8080/static/uploadVideo/video_name

        String[] split = imagePath.split("/");
        String imageName = split[split.length-1];

        String videoUrl = domain + "/static/uploadVideo/" + fileName;
        String coverUrl = domain + "/static/uploadVideo/" + imageName;
        videoService.uploadVideo(videoUrl, user.getUserId(), coverUrl, title);

        return new Response(0, "???????????????");
    }

    @ResponseBody
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public Response List(
            @RequestParam(name = "user_id", defaultValue = "") String userId) {

//        Map<String, Object> infoMap = userService.userInfo(token);
        long currentTime = new Date().getTime();
        User user = hostHolder.getUser();
        if(user==null){
            return new VideoListResponse(1, "???????????????",null);
        }
        List<Video> videos = videoService.GetVideosByUserId(Integer.parseInt(userId), currentTime,20);
        return new VideoListResponse(0, "?????????????????????????????????", videos);
    }

}
