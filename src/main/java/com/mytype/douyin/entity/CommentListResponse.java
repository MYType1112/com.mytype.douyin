package com.mytype.douyin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentListResponse extends Response{


    @JsonProperty("comment_list")
    List<Comment> CommentList;

    public CommentListResponse(int statusCode,String statusMsg, List<Comment> commentList){
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
        CommentList = commentList;
    }

}
