package com.jiopeel.config.interceptor;

import com.jiopeel.base.Constant;
import com.jiopeel.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;


@Component
@Slf4j
public class LogicInterceptor implements HandlerInterceptor {

    //controller 调用之前被调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        HttpSession session = request.getSession();
        //token验证
        String token = "";
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue();
                    break;
                }
            }
        }
        if (BaseUtil.empty(token)) {
            log.info("token为空====>跳转登陆");
            redirect(response);
            return false;
        }
        String result = BaseUtil.AESdec(token);
        if (BaseUtil.empty(result)) {
            log.info("token解析失败:{}", token);
            redirect(response);
            return false;
        }
        String[] split = result.split("#");
        long expd = Long.valueOf(split[1]).longValue();
        boolean flag = token.equalsIgnoreCase((String) session.getAttribute("token"));
        if (!flag) {
            log.info("token无效:{}", token);
            log.info("session-token:{}", session.getAttribute("token"));
            redirect(response);
            return false;
        }
        if (new Date().getTime() > expd && flag) {
            log.info("token超时:{}", token);
            String tokenstr = session.getAttribute("access_token") + "#" + (new Date().getTime() + Constant.TOKEN_TIMEOUT);
            token = BaseUtil.AES(tokenstr);
            Cookie ss = new Cookie("token", token);
            session.setAttribute("token", token);
            response.addCookie(ss);
        }
        request.setAttribute("user", session.getAttribute("user"));
        return true;
    }

    public void redirect(HttpServletResponse response) throws IOException {
        //token超时处理
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.sendRedirect("/index");
    }

    //对于请求是ajax请求重定向问题的处理方法
    public void reDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENTPATH", basePath);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setHeader("token", request.getHeader("token"));
        } else {
            response.sendRedirect(basePath);
        }
    }
}
