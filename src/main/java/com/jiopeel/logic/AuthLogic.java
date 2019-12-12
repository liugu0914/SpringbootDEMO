package com.jiopeel.logic;

import com.alibaba.fastjson.JSONObject;
import com.jiopeel.bean.UserGrant;
import com.jiopeel.config.exception.ServerException;
import com.jiopeel.constant.OAuthConstant;
import com.jiopeel.dao.UserDao;
import com.jiopeel.dao.UserGrantDao;
import com.jiopeel.util.BaseUtil;
import com.jiopeel.util.HttpTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AuthLogic {

    @Resource
    private UserGrantDao dao;

    /**
     * @Description :保存第三方登陆信息
     * @Param: access_token
     * @Param: granttype 授权类型
     * @Return: userGrant
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    @Transactional(rollbackFor = {ServerException.class, Exception.class})
    public UserGrant addOauthUser(String access_token, String granttype) {
        if (BaseUtil.empty(access_token))
            throw new ServerException("access_token不能为空");
        if (BaseUtil.empty(granttype))
            throw new ServerException("granttype不能为空");
        UserGrant userGrant = null;
        switch (granttype) {
            case "github":
                //组装Url
                String user_url = String.format(OAuthConstant.GITHUB_USER, access_token);
                //请求地址
                String result = HttpTool.get(user_url);
                userGrant = savebyGithub(result, access_token, granttype);
                break;
            default:
                break;
        }
        if (BaseUtil.empty(userGrant))
            throw new ServerException("信息有误，授权登陆失败！");
        UserGrant user_data = dao.queryOne("login.getuserGrant", userGrant);
        if (BaseUtil.empty(user_data)) {
            if (!dao.add("login.saveuserGrant", userGrant))
                throw new ServerException("信息有误，授权登陆失败！");
        }else {
            user_data.updTime();
            user_data.setImgurl(userGrant.getImgurl());
            user_data.setNickname(userGrant.getNickname());
            user_data.setPassword(userGrant.getPassword());
            if (!dao.upd("login.upduserGrant", user_data))
                throw new ServerException("信息有误，授权登陆失败！");
        }
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
    private UserGrant savebyGithub(String result, String access_token, String granttype) {
        JSONObject obj = (JSONObject) JSONObject.parse(result);
        UserGrant userGrant = UserGrant.builder()
                .granttype(granttype)
                .imgurl(obj.getString("avatar_url"))
                .onlyid(obj.getString("id"))
                .nickname(obj.getString("login"))
                .password(access_token)
                .build();
        userGrant.createUUID();
        userGrant.createTime();
        return userGrant;
    }

    /**
     * @Description :处理第三方登陆信息
     * @Param: request
     * @Param: granttype 授权类型
     * @Return: UserGrant
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    public UserGrant redirectType(HttpServletRequest request, String granttype) {
        if (BaseUtil.empty(granttype))
            throw new ServerException("授权类型不能为空");
        Map<String, String[]> parameterMap = request.getParameterMap();
        String access_token = null;
        switch (granttype) {
            case "github":
                access_token = getTokenbyGithub(parameterMap);
                break;
            default:
                break;
        }
        return addOauthUser(access_token, granttype);
    }

    /**
     * @Description :处理github登陆信息
     * @Param: request
     * @Param: granttype 授权类型
     * @Return: String 返回access_token
     * @auhor:lyc
     * @Date:2019/12/12 21:49
     */
    private String getTokenbyGithub(Map<String, String[]> parameterMap) {
        String access_token = null;
        if (parameterMap.containsKey("code")) {
            String code = parameterMap.get("code")[0];
            String url = String.format(OAuthConstant.GITHUB_TOKEN, OAuthConstant.GITHUB_CLIENT_ID, OAuthConstant.GITHUB_CLIENT_SECRET, code);
            String res = HttpTool.get(url);
            res = BaseUtil.Url2JSON(res);
            JSONObject parse = (JSONObject) JSONObject.parse(res);
            access_token = parse.getString("access_token");
        }
        return access_token;
    }
}
