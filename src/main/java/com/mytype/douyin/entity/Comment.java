package com.mytype.douyin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Comment {

    @JsonProperty("id")
    private int id;
    private int userId;
    private User user;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    @JsonProperty("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
}

