package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.sys.logic.AppLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "get/{id}", method = {RequestMethod.GET})
    public Base get(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "getInfo/{id}", method = {RequestMethod.GET})
    public Base getInfo(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "getList", method = {RequestMethod.GET})
    public Base getList(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "list", method = {RequestMethod.GET})
    public Base list(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "save", method = {RequestMethod.GET})
    public Base save(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "upd", method = {RequestMethod.GET})
    public Base upd(@PathVariable("id") String id) {
        return Base.suc();
    }

    @RequestMapping(value = "del/{ids}", method = {RequestMethod.GET})
    public Base del(@PathVariable("ids") String ids) {
        return logic.del(ids);
    }
}
