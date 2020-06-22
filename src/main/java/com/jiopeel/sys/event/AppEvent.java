package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.core.util.WebUtil;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.logic.AppLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ：lyc
 * @description：应用Event层
 * @date ：2019/12/20 10:25
 */
@Controller
@RequestMapping("/admin/app")
public class AppEvent extends BaseEvent {

    @Resource
    private AppLogic logic;

    /**
     * @Description :获取查询主页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "main", method = {RequestMethod.GET})
    public String main() {
        return "sys/app/main";
    }

    /**
     * @Description :获取分页列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "data", method = {RequestMethod.POST})
    public String data(@ModelAttribute AppQuery query, Page<AppResult> page, Model model) {
        Page<AppResult> PageData = logic.getListPage(query, page);
        model.addAttribute("PageData", PageData);
        return "sys/app/data";
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
        return "sys/app/info";
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
    public Base getListPage(@RequestBody AppQuery appQuery, Page<AppResult> page) {
        return Base.suc(logic.getListPage(appQuery,page));
    }

    /**
     * @Description :获取列表数据
     * @Param: appQuery
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "getList", method = {RequestMethod.POST})
    public Base list(@RequestBody AppQuery appQuery) {
        return Base.suc(logic.list(appQuery));
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
    public Base save(@ModelAttribute AppForm appForm) {
        return logic.save(appForm);
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
