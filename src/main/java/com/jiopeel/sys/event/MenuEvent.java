package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.core.util.WebUtil;
import com.jiopeel.sys.bean.form.MenuForm;
import com.jiopeel.sys.bean.query.MenuQuery;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.logic.MenuLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author ：lyc
 * @description：菜单Event层
 * @date ：2019/12/20 10:25
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuEvent extends BaseEvent {

    @Resource
    private MenuLogic logic;

    /**
     * @Description :获取查询主页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "main", method = {RequestMethod.GET})
    public String index() {
        return "sys/menu/main";
    }

    /**
     * @Description :获取分页列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "data", method = {RequestMethod.POST})
    public String data(@ModelAttribute MenuQuery query, Page<MenuResult> page, Model model) {
        Page<MenuResult> PageData = logic.getListPage(query, page);
        model.addAttribute("PageData", PageData);
        return "sys/menu/data";
    }

    /**
     * @Description :添加或修改页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "info", method = {RequestMethod.POST})
    public String addOrUpd(Model model) {
        Map<String, String> map = WebUtil.getParam2Map(request);
        model.addAttribute("bean", logic.getInfo(map.get("id")));
        return "sys/menu/info";
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
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "getListPage", method = {RequestMethod.POST})
    public Base getListPage(@ModelAttribute MenuQuery query, Page<MenuResult> page) {
        return Base.suc(logic.getListPage(query, page));
    }

    /**
     * @Description :获取列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "getList", method = {RequestMethod.POST})
    public Base getList(@ModelAttribute MenuQuery query) {
        List<MenuResult> list = logic.list(query);
        return Base.suc(list);
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
    public Base save(@ModelAttribute MenuForm form) {
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
