package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.RedisConstant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Menu;
import com.jiopeel.sys.bean.form.MenuForm;
import com.jiopeel.sys.bean.query.MenuQuery;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.constant.SysConstant;
import com.jiopeel.sys.dao.MenuDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
     * @return Menu
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Menu get(String id) {
        MenuResult bean = new MenuResult();
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("menu.get", id);
        return bean;
    }

    /**
     * @param id
     * @return MenuResult
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public MenuResult getInfo(String id) {
        MenuResult bean = new MenuResult();
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("menu.getInfo", id);
        return bean;
    }

    /**
     * @param query 查询对象
     * @param page  分页器
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
     * @return List<MenuResult>
     * @Description: 根据搜索条件查询数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @Cacheable(value = RedisConstant.CACHE, key = "targetClass + '$Menus'")
    public List<MenuResult> list(MenuQuery query) {
        List<MenuResult> list = dao.query("menu.list", query);
        if (list == null || list.isEmpty())
            return list;
        List<MenuResult> menus = new ArrayList();
        Map<String, List<MenuResult>> map = new HashMap();
        for (MenuResult item : list) {
            String superId = item.getSuperid();//上级id
            if (map.containsKey(superId)) {
                map.get(superId).add(item);
            } else {
                List<MenuResult> results = new ArrayList();
                results.add(item);
                map.put(superId, results);
            }
        }
        for (MenuResult item : list) {
            if (item.getLevel() == SysConstant.LEVEL_1)
                menus.add(item);
            String id = item.getId();
            if (map.containsKey(id))
                item.setList(map.get(id));
        }
        log.info(BaseUtil.toJson(menus));
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
    @CacheEvict(value = RedisConstant.CACHE, key = "targetClass + '$Menus'")
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base save(MenuForm form) {
        CheckBean(form);
        Menu bean = new Menu();
        BeanUtils.copyProperties(form, bean);
        if (BaseUtil.empty(bean.getId()))
            bean.createUUID();
        bean = HandleLevel(bean);
        bean.createTime();
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
    @CacheEvict(value = RedisConstant.CACHE, key = "targetClass + '$Menus'")
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base upd(MenuForm form) {
        CheckBean(form);
        Assert.isNull(form.getId(), "ID不能为空");
        Menu bean = get(form.getId());
        bean.setName(form.getName());
        bean.setIcon(form.getIcon());
        bean.setParent(form.getParent());
        bean.setUrl(form.getUrl());
        bean.setSuperid(form.getSuperid());
        bean.setOrdernum(form.getOrdernum());
        bean.setAppid(form.getAppid());
        bean.setEnable(form.getEnable());
        bean = HandleLevel(bean);
        bean.updTime();
        dao.upd(bean, "id", "id", "ctime");
        return Base.suc();
    }

    /**
     * @Description :处理menu中的 level superid ordernum
     * @Param: Menu bean
     * @Return: Menu
     * @auhor:lyc
     * @Date:2020/5/25 16:26
     */
    private Menu HandleLevel(Menu bean) {
        if (Constant.NO.equals(bean.getParent())) {
            Menu superMenu = get(bean.getSuperid());
            Integer level = superMenu.getLevel();
            bean.setLevel(++level);
        } else {//是父级
            bean.setLevel(SysConstant.LEVEL_1);
            bean.setSuperid(SysConstant.NO_SUPER);
        }
        if (bean.getOrdernum() == null || bean.getOrdernum() == 0) {
            //根据父级id查询该父级下最大的序号
            String SuperId = bean.getSuperid();
            Integer OrderNum = dao.queryOne("menu.getOrderNumBySuperId", SuperId);
            bean.setOrdernum(OrderNum);
        }
        return bean;
    }

    /**
     * @param ids
     * @return Base
     * @Description: 删除
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    @CacheEvict(value = RedisConstant.CACHE, key = "targetClass + '$Menus'")
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base del(String ids) {
        String[] ids_ = ids.split(",");
        if (BaseUtil.empty(ids) || ids_ == null || ids_.length <= 0) {
            Assert.isNull("", "未选择不能删除");
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
        Assert.isNull(form.getName(), "菜单名称不能为空");
        Assert.isNull(form.getIcon(), "图标不能为空");
        Assert.isNull(form.getParent(), "是否为父级不能为空");
        if (Constant.NO.equals(form.getParent())) {//是父级
            Assert.isNull(form.getUrl(), "地址不能为空");
            Assert.isNull(form.getSuperid(), "上级菜单不能为空");
        }
        Assert.isNull(form.getAppid(), "应用ID不能为空");
        Assert.isNull(form.getEnable(), "是否可用不能为空");
    }
}
