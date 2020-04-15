package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.sys.bean.form.MenuForm;
import com.jiopeel.sys.bean.query.MenuQuery;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.logic.MenuLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


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
    @RequestMapping(value = "index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "sys/menu/index";
    }

    /**
     * @Description :获取分页列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "data", method = {RequestMethod.POST})
    public String data(@RequestBody MenuQuery query, Page<MenuResult> page, Model model) {
        model.addAttribute("list", logic.getListPage(query, page));
        return "sys/menu/data";
    }

    /**
     * @Description :添加或修改页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "addOrUpd", method = {RequestMethod.POST})
    public String addOrUpd(@RequestBody MenuQuery query, Model model) {
        model.addAttribute("list", logic.list(query));
        return "sys/menu/addOrUpd";
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
    public Base getListPage(@RequestBody MenuQuery query, Page<MenuResult> page) {
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
    public Base getList(@RequestBody MenuQuery query) {
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
    public Base save(@RequestBody MenuForm form) {
        return logic.save(form);
    }

    /**
     * @Description :修改
     * @Param: appForm
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "upd", method = {RequestMethod.POST})
    public Base upd(@RequestBody MenuForm form) {
        return logic.upd(form);
    }

    /**
     * @Description :删除
     * @Param: ids
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "del/{ids}", method = {RequestMethod.GET})
    public Base del(@PathVariable("ids") String ids) {
        return logic.del(ids);
    }
}
