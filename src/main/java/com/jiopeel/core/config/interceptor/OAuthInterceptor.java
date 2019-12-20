package com.jiopeel.core.config.interceptor;

import com.jiopeel.core.bean.OauthToken;
import com.jiopeel.core.config.redis.RedisUtil;
import com.jiopeel.core.constant.OauthConstant;
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

    //controller 调用之前被调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        //token验证
        String access_token = "";
        String reflesh_token = "";
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (int i = 0; i < cookie.length; i++) {
                if (!BaseUtil.empty(access_token) && !BaseUtil.empty(reflesh_token))
                    break;
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase(OauthConstant.ACCESS_TOKEN)) //获取键
                    access_token = cook.getValue();
                if (cook.getName().equalsIgnoreCase(OauthConstant.REFLESH_TOKEN))//获取键
                    reflesh_token = cook.getValue();
            }
        }
        //reflesh_token 为空 则登陆跳转
        if (!redisUtil.hasKey(reflesh_token)) {
            log.warn(OauthConstant.REFLESH_TOKEN + "无效: {}", reflesh_token);
            redirect(response);
            return false;
        }
        //access_token为空 通过reflesh_token请求新的access_token
        if (!redisUtil.hasKey(access_token)) {
            log.warn(OauthConstant.ACCESS_TOKEN + "无效: {}", access_token);
            log.warn("通过{}获取新的{}",OauthConstant.REFLESH_TOKEN,OauthConstant.ACCESS_TOKEN);
            OauthToken oauthToken= (OauthToken)redisUtil.get(reflesh_token);
            access_token=BaseUtil.getUUID();
            oauthToken.setAccess_token(access_token);
            //更新redis中的数据
            oauthLogic.RedisUserUpd(oauthToken);
            Cookie token = new Cookie(OauthConstant.ACCESS_TOKEN, access_token);
            token.setPath("/");
            response.addCookie(token);
        }
        request.setAttribute("user", redisUtil.get(access_token));
        return true;
    }

    public void redirect(HttpServletResponse response) throws IOException {
        //token超时处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.sendRedirect("/index");
    }
}
