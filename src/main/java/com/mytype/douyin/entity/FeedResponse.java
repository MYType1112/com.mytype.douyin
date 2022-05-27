package com.mytype.douyin.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FeedResponse extends Response{

//    Response response;

    @JsonProperty("video_list")
    Video[] VideoList;
    @JsonProperty("next_time")
    long nextTime;

    public FeedResponse(int statusCode,String statusMsg, Video[] videoList, long nextTime){
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
        VideoList = videoList;
        this.nextTime = nextTime;
    }

}
