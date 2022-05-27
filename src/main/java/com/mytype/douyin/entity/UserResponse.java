package com.mytype.douyin.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserResponse extends Response{
    private User user;

    public UserResponse(int statusCode,String statusMsg, User user) {
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
        this.user = user;
    }

    public UserResponse(int statusCode, User user) {
        this.setStatusCode(statusCode);
        this.user = user;
    }
}
