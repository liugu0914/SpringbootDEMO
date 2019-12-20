package com.jiopeel.core.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.User;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.logic.LoginLogic;
import com.jiopeel.core.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description :首页登陆
 * @auhor:lyc
 * @Date:2019/10/30 23:53
 */
@Slf4j
@Controller
public class LoginEvent extends BaseEvent{

    @Resource
    private LoginLogic logic;

    @RequestMapping(value = {"/signin"}, method = RequestMethod.GET)
    public String signin() {
        return "redirect:/oauth";
    }

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String home(@RequestParam(value = "client_id", required = false) String client_id,
                       @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
                       Model model) {
        if (BaseUtil.empty(client_id))
            client_id= OauthConstant.local_client_id;
        if (BaseUtil.empty(redirect_uri))
            redirect_uri=OauthConstant.EDIRECT_URI+"/"+ UserConstant.USER_TYPE_LOCAL;
        model.addAttribute("client_id", client_id);
        model.addAttribute("redirect_uri", redirect_uri);
        return "core/login";
    }

    /**
     * @description：登录成功返回授权码
     * @author ：lyc
     * @date ：2019/12/14 16:35
     */
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public Object login( @RequestParam("client_id") String client_id,
                        @RequestParam("redirect_uri") String redirect_uri,
                        @ModelAttribute User user)  {
        String code = "";
        try {
            code = logic.dologin(user, request,response, client_id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return  Base.fail(e.getMessage());
        }
        //回调授权地址
        return Base.suc("登陆成功",redirect_uri + "?code=" + code);
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @RequestMapping(value = {"/loginout"}, method = RequestMethod.GET)
    public String loginOut() {
        logic.loginOut(request);
        return "redirect:/";
    }

    /**
     * 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public Base register(@ModelAttribute User user) {
        return logic.addregister(user);
    }


    /**
     * 登陆成功跳转main
     *
     * @return
     */
    @RequestMapping(value = {"/main"}, method = RequestMethod.GET)
    public String main() {
        return "core/admin/main";
    }
}