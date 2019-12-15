package com.jiopeel.logic;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiopeel.base.Base;
import com.jiopeel.bean.OauthToken;
import com.jiopeel.bean.User;
import com.jiopeel.config.exception.ServerException;

import com.jiopeel.constant.OauthConstant;
import com.jiopeel.constant.UserConstant;
import com.jiopeel.dao.UserDao;
import com.jiopeel.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登陆信息处理
 */
@Slf4j
@Service
public class LoginLogic {

    @Resource
    private UserDao dao;
    @Resource
    private  OauthLogic oauthLogic;


    /**
     * 注册操作
     *
     * @return
     */
    @Transactional
    public Base addregister(User user) {
        if (BaseUtil.empty(user))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(user.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(user.getPassword()))
            throw new ServerException("密码不能为空");
        if (BaseUtil.empty(user.getEmail()))
            throw new ServerException("邮箱不能为空");
        int o = dao.queryOne("login.checkAccount", user);
        if (o > 0)
            throw new ServerException("该账号已存在");
        user.createUUID();
        user.createTime();
        user.setType(UserConstant.USER_TYPE_LOCAL);
        user.setUsername(user.getAccount());
        user.setPassword(BaseUtil.MD5(user.getPassword()));
        boolean s = dao.add("login.saveUser", user);
        return s ? Base.suc("注册成功") : Base.fail("注册失败");
    }

    /**
     * 登陆操作操作
     *x
     * @param tmpUser   页面登陆数据
     * @param request
     * @param response
     * @param client_id
     * @return code
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public String dologin(User tmpUser, HttpServletRequest request, HttpServletResponse response,
                          String client_id) {
        if (!OauthConstant.local_client_id.equals(client_id))
            throw new ServerException("client_id无法识别");
        String password = tmpUser.getPassword();
        if (BaseUtil.empty(tmpUser))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(tmpUser.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(password))
            throw new ServerException("密码不能为空");
        User user = dao.queryOne("login.getUserbyaccount", tmpUser);
        if (user == null)
            throw new ServerException("该账号不存在");
        password = BaseUtil.MD5(password);
        if (!user.getPassword().equals(password))
            throw new ServerException("密码不正确");
        //code
        String code = BaseUtil.getUUID();
        OauthToken oauthToken = oauthLogic.RedisCode(user, code);
        oauthLogic.AddTokenCookie(response,oauthToken);
        return code;
    }


    /**
     * 退出登陆
     * @param request
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public void loginOut(HttpServletRequest request) {
        oauthLogic.loginOut(request);
    }
}
