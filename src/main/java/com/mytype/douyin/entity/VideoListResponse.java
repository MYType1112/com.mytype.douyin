package com.mytype.douyin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class VideoListResponse extends Response{


    @JsonProperty("video_list")
    List<Video> VideoList;

    public VideoListResponse(int statusCode,String statusMsg, List<Video> videoList) {
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
        this.VideoList = videoList;
    }

}
