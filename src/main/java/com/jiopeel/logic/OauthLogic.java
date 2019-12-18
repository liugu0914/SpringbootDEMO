package com.jiopeel.logic;

import com.alibaba.fastjson.JSONObject;
import com.jiopeel.bean.OauthToken;
import com.jiopeel.bean.User;
import com.jiopeel.bean.UserAgent;
import com.jiopeel.bean.UserGrant;
import com.jiopeel.config.exception.ServerException;
import com.jiopeel.config.redis.RedisUtil;
import com.jiopeel.constant.Constant;
import com.jiopeel.constant.OauthConstant;
import com.jiopeel.constant.UserConstant;
import com.jiopeel.dao.UserGrantDao;
import com.jiopeel.util.BaseUtil;
import com.jiopeel.util.HttpTool;
import com.jiopeel.util.WebUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = {ServerException.class, Exception.class})
public class OauthLogic {

    @Resource
    private UserGrantDao dao;


    @Resource
    private RedisUtil redisUtil;

    /**
     * @Description :保存第三方登陆信息
     * @Param: access_token
     * @Param: granttype 授权类型
     * @Return: userGrant
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public OauthToken addOauthUser(String access_token, String granttype) {
        if (BaseUtil.empty(access_token))
            throw new ServerException("access_token不能为空");
        if (BaseUtil.empty(granttype))
            throw new ServerException("granttype不能为空");
        UserGrant userGrant = null;
        String result = "";
        switch (granttype) {
            case UserConstant.USER_TYPE_GITHUB:
                //请求地址
                result = HttpTool.get(String.format(OauthConstant.GITHUB_USER, access_token));
                userGrant = boxBeanbyGithub(result, access_token, granttype);
                break;
            case UserConstant.USER_TYPE_GITEE:
                //请求地址
                result = HttpTool.get(String.format(OauthConstant.GITEE_USER, access_token));
                userGrant = boxBeanbyGitee(result, access_token, granttype);
            default:
                break;
        }
        if (BaseUtil.empty(userGrant))
            throw new ServerException("信息有误，授权登陆失败！");
        UserGrant user_data = dao.queryOne("login.getuserGrant", userGrant);
        User user = null;
        if (BaseUtil.empty(user_data)) {
            user = addUser(userGrant);
            userGrant.setUserid(user.getId());
            if (!dao.add("login.saveuserGrant", userGrant))
                throw new ServerException("信息有误，授权登陆失败！");
        } else {
            user_data.updTime();
            user_data.setImgurl(userGrant.getImgurl());
            user_data.setNickname(userGrant.getNickname());
            user_data.setToken(userGrant.getToken());
            user = updUser(user_data);
            user_data.setUserid(user.getId());
            if (!dao.upd("login.upduserGrant", user_data))
                throw new ServerException("信息有误，授权登陆失败！");
        }
        return !BaseUtil.empty(user) ? RedisUser(user) : null;
    }

    /**
     * @Description :将用户信息存入redis
     * @Param: user
     * @auhor:lyc
     * @Date:2019/12/15 18:14
     */
    public OauthToken RedisUser(User user) {
        return RedisCode(user, null);
    }

    /**
     * @Description :授权码模式
     * @Param: user
     * @Param: code
     * @auhor:lyc
     * @Date:2019/12/15 18:14
     */
    public OauthToken RedisCode(User user, String code) {
        OauthToken oauthToken = null;
        if (redisUtil.hasKey(user.getId())) {
            oauthToken = (OauthToken) redisUtil.get(user.getId());
        } else {
            oauthToken = new OauthToken();
            oauthToken.setUserId(user.getId());
        }
        //将token放入redis
        redisUtil.set(oauthToken.getAccess_token(), user, Constant.TOKEN_TIMEOUT);
        //将reflesh_token放入redis
        redisUtil.set(oauthToken.getRefresh_token(), oauthToken, Constant.RELESH_TOKEN_TIMEOUT);
        //将userId放入redis
        redisUtil.set(user.getId(), oauthToken, Constant.RELESH_TOKEN_TIMEOUT);
        //redis 放入user
        if (redisUtil.hasKey(UserConstant.USER)) {
            redisUtil.hset(UserConstant.USER, user.getId(), user);
        } else {
            Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put(user.getId(), user);
            redisUtil.hmset(UserConstant.USER, userMap);
        }
        //将code存入redis
        if (!BaseUtil.empty(code))
            redisUtil.set(code, oauthToken, Constant.CODE_TIMEOUT);
        return oauthToken;
    }


