package com.jiopeel.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
@Builder
public class User extends Bean implements Serializable {

    private static final long serialVersionUID = -2502043601744976503L;

    @Tolerate
    public User() {
        super();
    }

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号
     */
    private String account;

    /**
     * 头像
     */
    private String imgurl;


    /**
     * 登陆方式 local ， github
     */
    private String type;

}
