package com.jiopeel.core.config.filter;

import com.jiopeel.core.bean.OauthToken;
import com.jiopeel.core.config.redis.RedisUtil;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.logic.OauthLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


/**
 * @author ：lyc
 * @description 拦截登录
 * @date ：2020年07月19日19:03:47
 */
@Slf4j
public class TokenFilter extends PathMatchingFilter {

    public TokenFilter() {
    }

    /**
     *  token校验
     * @param req
     * @param res
     * @return boolean
     * @throws IOException
     * @auhor: lyc
     * @Date: 2020/7/19 19:04
     */
    @Override
    public boolean onPreHandle(ServletRequest req, ServletResponse res, Object mappedValue)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        return Oauth(request, response);
    }

    /**
     *  token校验
     * @param request
     * @param response
     * @return boolean
     * @throws IOException
     * @auhor: lyc
     * @Date: 2020/7/19 19:04
     */
    public boolean Oauth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        OauthLogic oauthLogic = SpringUtil.getBean(OauthLogic.class);

        //token验证
        String url = request.getRequestURL().toString();

        String access_token = oauthLogic.getTokenfromCookie(request);
        request.setAttribute(OauthConstant.ACCESS_TOKEN, access_token);
        //access_token 为空 则登陆跳转
        if (!redisUtil.hasKey(access_token)) {
            log.warn(OauthConstant.ACCESS_TOKEN + "为空跳转登录");
            redirect(response);
            return false;
        }
        OauthToken token = (OauthToken) redisUtil.get(access_token);
        //reflesh_token 过期 则登陆跳转
        if (token.getRefreshExpiresIn() < new Date().getTime()) {
            log.warn(OauthConstant.REFLESH_TOKEN + "失效，跳转登录");
            redirect(response);
            return false;
        }
        //access_token 过期，不刷新reflesh_token
        if (token.getExpiresIn() < new Date().getTime()) {
            log.warn(OauthConstant.ACCESS_TOKEN + "过期,进行续期操作,生成新的token");
            token.setAccess_token(BaseUtil.getUUID());
            //存放新的token到cookie中
            Cookie newtoken = new Cookie(OauthConstant.ACCESS_TOKEN, access_token);
            newtoken.setPath("/");
            response.addCookie(newtoken);
        } else {//未过期 只刷新过期时间
            token.setExpiresIn(new Date().getTime() + Constant.TOKEN_TIMEOUT);
        }
        oauthLogic.RedisUserUpd(token);
        request.setAttribute(UserConstant.USER, redisUtil.hget(UserConstant.USER, token.getUserId()));
        return true;
    }

    /**
     *  无效重定向
     * @param response
     * @throws IOException
     * @Date 2020年07月19日19:05:32
     */
    public void redirect(HttpServletResponse response) throws IOException {
        //token超时处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.sendRedirect("/");
    }
}
