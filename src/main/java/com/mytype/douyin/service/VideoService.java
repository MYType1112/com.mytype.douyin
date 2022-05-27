package com.mytype.douyin.service;


import com.mytype.douyin.dao.VideoMapper;
import com.mytype.douyin.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Video> GetVideosByUserId(int authorId){
        return videoMapper.selectVideos(authorId, 0, 30);
    }

    public int updateCommentCount(int id, int commentCount) {
        return videoMapper.updateCommentCount(id, commentCount);
    }

}
