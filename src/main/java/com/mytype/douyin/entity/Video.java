package com.mytype.douyin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class Video {

    private int id;
    @JsonProperty("author_id")
    private int authorId;
    private User author;

    @JsonProperty("play_url")
    private String playUrl;
    @JsonProperty("cover_url")
    private String coverUrl;
    @JsonProperty("favorite_count")
    private int favoriteCount;
    @JsonProperty("comment_count")
    private int commentCount;
    @JsonProperty("is_favorite")
    private boolean isFavorite;
    @JsonProperty("uploadTime")
    private Date uploadTime;
    private String title;

    public void setIsFavorite(boolean like) {
        this.isFavorite = like;
    }
}
