package com.jiopeel.core.bean;

import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.util.BaseUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :返回token数据
 * @auhor:lyc
 * @Date:2019/12/12 21:23
 */
public class OauthToken implements Serializable {

    private static final long serialVersionUID = -3540383839044057287L;


    public OauthToken() {
        this.expiresIn = new Date().getTime() + Constant.TOKEN_TIMEOUT;
        this.refreshExpiresIn = new Date().getTime() + Constant.RELESH_TOKEN_TIMEOUT;
        this.access_token = BaseUtil.getUUID();
        this.refresh_token = BaseUtil.getUUID();
    }

    public OauthToken(String access_token, String refresh_token) {
        this.expiresIn = new Date().getTime() + Constant.TOKEN_TIMEOUT;
        this.refreshExpiresIn = new Date().getTime() + Constant.RELESH_TOKEN_TIMEOUT;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    /**
     * token
     */
    @Getter
    private String access_token;
    /**
     * 刷新token
     */
    @Getter
    private String refresh_token;
    /**
     * token过期时间
     */
    @Getter
    @Setter
    private Long expiresIn;
    /**
     * 刷token过期时间
     */
    @Getter
    @Setter
    private Long refreshExpiresIn;

    /**
     * 用户id
     */
    @Getter
    @Setter
    private  String userId;

    /**
     * 其他数据
     */
    @Getter
    @Setter
    private Object data;


    public void setAccess_token(String access_token) {
        this.access_token = access_token;
        this.expiresIn = new Date().getTime() + Constant.TOKEN_TIMEOUT;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
        this.refreshExpiresIn = new Date().getTime() + Constant.RELESH_TOKEN_TIMEOUT;
    }
}
