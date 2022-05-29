package com.mytype.douyin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@Data
public class User {

    @JsonProperty("id")
    private int userId;
    @JsonProperty("name")
    private String username;
    private String password;
    private String salt;
    @JsonProperty("follow_count")
    private int followCount;
    @JsonProperty("follower_count")
    private int followerCount;
    @JsonProperty("total_favorited")
    private int totalFavorited;
    @JsonProperty("favorite_count")
    private long favoriteCount;
    private Date createTime;
    private String avatar;
    private String signature;
    @JsonProperty("background_image")
    private String backgroundImage;
    @JsonProperty("is_follow")
    private boolean isFollow;

    public void setIsFollow(boolean follow) {
        this.isFollow = follow;
    }

}
