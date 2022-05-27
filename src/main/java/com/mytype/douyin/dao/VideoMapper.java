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

    List<Video> selectVideos(int authorId, int offset, int limit);

    int updateCommentCount(int id, int commentCount);

//
//    // @Param注解用于给参数取别名,
//    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
//
//
//    DiscussPost selectDiscussPostById(int id);

}
