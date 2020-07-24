package com.jiopeel.sys.event;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.event.BaseEvent;
import com.jiopeel.core.util.WebUtil;
import com.jiopeel.sys.bean.form.RoleForm;
import com.jiopeel.sys.bean.query.RoleQuery;
import com.jiopeel.sys.bean.result.RoleResult;
import com.jiopeel.sys.constant.SysConstant;
import com.jiopeel.sys.logic.AppLogic;
import com.jiopeel.sys.logic.PermissionLogic;
import com.jiopeel.sys.logic.RoleLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：lyc
 * @description：角色Event层
 * @date ：2019/12/20 10:25
 */
@Controller
@RequestMapping("/admin/role")
public class RoleEvent extends BaseEvent {


    @Resource
    private RoleLogic logic;

    @Resource
    private PermissionLogic permissionLogic;

    @Resource
    private AppLogic appLogic;

    /**
     * @Description :获取查询主页面
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "main", method = {RequestMethod.GET})
    public String main() {
        return "sys/role/main";
    }

    /**
     * @Description :获取分页列表数据
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "data", method = {RequestMethod.POST})
    public String data(@ModelAttribute RoleQuery query, Page<RoleResult> page, Model model) {
        Page<RoleResult> PageData = logic.getListPage(query, page);
        model.addAttribute("PageData", PageData);
        return "sys/role/data";
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
        return "sys/role/info";
    }

    /**
     * @Description :分配权限
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "config", method = {RequestMethod.POST})
    public String config(Model model) {
        Map<String, String> map = WebUtil.getParam2Map(request);
        model.addAttribute("apps", appLogic.list());
        model.addAttribute("roleid", map.get("id"));
        return "sys/role/config";
    }

    /**
     * @Description :分配权限明细
     * @Param: query
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @RequestMapping(value = "configDetail", method = {RequestMethod.POST})
    public String configDetail(Model model) {
        Map<String, String> map = WebUtil.getParam2Map(request);
        String appId = map.get("appid");
        String roleId = map.get("roleid");
        model.addAttribute("sys", permissionLogic.list(appId, SysConstant.PERMISSION_TYPE_SYS));
        model.addAttribute("menus", permissionLogic.getMenuPermissionByAppId(appId));
        model.addAttribute("fucs", permissionLogic.getFucPermissionByAppId(appId));
        model.addAttribute("pes", logic.getPermissionById(roleId));
        model.addAttribute("roleid",roleId);
        return "sys/role/configDetail";
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
    public Base getListPage(@ModelAttribute RoleQuery query, Page<RoleResult> page) {
        return Base.suc(logic.getListPage(query, page));
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
    public Base save(@ModelAttribute RoleForm form) {
        return Base.judge(logic.save(form));
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

    /**
     * @Description :保存角色配置的权限
     * @Param: list
     * @Param: roleid 角色id
     * @Return: Base
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @ResponseBody
    @RequestMapping(value = "savePes", method = {RequestMethod.POST})
    public Base savePes(@RequestParam(value = "permissionid" ,required = false) Set<String> sets,@RequestParam("roleid") String roleid) {
        return Base.judge(logic.savePes(sets,roleid));
    }
}
