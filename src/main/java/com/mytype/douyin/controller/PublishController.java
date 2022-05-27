package com.mytype.douyin.controller;


import com.mytype.douyin.entity.Video;
import com.mytype.douyin.entity.VideoListResponse;
import com.mytype.douyin.until.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
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

//    @Value("${server.servlet.context-path}")
//    private String contextPath;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private HostHolder hostHolder;


//    @RequestMapping(path = "/action", method = RequestMethod.GET)
//    public String getUploadPage() {
//        return "/action";
//    }

    @ResponseBody
    @RequestMapping(path = "/action", method = RequestMethod.POST)
    public Response Action(
            @RequestParam(name = "data") MultipartFile videoFile,
            @RequestParam(name = "token", defaultValue = "") String token,
            @RequestParam(name = "title", defaultValue = "") String title) {

        if (videoFile == null) {
            return new Response(1, "没有文件！");
        }
        String fileName = videoFile.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return new Response(1, "文件格式不正确！");
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
//        File dest = new File(uploadPath + "/" + fileName);
        String basePath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/uploadVideo/";
        File dest = new File(basePath+fileName);
        try {
            // 存储文件
//            videoFile.transferTo(dest);
            videoFile.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            System.out.println(e.getMessage());
            return new Response(1, "上传文件失败,服务器发生异常!");
        }

        Map<String, Object> InfoMap = userService.userInfo(token);
        if (!InfoMap.containsKey("userId")) {
            return new Response(1, (String) InfoMap.get("errMsg"));
        }

//      127.0.0.1:8080/static/uploadVideo/video_name

        String videoUrl = domain + "/static/uploadVideo/" + fileName;
        videoService.uploadVideo(videoUrl, (int) InfoMap.get("userId"), "", title);

        return new Response(0, "上传成功！");
    }

    @ResponseBody
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public Response List(
            @RequestParam(name = "token", defaultValue = "") String token,
            @RequestParam(name = "user_id", defaultValue = "") String userId) {

        Map<String, Object> infoMap = userService.userInfo(token);
        if(infoMap.containsKey("errMsg")){
            return new VideoListResponse(1, (String) infoMap.get("errMsg"),null);
        }
        List<Video> videos = videoService.GetVideosByUserId(Integer.parseInt(userId));
        return new VideoListResponse(0, null,videos);
    }

}
