package com.jiopeel.event;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jiopeel.base.Base;
import com.jiopeel.bean.User;
import com.jiopeel.logic.LoginLogic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description :首页登陆
 * @auhor:lyc
 * @Date:2019/10/30 23:53
 */
@Controller
public class LoginEvent {

    @Resource
    LoginLogic logic;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        return "login";
    }


    /**
     * 登陆
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public Base login(HttpServletRequest request, @ModelAttribute User user) {
        return logic.dologin(user, request);
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
        return  "redirect:/";
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
