package com.mytype.douyin.service;


import com.mytype.douyin.dao.VideoMapper;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    public int uploadVideo(String playUrl, int authorId, String coverUrl, String title){
        Map<String, Object> map = new HashMap<>();

        Video video = new Video();

        video.setTitle(title);
        video.setPlayUrl(playUrl);
        video.setAuthorId(authorId);
        video.setCoverUrl(coverUrl);
        video.setUploadTime(new Date());

        return videoMapper.insertVideo(video);
    }

    public List<Video> GetVideosByUserId(int authorId, long latestTime, int limit){
        Date date = new Date(latestTime);//新建一个时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//你要转换成的时间格式,大小写不要变
        String Time = sdf.format(date);//转换你的时间
        return videoMapper.selectVideos(authorId, Time, 0, limit);
    }

    public Video findVideoById(int id) {
        return videoMapper.selectVideoById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return videoMapper.updateCommentCount(id, commentCount);
    }

}
