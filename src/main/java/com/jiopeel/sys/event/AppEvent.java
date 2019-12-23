package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.sys.bean.App;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.logic.AppLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：lyc
 * @description：应用Event层
 * @date ：2019/12/20 10:25
 */
@Slf4j
@Controller
@RequestMapping("/admin/app")
public class AppEvent extends BaseEvent {

    @Resource
    private AppLogic logic;

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
        return logic.get(id);
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
        return logic.getInfo(id);
    }

    /**
     * @Description :获取分页列表数据
     * @Param: appQuery
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "getList", method = {RequestMethod.POST})
    public String getList(@RequestBody AppQuery appQuery, Model model) {
        model.addAttribute("list",logic.getList(appQuery)) ;
        return  "sys/app/index";
    }

    /**
     * @Description :获取列表数据
     * @Param: appQuery
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "list", method = {RequestMethod.POST})
    public String list(@RequestBody AppQuery appQuery, Model model) {
        model.addAttribute("list",logic.getList(appQuery)) ;
        return  "sys/app/index";
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
    public Base save(@RequestBody AppForm appForm) {
        return logic.save(appForm);
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
    public Base upd(@RequestBody AppForm appForm) {
        return logic.upd(appForm);
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
