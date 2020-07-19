package com.jiopeel.core.config.filter;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.base.StateCode;
import com.jiopeel.core.config.shiro.PermissionToken;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.User;
import com.jiopeel.sys.bean.result.UserResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


/**
 * @author ：lyc
 * @description：拦截登录
 * @date ：2020年07月19日18:12:18
 */
@Slf4j
public class OAuthFilter extends AccessControlFilter {

    /**
     * 返回false 交给权限处理
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        //不允许访问，执行onAccessDenied的拦截内容
        return false;
    }

    /**
     * url 权限处理
     *
     * @param servletRequest
     * @param servletResponse
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        TokenFilter noCheckFilter = new TokenFilter();
        if (!noCheckFilter.Oauth(request, response))
            return false;
        long start = System.currentTimeMillis();
        //token验证
        String access_token = String.valueOf(request.getAttribute(OauthConstant.ACCESS_TOKEN));
        UserResult user = (UserResult) request.getAttribute(UserConstant.USER);
        String uri = request.getRequestURI();
        String charm = BaseUtil.uri2Charm(uri);
        try {
            //委托给Realm进行认证
            Subject subject = getSubject(servletRequest, servletResponse);
            subject.login(new PermissionToken(access_token));
            //验证权限
            subject.checkPermission(charm);
        } catch (Exception e) {
            log.warn("用户[{} : {}] 没有[{}]的操作权限", user.getAccount(), user.getId(), uri);
            e.printStackTrace();
            //无权限
            noPermission(response);
            return false;
        }
        long end = System.currentTimeMillis();
        long c = end - start;
        if (c >= 500L)
            log.warn("权限验证超时 ============> {}ms, User :[{} : {}] , Uri :{}", c, user.getAccount(), user.getId(), uri);
        return true;
    }


    /**
     * 无权限操作
     *
     * @Param: response
     * @auhor: lyc
     * @Date: 2020/7/19 18:10
     */
    public void noPermission(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(StateCode.NOPERMISSION.getStatus());
            out = response.getWriter();
            out.write(Objects.requireNonNull(BaseUtil.toJson(Base.fail(StateCode.NOPERMISSION))));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
