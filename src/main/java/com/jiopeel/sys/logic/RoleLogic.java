package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Role;
import com.jiopeel.sys.bean.RolePermission;
import com.jiopeel.sys.bean.form.RoleForm;
import com.jiopeel.sys.bean.query.RoleQuery;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.bean.result.RolePermissionResult;
import com.jiopeel.sys.bean.result.RoleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色Logic层
 *
 * @author ：lyc
 * @date ：2020年07月16日16:37:44
 */
@Slf4j
@Service
public class RoleLogic extends BaseLogic {


    /**
     * @param id
     * @return App
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Role get(String id) {
        Role bean = new Role();
        if (!BaseUtil.empty(id))
            bean = dao.queryOneById(Role.class, id);
        return bean;
    }

    /**
     * @param id
     * @return AppResult
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public RoleResult getInfo(String id) {
        RoleResult bean = new RoleResult();
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("role.getInfo", id);
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
    public Page<RoleResult> getListPage(RoleQuery query, Page<RoleResult> page) {
        if (query != null && !BaseUtil.empty(query.getName()))
            query.setName(query.getName().trim());
        return dao.queryPageList("role.getListPage", query, page);
    }

    /**
     * @return Base
     * @Description: 获取分页列表数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public List<RoleResult> getList() {
        return dao.query("role.getList");
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
    public boolean save(RoleForm form) {
        CheckBean(form);
        String id = form.getId();
        Role bean = new Role();
        if (BaseUtil.empty(id)) {
            BaseUtil.copyProperties(form, bean);
            if (BaseUtil.empty(bean.getId()))
                bean.createID();
            bean.createTime();
            dao.add(bean);
        } else {
            bean = get(id);
            BaseUtil.copyProperties(form, bean);
            bean.updTime();
            dao.upd(bean, "id", "id", "ctime");
        }
        return true;
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
        Assert.isNull(ids, "未选择不能删除");
        String[] ids_ = ids.split(",");
        if (ids_.length <= 0) {
            Assert.isNull("", "未选择不能删除");
        }

        dao.delByIds(Role.class, ids_);
        // 删除角色对应的权限
        dao.del(RolePermission.class, "roleid", ids_);
        return Base.suc("删除成功");
    }


    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(RoleForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getName(), "角色名称不能为空");
    }


    /**
     * 根据用户id获取用户角色
     *
     * @param id
     * @return List<RoleResult>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public List<RoleResult> getRoles(String id) {
        if (BaseUtil.empty(id))
            return null;
        return dao.query("role.getListById", id);
    }


    /**
     * 将 List<RoleResult> 转 Set<String>
     *
     * @param list
     * @return Set<String>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public Set<String> getRoles(List<RoleResult> list) {
        if (list == null || list.isEmpty())
            return null;
        Set<String> roles = new HashSet<>();
        for (RoleResult role : list) {
            roles.add(role.getId());
        }
        return roles;
    }

    /**
     * 根据角色获取权限信息
     *
     * @param roles
     * @return List<PermissionResult>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public List<PermissionResult> getPermissionByRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty())
            return null;
        return dao.query("role.getPermissionByRoles", roles);
    }

    /**
     * 根据角色获取菜单
     *
     * @param roles
     * @return List<MenuResult>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public List<MenuResult> getMenuByRoles(Set<String> roles) {
        if (roles == null || roles.isEmpty())
            return null;
        return dao.query("role.getMenuByRoles", roles);
    }


    /**
     * 将 List<PermissionResult> 转 Set<String>
     *
     * @param list
     * @return Set<String>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public Set<String> getPermissions(List<PermissionResult> list) {
        if (list == null || list.isEmpty())
            return null;
        Set<String> permissions = new HashSet<>();
        for (PermissionResult permission : list) {
            String charm = permission.getCharm();
            if (!BaseUtil.empty(charm))
                permissions.add(charm);
        }
        return permissions;
    }

    /**
     * 根据角色id查询配置的权限
     *
     * @param roleid 角色id
     * @return Set<String>
     * @author lyc
     * @date 2020年07月16日16:36:49
     */
    public Map<String, RolePermissionResult> getPermissionById(String roleid) {
        // 查询权限
        List<RolePermissionResult> list = dao.query("role.getPermissionById", roleid);
        Map<String, RolePermissionResult> map = new HashMap<>();
        for (RolePermissionResult result : list) {
            map.put(result.getPermissionid(), result);
        }
        return map;
    }

    /**
     * @Description :保存角色配置的权限
     * @Param: list 权限id集合
     * @Param: roleid 角色id
     * @Return: boolean
     * @auhor:lyc
     * @Date:2019/12/21 00:02
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public boolean savePes(Set<String> sets, String roleid) {
        if(BaseUtil.empty(roleid))
            return false;
        dao.del(RolePermission.class, "roleid", roleid);
        if (sets == null || sets.isEmpty()) {
            return true;
        }
        List<RolePermission> items = new ArrayList<>();
        for (String id : sets) {
            RolePermission bean = new RolePermission();
            bean.setPermissionid(id);
            bean.setRoleid(roleid);
            bean.createTime();
            bean.createID();
            items.add(bean);
        }
        dao.addBatch(items);
        return true;
    }
}
