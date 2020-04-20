package com.jiopeel.core.config.interceptor;

import com.jiopeel.core.bean.OauthToken;
import com.jiopeel.core.config.redis.RedisUtil;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.logic.OauthLogic;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


/**
 * @author ：lyc
 * @description：拦截登录
 * @date ：2019/12/14 16:43
 */
@Component
@Slf4j
public class OAuthInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private OauthLogic oauthLogic;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //token验证
        String url = request.getRequestURL().toString();

        String access_token =oauthLogic.getTokenfromCookie(request);
        //access_token 为空 则登陆跳转
        if (!redisUtil.hasKey(access_token)) {
            log.warn(OauthConstant.ACCESS_TOKEN + "为空跳转登录");
            redirect(response);
            return false;
        }
        OauthToken token = (OauthToken) redisUtil.get(access_token);
        //reflesh_token 过期 则登陆跳转
        if (token.getRefreshExpiresIn()<new Date().getTime()){
            log.warn(OauthConstant.REFLESH_TOKEN + "失效，跳转登录");
            redirect(response);
            return false;
        }
        //access_token 过期，不刷新reflesh_token
        if (token.getExpiresIn()<new Date().getTime()){
            log.warn(OauthConstant.ACCESS_TOKEN + "过期,进行续期操作,生成新的token");
            token.setAccess_token(BaseUtil.getUUID());
            //存放新的token到cookie中
            Cookie newtoken = new Cookie(OauthConstant.ACCESS_TOKEN, access_token);
            newtoken.setPath("/");
            response.addCookie(newtoken);
        }else {//未过期 只刷新过期时间
            token.setExpiresIn(new Date().getTime()+Constant.TOKEN_TIMEOUT);
        }
        oauthLogic.RedisUserUpd(token);
        request.setAttribute("user", redisUtil.hget(UserConstant.USER,token.getUserId()));
        return true;
    }

    public void redirect(HttpServletResponse response) throws IOException {
        //token超时处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.sendRedirect("/index");
    }
}
