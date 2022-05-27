package com.mytype.douyin.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLoginResponse extends Response{
//    Response Response;

    @JsonProperty("user_id")
    private int UserId;
    @JsonProperty("token")
    private String Token;

    public UserLoginResponse(){

    }
//    int statusCode,String statusMsg , User user
    public UserLoginResponse(int statusCode,String statusMsg){
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
    }

    public UserLoginResponse(int statusCode,String statusMsg, int userId, String token){
        this.setStatusCode(statusCode);
        this.setStatusMsg(statusMsg);
        UserId = userId;
        Token = token;
    }
}
