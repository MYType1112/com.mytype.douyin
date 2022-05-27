package com.mytype.douyin.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
