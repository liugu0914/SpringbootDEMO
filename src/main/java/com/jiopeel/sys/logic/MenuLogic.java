package com.jiopeel.sys.logic;


import com.alibaba.fastjson.JSON;
import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.App;
import com.jiopeel.sys.bean.Menu;
import com.jiopeel.sys.bean.form.AppForm;
import com.jiopeel.sys.bean.form.MenuForm;
import com.jiopeel.sys.bean.query.AppQuery;
import com.jiopeel.sys.bean.query.MenuQuery;
import com.jiopeel.sys.bean.result.AppResult;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.constant.SysConstant;
import com.jiopeel.sys.dao.AppDao;
import com.jiopeel.sys.dao.MenuDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：lyc
 * @description：菜单Logic层
 * @date ：2019/12/20 10:25
 */
@Slf4j
@Service
public class MenuLogic extends BaseLogic {

    @Resource
    private MenuDao dao;

    /**
     * @param id
     * @return App
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Menu get(String id) {
        return dao.queryOne("menu.get", id);
    }

    /**
     * @param id
     * @return AppResult
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public MenuResult getInfo(String id) {
        return dao.queryOne("menu.getInfo", id);
    }

    /**
     * @param query 查询对象
     * @param page     分页器
     * @return Base
     * @Description: 获取分页列表数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Page<MenuResult> getListPage(MenuQuery query, Page<MenuResult> page) {
        Page<MenuResult> PageList = dao.queryPageList("menu.getListPage", query, page);
        return PageList;
    }


    /**
     * @param query 查询对象
     * @return List<AppResult>
     * @Description: 根据搜索条件查询数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public List<MenuResult> list(MenuQuery query) {
        List<MenuResult> list = dao.query("menu.list", query);
        if (list==null || list.isEmpty())
            return list;
        List<MenuResult> menus=new ArrayList();
        Map<String,List<MenuResult>> map=new HashMap();
        for (MenuResult item : list) {
            String superId = item.getSuperid();//上级id
            if (map.containsKey(superId)){
                map.get(superId).add(item);
            }else {
                List<MenuResult> results=new ArrayList();
                results.add(item);
                map.put(superId,results);
            }
        }
        for (MenuResult item : list) {
            if (item.getLevel()== SysConstant.LEVEL_1)
                menus.add(item);
            String id =item.getId();
            if (map.containsKey(id))
                item.setList(map.get(id));
        }
        log.info(JSON.toJSONString(menus));
        return menus;
    }

    /**
     * @param form 表单提交对象
     * @return Base
     * @Description: 保存数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base save(MenuForm form) {
        CheckBean(form);
        Menu bean = new Menu();
        BeanUtils.copyProperties(form, bean);
        if (BaseUtil.empty(bean.getId()))
            bean.createUUID();
        bean.createTime();
        bean.setEnable(Constant.ENABLE_YES);
        return Base.judge(dao.add(bean));
    }


    /**
     * @param form
     * @return Base
     * @Description: 保存数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base upd(MenuForm form) {
        CheckBean(form);
        Assert.isNull(form.getId(), "ID不能为空");
        Menu bean = get(form.getId());
        bean.setName(form.getName());
        bean.setEnable(form.getEnable());
        bean.updTime();
        dao.upd("menu.upd", bean);
        return Base.suc();
    }

    /**
     * @param ids
     * @return Base
     * @Description: 删除
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base del(String ids) {
        String[] ids_ = ids.split(",");
        if (ids_ == null || ids_.length <= 0) {
            Assert.isNull("", "删除不能为空");
        }
        dao.del("menu.del", ids_);
        return Base.suc("删除成功");
    }


    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(MenuForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getName(), "应用名称不能为空");
    }

    /**
     * @Description :菜单名称模糊搜索
     * @Param: map
     * @Return: List<MenuResult>
     * @auhor:lyc∏
     * @Date:2019/12/21 00:02
     */
    public List<MenuResult> searchMenu(Map<String,String> map) {
        List<MenuResult> list=dao.query("menu.searchMenu",map);
        return list;
    }
}
