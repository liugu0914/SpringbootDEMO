package com.jiopeel.sys.logic;


import com.jiopeel.core.constant.RedisConstant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Permission;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.bean.result.RoleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

}
