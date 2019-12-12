package com.jiopeel.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class OAth extends Bean implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;

    /**
     * 返回授权形式 code 授权码
     */
    private String responseType;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 跳转地址
     */
    private String redirectUrl;

    /**
     * 授权码
     */
    private  String code;

    /**
     * 授权类型
     */
    private  String grantType;

    /**
     * token
     */
    private  String access_token;

    /**
     * 刷新token
     */
    private  String refresh_token;

    /**
     * 范围 read
     */
    private  String scope;
}
