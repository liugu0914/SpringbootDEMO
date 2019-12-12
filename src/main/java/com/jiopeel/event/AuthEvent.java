package com.jiopeel.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiopeel.base.Base;
import com.jiopeel.bean.User;
import com.jiopeel.constant.OAuthConstant;
import com.jiopeel.logic.AuthLogic;
import com.jiopeel.logic.LoginLogic;
import com.jiopeel.util.BaseUtil;
import com.jiopeel.util.HttpTool;
import lombok.extern.slf4j.Slf4j;
import org.nutz.http.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description :授权登录
 * @auhor:lyc
 * @Date:2019年12月12日17:39:11
 */
@Slf4j
@Controller
public class AuthEvent {

    @Resource
    AuthLogic logic;

    @RequestMapping(value = {"/oauth"}, method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        String url= String.format(OAuthConstant.GITHUB_URL,OAuthConstant.GITHUB_CLIENT_ID,OAuthConstant.EDIRECT_URI);
        return "redirect:"+url;
    }


    /**
     * 跳转网页
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/oauth/redirect"}, method = RequestMethod.GET)
    public void redirect(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.containsKey("code")){
            String code = parameterMap.get("code")[0];
            String url=String.format(OAuthConstant.GITHUB_TOKEN,OAuthConstant.GITHUB_CLIENT_ID,OAuthConstant.GITHUB_CLIENT_SECRET,code);
            String res= HttpTool.get(url);
            res=BaseUtil.Url2JSON(res);
            JSONObject parse = (JSONObject) JSONObject.parse(res);
            String user_url=String.format(OAuthConstant.GITHUB_USER,parse.get("access_token"));
            String user= HttpTool.get(user_url);
            log.info(res);
        }
    }

}
