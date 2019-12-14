package com.jiopeel.logic;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.jiopeel.base.Base;
import com.jiopeel.base.StateCode;
import com.jiopeel.bean.OauthToken;
import com.jiopeel.config.redis.RedisUtil;
import com.jiopeel.constant.Constant;
import com.jiopeel.bean.User;
import com.jiopeel.config.exception.ServerException;

import com.jiopeel.constant.OauthConstant;
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

    @Resource
    private RedisUtil redisUtil;

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
     * @return code
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public String dologin(User tmpuser, HttpServletRequest request, String client_id) {
        if (!OauthConstant.local_client_id.equals(client_id))
            throw new ServerException("client_id无法识别");
        String password = tmpuser.getPassword();
        if (BaseUtil.empty(tmpuser))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(tmpuser.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(password))
            throw new ServerException("密码不能为空");
        User user = dao.queryOne("login.getUserbyaccount", tmpuser);
        if (user == null)
            throw new ServerException("该账号不存在");
        password = BaseUtil.MD5(password);
        if (!user.getPassword().equals(password))
            throw new ServerException("密码不正确");
        //token
        String token=BaseUtil.getUUID();
        //reflesh_token
        String reflesh_token=BaseUtil.getUUID();
        //code
        String code=BaseUtil.getUUID();
        OauthToken oauthToken=new OauthToken(token,reflesh_token);
        oauthToken.setExpiresIn(new Date().getTime() + Constant.TOKEN_TIMEOUT);
        oauthToken.setRefreshExpiresIn(new Date().getTime() + Constant.RELESH_TOKEN_TIMEOUT);
        //将token放入redis
        redisUtil.set(token,user,Constant.TOKEN_TIMEOUT);
        //将reflesh_token放入redis
        redisUtil.set(reflesh_token,oauthToken,Constant.RELESH_TOKEN_TIMEOUT);
        //将code放入redis
        redisUtil.set(code,oauthToken,Constant.CODE_TIMEOUT);
        return code;
    }


}
