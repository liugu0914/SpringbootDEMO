package com.jiopeel.core.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.OauthToken;
import com.jiopeel.core.bean.User;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.logic.OauthLogic;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description :授权登录
 * @auhor:lyc
 * @Date:2019年12月12日17:39:11
 */
@Slf4j
@Controller
public class OauthEvent extends  BaseEvent{

    @Resource
    private OauthLogic logic;


    /**
     * 不同授权类型不同登陆方式
     *
     * @param granttype 授权类型 github
     * @return String
     * @auhor:lyc
     * @Date:2019/12/12 22:08
     */
    @RequestMapping(value = {"/oauth"}, method = RequestMethod.GET)
    public String oauth(@RequestParam(value = "granttype", required = false) String granttype) {
        String url = "";
        if (BaseUtil.empty(granttype))
            granttype = UserConstant.USER_TYPE_LOCAL;
        String redirect_uri=BaseUtil.encodeURL(OauthConstant.EDIRECT_URI + "/" + granttype);
        switch (granttype) {
            case UserConstant.USER_TYPE_GITHUB:
                url = String.format(OauthConstant.GITHUB_URL, OauthConstant.GITHUB_CLIENT_ID,redirect_uri);
                break;
            case UserConstant.USER_TYPE_GITEE:
                url = String.format(OauthConstant.GITEE_URL, OauthConstant.GITEE_CLIENT_ID,redirect_uri);
                break;
            case UserConstant.USER_TYPE_LOCAL:
                url = String.format(OauthConstant.local_url, OauthConstant.local_client_id, redirect_uri);
                break;
            default:
                break;
        }
        return "redirect:" + url;
    }


    /**
     * 授权回调地址
     *
     * @param granttype 授权类型
     * @return
     * @auhor:lyc
     * @Date:2019/12/12 22:08
     */
    @RequestMapping(value = {"/oauth/redirect/{granttype}"}, method = RequestMethod.GET)
    public String redirect( Model model, @PathVariable("granttype") String granttype) {
        model.addAttribute("access_token",  logic.redirectType(request, response,granttype));
        return  "redirect:/main";
    }

    /**
     * 中间过程添加cookie
     * @param access_token
     * @return
     * @auhor:lyc
     * @Date:2019/12/12 22:08
     */
    @ResponseBody
    @RequestMapping(value = {"/oauth/addcookie"}, method = RequestMethod.GET)
    public void redirect(@RequestParam(value = "access_token",required =false) String access_token) {
        if (!BaseUtil.empty(access_token))
            logic.AddTokenCookie(response,access_token);
        try {
            response.sendRedirect("/main");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description :通过授权码获取本地access_token
     * @Param: request
     * @Param: code
     * @Return: OauthToken
     * @auhor:lyc
     * @Date:2019/12/12 21:44
     */
    @ResponseBody
    @RequestMapping(value = {"/oauth/access_token"}, method = RequestMethod.POST)
    public OauthToken getOauthUserInfo() {
        return logic.chkLocalOauth(request);
    }


    /**
     * @Description :通过access_token获取用户信息
     * @Param: request
     * @Param: access_token
     * @Return: void
     * @auhor:lyc
     * @Date:2019/12/12 21:44
     */
    @ResponseBody
    @RequestMapping(value = {"/oauth/getuser"}, method = RequestMethod.GET)
    public Base getUserInfo( @RequestParam(OauthConstant.ACCESS_TOKEN) String access_token) {
        if (BaseUtil.empty(access_token))
            throw new ServerException("access_token不能为空");
        return logic.getUserbyToken(access_token);
    }

}
