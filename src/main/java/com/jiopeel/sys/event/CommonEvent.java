package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.core.util.WebUtil;
import com.jiopeel.sys.logic.CommonLogic;
import com.jiopeel.sys.logic.MenuLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description：公共的事件集合
 * @author     ：lyc
 * @date       ：2019/12/20 10:25
 */
@Controller
@RequestMapping("/admin/common")
public class CommonEvent extends BaseEvent {

    @Resource
    private CommonLogic logic;

    /**
     * @Description :模糊搜索
     * @Param: query
     * @Return: Base
     * @auhor: lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "search", method = {RequestMethod.POST})
    public Base search() {
        Map<String,String> map= WebUtil.getParam2Map(request);
        return Base.suc(logic.search(map));
    }


    /**
     * @Description :应用模糊搜索
     * @Param: query
     * @Return: Base
     * @auhor:lyc∏
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "searchApp", method = {RequestMethod.POST})
    public Base searchApp() {
        Map<String,String> map= WebUtil.getParam2Map(request);
        return Base.suc(logic.searchApp(map));
    }


    /**
     * @Description :菜单名称模糊搜索
     * @Param: query
     * @Return: Base
     * @auhor:lyc∏
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "searchMenu", method = {RequestMethod.POST})
    public Base searchMenu() {
        Map<String,String> map= WebUtil.getParam2Map(request);
        return Base.suc(logic.searchMenu(map));
    }

    /**
     * @Description :菜单名称模糊搜索
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "icons", method = {RequestMethod.POST,RequestMethod.GET})
    public Base searchIcons() {
        Map<String,String> map= WebUtil.getParam2Map(request);
        return Base.suc(logic.searchIcons(map));
    }
}
