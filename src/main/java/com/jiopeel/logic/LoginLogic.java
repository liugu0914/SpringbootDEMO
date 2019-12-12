package com.jiopeel.logic;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.jiopeel.base.Base;
import com.jiopeel.base.StateCode;
import com.jiopeel.constant.Constant;
import com.jiopeel.bean.User;
import com.jiopeel.config.exception.ServerException;

import com.jiopeel.constant.OAuthConstant;
import com.jiopeel.dao.UserDao;
import com.jiopeel.util.BaseUtil;
import com.jiopeel.util.HttpTool;
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


    /**
     * 注册操作
     *
     * @return
     */
    @Transactional
    public Base addregister(User user) {
        user.createUUID();
        user.createTime();
        user.setUsername(user.getAccount());
        user.setPassword(BaseUtil.MD5(user.getPassword()));
        int o = dao.queryOne("login.checkusername", user);
        if (o > 0)
            return Base.fail("该账号已存在");
        boolean s = dao.add("login.saveUser", user);
        return s ? Base.suc("注册成功") : Base.fail("注册失败");
    }

    /**
     * 登陆操作操作
     * @param tmpuser 页面登陆数据
     * @param request
     * @param client_id
     * @param redirect_uri
     * @return Base
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public Base dologin(User tmpuser, HttpServletRequest request, String client_id, String redirect_uri) {
        String password = tmpuser.getPassword();
        if (BaseUtil.empty(tmpuser))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(tmpuser.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(password))
            throw new ServerException("账号不能为空");
        User user = dao.queryOne("login.getUserbyaccount", tmpuser);
        if (user == null)
            throw new ServerException("该账号不存在");
        password = BaseUtil.MD5(password);
        if (!user.getPassword().equals(password))
            throw new ServerException("密码不正确");
        Base base = Base.suc("登陆成功");
        //token
        String pidaes = BaseUtil.AES(user.getId());
        Long exlong = new Date().getTime() + Constant.TOKEN_TIMEOUT;
        log.info("本地token过期时间：" + new Date(exlong).toString());
        String tokenstr = pidaes + "#" + exlong;
        String token = BaseUtil.AES(tokenstr);
        base.setToken(token);
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(Constant.SESSION_TIMEOUT);//session有效期10小时
        session.setAttribute("access_token", pidaes);
        session.setAttribute("user", user);
        session.setAttribute("token", token);

        if (!OAuthConstant.local_client_id.equals(client_id)){
            base.setResult(false);
            base.setCode(StateCode.FAIL.getCode());
            base.setMessage("client_id无法识别");
        }
        return base;
    }


}