    /**
     * @Description : 更新redis 中的用户数据
     * @Param: oauthToken
     * @auhor:lyc
     * @Date:2019/12/15 18:14
     */
    public void RedisUserUpd(OauthToken oauthToken) {
        User user = getUser(oauthToken.getUserId());
        redisUtil.set(oauthToken.getAccess_token(), user, Constant.TOKEN_TIMEOUT);
        redisUtil.set(oauthToken.getRefresh_token(), oauthToken, Constant.RELESH_TOKEN_TIMEOUT);
        redisUtil.set(user.getId(), oauthToken, Constant.RELESH_TOKEN_TIMEOUT);
    }

    /**
     * @Description :通过第三方登陆更新用户信息
     * @Param: userGrant
     * @Return: User
     * @auhor:lyc
     * @Date:2019/12/15 18:14
     */
    private User updUser(UserGrant user_data) {
        User user = dao.queryOne("login.getUserbyUserGrant", user_data);
        if (BaseUtil.empty(user))
            return addUser(user_data);
//        user.setImgurl(user_data.getImgurl());
//        user.setUsername(user_data.getNickname());
//        user.updTime();
//        dao.upd("login.updUser", user);
        return user;
    }

    /**
     * @Description :通过第三方登陆添加随机用户信息
     * @Param: userGrant
     * @Return: User
     * @auhor:lyc
     * @Date:2019/12/15 18:14
     */
    private User addUser(UserGrant userGrant) {
        User user = User.builder()
                .enable(UserConstant.USER_YES)
                .account(userGrant.getNickname())
                .imgurl(userGrant.getImgurl())
                .password(BaseUtil.MD5(BaseUtil.getUUID()))
                .type(userGrant.getGranttype())
                .username(userGrant.getNickname())
                .account(BaseUtil.getUUID())
                .build();
        user.createUUID();
        user.createTime();
        if (!dao.add("login.saveUser", user))
            throw new ServerException("添加失败");
        return user;
    }

