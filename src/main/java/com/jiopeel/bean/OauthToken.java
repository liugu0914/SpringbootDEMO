package com.jiopeel.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :返回token数据
 * @auhor:lyc
 * @Date:2019/12/12 21:23
 */
@Data
public class OauthToken implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;

    /**
     *token
     */
    private String access_token;
    /**
     * 刷新token
     */
    private String refresh_token;
    /**
     * token过期时间
     */
    private Date expires_in;
    /**
     * 刷token过期时间
     */
    private Date refreshExpiresIn;
    /**
     * 其他数据
     */
    private Object data;
}
