package com.jiopeel.sys.bean;

import com.jiopeel.core.bean.Bean;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @Description : 用户信息
 * @auhor: lyc
 * @Date: 2020/7/16 15:23
 */
@Data
public class User extends Bean implements Serializable {

    private static final long serialVersionUID = -5471590656721463241L;

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
     * 盐值
     */
    private String salt;


    /**
     * 登陆方式 local ， github ，gitee
     */
    private String type;

    /**
     * 是否可用 0否1是
     */
    private String enable;

}