    /**
     * @Description :处理github第三方登陆信息
     * @Param: result
     * @Param: access_token
     * @Param: granttype
     * @Return: UserGrant
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private UserGrant boxBeanbyGitee(String result, String access_token, String granttype) {
        JSONObject obj = (JSONObject) JSONObject.parse(result);
        UserGrant userGrant = UserGrant.builder()
                .granttype(granttype)
                .token(access_token)
                .build();
        try {
            userGrant.setImgurl(obj.getString("avatar_url"));
            userGrant.setOnlyid(obj.getString("id"));
            userGrant.setNickname(obj.getString("name"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        userGrant.createUUID();
        userGrant.createTime();
        return userGrant;
    }


    /**
     * @Description :处理github第三方登陆信息
     * @Param: result
     * @Param: access_token
     * @Param: granttype
     * @Return: UserGrant
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private UserGrant boxBeanbyGithub(String result, String access_token, String granttype) {
        JSONObject obj = (JSONObject) JSONObject.parse(result);
        UserGrant userGrant = UserGrant.builder()
                .granttype(granttype)
                .token(access_token)
                .build();
        try {
            userGrant.setImgurl(obj.getString("avatar_url"));
            userGrant.setOnlyid(obj.getString("id"));
            userGrant.setNickname(obj.getString("login"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        userGrant.createUUID();
        userGrant.createTime();
        return userGrant;
    }

    /**
     * @Description :处理第三方登陆信息
     * @Param: request
     * @Param: granttype 授权类型
     * @Return: OauthToken
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public String redirectType(HttpServletRequest request, HttpServletResponse response, String granttype) {
        if (BaseUtil.empty(granttype))
            throw new ServerException("授权类型不能为空");
        Map<String, String[]> parameterMap = request.getParameterMap();
        OauthToken oauthToken = null;
        String access_token = "";
        switch (granttype) {
            case UserConstant.USER_TYPE_GITHUB:
                oauthToken = addOauthUser(getTokenbyGithub(parameterMap), granttype);
                access_token = oauthToken.getAccess_token();
                break;
            case UserConstant.USER_TYPE_GITEE:
                oauthToken = addOauthUser(getTokenbyGitee(parameterMap), granttype);
                access_token = oauthToken.getAccess_token();
                break;
            case UserConstant.USER_TYPE_LOCAL:
                access_token = getTokenbyLocal(parameterMap);
                break;
            default:
                break;
        }
        if (!BaseUtil.empty(oauthToken))
            BoxuserAgent(oauthToken.getUserId(),request);
        AddTokenCookie(response, access_token);
        return access_token;
    }

    /**
     * @Description :将token信息保存到本地cookie上
     * @Param: response
     * @Param: access_token
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public void AddTokenCookie(HttpServletResponse response, String access_token) {
        OauthToken oauthToken = null;
        if (redisUtil.hasKey(access_token)) {
            User user = (User) redisUtil.get(access_token);
            if (redisUtil.hasKey(user.getId()))
                oauthToken = (OauthToken) redisUtil.get(user.getId());
        }
        if (BaseUtil.empty(oauthToken))
            return;
        AddTokenCookie(response, oauthToken);
    }

    /**
     * @Description :将token信息保存到本地cookie上
     * @Param: response
     * @Param: oauthToken
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public void AddTokenCookie(HttpServletResponse response, OauthToken oauthToken) {
        Cookie[] cookies = {
                new Cookie(OauthConstant.ACCESS_TOKEN, oauthToken.getAccess_token()),
                new Cookie(OauthConstant.REFLESH_TOKEN, oauthToken.getRefresh_token())
        };
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * @Description :处理本地登陆信息
     * @Param: parameterMap
     * @Return: String 返回access_token
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private String getTokenbyLocal(Map<String, String[]> parameterMap) {
        String access_token = null;
        if (parameterMap.containsKey(OauthConstant.CODE)) {
            String code = parameterMap.get(OauthConstant.CODE)[0];
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(OauthConstant.CLIENT_ID, OauthConstant.local_client_id);
            params.put(OauthConstant.CLIENT_SECRET, OauthConstant.local_client_secret);
            params.put(OauthConstant.CODE, code);
            String res = HttpTool.post(OauthConstant.local_token, params);
            JSONObject parse = (JSONObject) JSONObject.parse(res);
            access_token = parse.getString(OauthConstant.ACCESS_TOKEN);
        }
        return access_token;
    }


    /**
     * @Description :处理gitee登陆信息
     * @Param: parameterMap
     * @Return: String 返回access_token
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private String getTokenbyGitee(Map<String, String[]> parameterMap) {
        String access_token = null;
        if (parameterMap.containsKey(OauthConstant.CODE)) {
            String code = parameterMap.get(OauthConstant.CODE)[0];
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(OauthConstant.CLIENT_ID, OauthConstant.GITEE_CLIENT_ID);
            params.put(OauthConstant.CLIENT_SECRET, OauthConstant.GITEE_CLIENT_SECRET);
            params.put(OauthConstant.CODE, code);
            params.put("grant_type", "authorization_code");
            params.put("redirect_uri", OauthConstant.EDIRECT_URI + "/"+UserConstant.USER_TYPE_GITEE);
            String res = HttpTool.post(OauthConstant.GITEE_TOKEN, params);
            JSONObject parse = (JSONObject) JSONObject.parse(res);
            access_token = parse.getString(OauthConstant.ACCESS_TOKEN);
        }
        return access_token;
    }

    /**
     * @Description :处理github登陆信息
     * @Param: parameterMap
     * @Return: String 返回access_token
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private String getTokenbyGithub(Map<String, String[]> parameterMap) {
        String access_token = null;
        if (parameterMap.containsKey(OauthConstant.CODE)) {
            String code = parameterMap.get(OauthConstant.CODE)[0];
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(OauthConstant.CLIENT_ID, OauthConstant.GITHUB_CLIENT_ID);
            params.put(OauthConstant.CLIENT_SECRET, OauthConstant.GITHUB_CLIENT_SECRET);
            params.put(OauthConstant.CODE, code);
            String res = HttpTool.post(OauthConstant.GITHUB_TOKEN, params);
            res = BaseUtil.Url2JSON(res);
            JSONObject parse = (JSONObject) JSONObject.parse(res);
            access_token = parse.getString(OauthConstant.ACCESS_TOKEN);
        }
        return access_token;
    }


    /**
     * @Description :验证授权码
     * @Param: parameterMap
     * @Return: OauthToken
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public OauthToken chkLocalOauth(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey(OauthConstant.CODE))
            throw new ServerException("授权码不能为空");
        if (!parameterMap.containsKey(OauthConstant.CLIENT_ID))
            throw new ServerException(OauthConstant.CLIENT_ID + "不能为空");
        if (!parameterMap.containsKey(OauthConstant.CLIENT_SECRET))
            throw new ServerException(OauthConstant.CLIENT_SECRET + "不能为空");
        String code = parameterMap.get(OauthConstant.CODE)[0];
        String client_id = parameterMap.get(OauthConstant.CLIENT_ID)[0];
        String client_secret = parameterMap.get(OauthConstant.CLIENT_SECRET)[0];
        if (!OauthConstant.local_client_id.equals(client_id))
            throw new ServerException(OauthConstant.CLIENT_ID + "不匹配");
        if (!OauthConstant.local_client_secret.equals(client_secret))
            throw new ServerException(OauthConstant.CLIENT_SECRET + "不匹配");
        if (!redisUtil.hasKey(code))
            throw new ServerException("授权码已失效");
        OauthToken oauthToken = (OauthToken) redisUtil.get(code);
        if (redisUtil.hasKey(code))
            redisUtil.del(code);
        return oauthToken;
    }

    /**
     * @Description :通过id获取用户信息
     * @Param: userId
     * @Return: User
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public User getUser(String userId) {
        return dao.queryOne("login.getUser", userId);
    }

    /**
     * 退出登陆
     *
     * @param request
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public void loginOut(HttpServletRequest request) {
        String reflesh_token = "";
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (int i = 0; i < cookie.length; i++) {
                if (!BaseUtil.empty(reflesh_token))
                    break;
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase(OauthConstant.REFLESH_TOKEN))//获取键
                    reflesh_token = cook.getValue();
            }
        }
        if (redisUtil.hasKey(reflesh_token)) {
            OauthToken oauthToken = (OauthToken) redisUtil.get(reflesh_token);
            if (redisUtil.hasKey(oauthToken.getUserId()))
                redisUtil.del(oauthToken.getUserId());
            if (redisUtil.hasKey(oauthToken.getAccess_token()))
                redisUtil.del(oauthToken.getAccess_token());
            redisUtil.del(reflesh_token);
            if (redisUtil.hHasKey(UserConstant.USER,oauthToken.getUserId())) {
                redisUtil.hdel(UserConstant.USER, oauthToken.getUserId());
            }
        }
    }

    /**
     * 获取登录浏览器信息
     *
     * @param userId
     * @param request
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public void BoxuserAgent(String userId, HttpServletRequest request) {
        try {
            String ua = request.getHeader("User-Agent");
            eu.bitwalker.useragentutils.UserAgent userAgent = eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(ua);
            //获取浏览器信息
            Browser browser = userAgent.getBrowser();
            //获取系统信息
            OperatingSystem os = userAgent.getOperatingSystem();
            //系统名称
            String system = os.getName();
            //浏览器名称
            String browserName = browser.getName();
            Version version = browser.getVersion(ua);
            String versionName = version.getVersion();
            String ipAddr = WebUtil.getIpAddr(request);
            String macAddr = WebUtil.getMacAddr();
            if (!BaseUtil.empty(macAddr))
                macAddr=macAddr.toUpperCase();
            UserAgent agent = UserAgent.builder()
                    .userid(userId)
                    .useragent(ua)
                    .browser(browserName)
                    .ip(ipAddr)
                    .mac(macAddr)
                    .now(UserConstant.USER_YES)
                    .system(system)
                    .version(versionName)
                    .build();
            agent.createUUID();
            agent.createTime();
            dao.upd("login.UpdAgentNotNow", userId);
            dao.add("login.AddUserAgent", agent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
