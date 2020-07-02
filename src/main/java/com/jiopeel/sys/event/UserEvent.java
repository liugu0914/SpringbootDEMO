package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.core.util.WebUtil;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.form.UserForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.query.UserQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.bean.result.UserResult;
import com.jiopeel.sys.logic.AppLogic;
import com.jiopeel.sys.logic.UserLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：lyc
 * @description：用户Event层
 * @date ：2019/12/20 10:25
 */
@Controller
@RequestMapping("/admin/user")
public class UserEvent extends BaseEvent {

    @Resource
    private UserLogic logic;

    /**
     * @Description :获取查询主页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "main", method = {RequestMethod.GET})
    public String main() {
        return "sys/user/main";
    }

    /**
     * @Description :获取分页列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "data", method = {RequestMethod.POST})
    public String data(@ModelAttribute UserQuery query, Page<UserResult> page, Model model) {
        Page<UserResult> PageData = logic.getListPage(query, page);
        model.addAttribute("PageData", PageData);
        return "sys/user/data";
    }

    /**
     * @Description :添加或修改页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "info", method = {RequestMethod.POST})
    public String info(Model model) {
        Map<String, String> map = WebUtil.getParam2Map(request);
        model.addAttribute("bean", logic.getInfo(map.get("id")));
        return "sys/user/info";
    }

    /**
     * @Description :根据id获取数据
     * @Param: id
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "get/{id}", method = {RequestMethod.GET})
    public Base get(@PathVariable("id") String id) {
        return Base.suc(logic.get(id));
    }

    /**
     * @Description :根据id获取数据
     * @Param: id
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "getInfo/{id}", method = {RequestMethod.GET})
    public Base getInfo(@PathVariable("id") String id) {
        return Base.suc(logic.getInfo(id));
    }

    /**
     * @Description :获取分页列表数据
     * @Param: appQuery
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "getListPage", method = {RequestMethod.POST})
    public Base getListPage(@ModelAttribute UserQuery query, Page<UserResult> page) {
        return Base.suc(logic.getListPage(query,page));
    }

    /**
     * @Description :获取列表数据
     * @Param: appQuery
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "getList", method = {RequestMethod.POST})
    public Base list(@ModelAttribute UserQuery query) {
        return Base.suc(logic.list(query));
    }

    /**
     * @Description :保存
     * @Param: appForm
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "save", method = {RequestMethod.POST})
    public Base save(@ModelAttribute UserForm form) {
        return logic.save(form);
    }

    /**
     * @Description :删除
     * @Param: ids
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "del", method = {RequestMethod.POST})
    public Base del(@RequestParam("id") String ids) {
        return logic.del(ids);
    }
}
