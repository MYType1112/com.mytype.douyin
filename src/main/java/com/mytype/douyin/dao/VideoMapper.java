package com.mytype.douyin.dao;

import com.mytype.douyin.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {

    int insertVideo(Video video);

    int selectVideoRows(@Param("userId") int userId);

    Video selectVideoById(int id);

    List<Video> selectVideos(int authorId, String latestTime, int offset, int limit);

    int updateCommentCount(int id, int commentCount);

}
