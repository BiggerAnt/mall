package com.ant.mall.auth.vo;

import lombok.Data;

import javax.servlet.Servlet;

@Data
public class SocialUser {

    private String accessToken;

    private String remindIn;

    private int expiresIn;

    private String uid;

    private String isrealname;
}