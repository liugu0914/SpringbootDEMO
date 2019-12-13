package com.jiopeel.event;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jiopeel.base.Base;
import com.jiopeel.bean.User;
import com.jiopeel.logic.LoginLogic;

import com.jiopeel.util.BaseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Description :首页登陆
 * @auhor:lyc
 * @Date:2019/10/30 23:53
 */
@Controller
public class LoginEvent {

    @Resource
    private LoginLogic logic;

    @RequestMapping(value = {"/signin"}, method = RequestMethod.GET)
    public String signin() {
        return "redirect:/oauth";
    }

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String home(HttpServletRequest request,
                       @RequestParam(value = "client_id",required = false) String client_id,
                       @RequestParam(value = "redirect_uri",required = false) String redirect_uri,
                       Model model) {
        model.addAttribute("client_id", client_id);
        model.addAttribute("redirect_uri", redirect_uri);
        return "login";
    }

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String login(HttpServletRequest request,
                      @RequestParam("client_id") String client_id,
                      @RequestParam("redirect_uri") String redirect_uri,
                      @ModelAttribute User user) {
        Base base= logic.dologin(user, request,client_id);
        //回调授权地址
        return "redirect:"+redirect_uri+"?"+ BaseUtil.Object2Url(base);
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @RequestMapping(value = {"/loginout"}, method = RequestMethod.GET)
    public String loginout(HttpServletRequest request) {
        if (request.getSession().getAttribute("token") != null)
            request.getSession().removeAttribute("token");
        if (request.getSession().getAttribute("user") != null)
            request.getSession().removeAttribute("user");
        if (request.getSession().getAttribute("access_token") != null)
            request.getSession().removeAttribute("access_token");
        return "redirect:/";
    }

    /**
     * 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public Base register(HttpServletRequest request, @ModelAttribute User user) throws Exception {
        return logic.addregister(user);
    }


    /**
     * 登陆成功跳转main
     *
     * @return
     */
    @RequestMapping(value = {"/main"}, method = RequestMethod.GET)
    public String main() {
        //权限验证
        return "admin/main";
    }
}
