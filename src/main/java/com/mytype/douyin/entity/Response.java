package com.mytype.douyin.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response {
    @JsonProperty("status_code")
    private int StatusCode;
    @JsonProperty("status_msg")
    private String StatusMsg;

    public Response(){

    }

    public Response(int statusCode){
        StatusCode = statusCode;
    }

    public Response(int statusCode, String statusMsg){
        StatusCode = statusCode;
        StatusMsg = statusMsg;
    }
}
