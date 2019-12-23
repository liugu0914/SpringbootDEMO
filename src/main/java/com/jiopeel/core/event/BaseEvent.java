package com.jiopeel.core.event;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description :首页登陆
 * @auhor:lyc
 * @Date:2019/10/30 23:53
 */
public abstract class BaseEvent {

    @Resource
    public HttpServletRequest request;

    @Resource
    public HttpServletResponse response;

}
