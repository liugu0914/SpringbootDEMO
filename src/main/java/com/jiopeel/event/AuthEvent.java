package com.jiopeel.event;

import com.jiopeel.bean.UserGrant;
import com.jiopeel.config.exception.ServerException;
import com.jiopeel.constant.Constant;
import com.jiopeel.constant.OAuthConstant;
import com.jiopeel.logic.AuthLogic;
import com.jiopeel.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Description :授权登录
 * @auhor:lyc
 * @Date:2019年12月12日17:39:11
 */
@Slf4j
@Controller
public class AuthEvent {

    @Resource
    private AuthLogic logic;

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
            granttype = "local";
        switch (granttype) {
            case "github":
                url = String.format(OAuthConstant.GITHUB_URL, OAuthConstant.GITHUB_CLIENT_ID, OAuthConstant.EDIRECT_URI + "/" + granttype);
                break;
            case "local":
                url = String.format(OAuthConstant.local_url, OAuthConstant.local_client_id, OAuthConstant.EDIRECT_URI + "/" + granttype);
                break;
            default:
                url="/";
                break;
        }
        return "redirect:" + url;
    }


    /**
     * 授权回调地址
     *
     * @param request
     * @param granttype 授权类型 github
     * @return
     * @auhor:lyc
     * @Date:2019/12/12 22:08
     */
    @RequestMapping(value = {"/oauth/redirect/{granttype}"}, method = RequestMethod.GET)
    public String redirect(HttpServletRequest request, @PathVariable("granttype") String granttype) {
        UserGrant userGrant = logic.redirectType(request, granttype);
        return "redirect:/" ;
    }

    /**
     * @Description :通过access_token获取第三方用户信息
     * @Param: request
     * @Param: access_token
     * @Return: void
     * @auhor:lyc
     * @Date:2019/12/12 21:44
     */
    @ResponseBody
    @RequestMapping(value = {"/oauth/getuser"}, method = RequestMethod.GET)
    public void getOauthUserInfo(HttpServletRequest request, @RequestParam("access_token") String access_token,
                                 @RequestParam("granttype") String granttype) {
        if (BaseUtil.empty(access_token))
            throw new ServerException("access_token不能为空");
        logic.addOauthUser(access_token, granttype);
    }

}
