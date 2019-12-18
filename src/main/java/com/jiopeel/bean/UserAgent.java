package com.jiopeel.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
@Builder
public class UserAgent extends Bean implements Serializable {

    private static final long serialVersionUID = -2502043601744976503L;

    @Tolerate
    public UserAgent() {
    }

    /**
     * 用户id
     */
    private String userid;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 浏览器总信息
     */
    private String useragent;

    /**
     * 浏览器名称
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String version;


    /**
     * 系统名称
     */
    private String system;

    /**
     * MAC地址
     */
    private String mac;


    /**
     * 是否是最新登录情况0否 1是
     */
    private String now;

}
