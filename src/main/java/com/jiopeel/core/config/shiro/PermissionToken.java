package com.jiopeel.core.config.shiro;

import com.jiopeel.core.constant.OauthConstant;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现AuthenticationToken接口
 *
 * @auhor: lyc
 * @Date: 2020/7/19 18:15
 */
public class PermissionToken implements AuthenticationToken {


    public PermissionToken() {
    }

    public PermissionToken(String token) {
        this.token = token;
    }

    /**
     * token信息
     */
    @Getter
    @Setter
    private String token;


    /**
     * 标识 默认为 access_token
     */
    @Getter
    @Setter
    private String tag = OauthConstant.ACCESS_TOKEN;

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.tag;
    }
}
