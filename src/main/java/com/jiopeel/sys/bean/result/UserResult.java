package com.jiopeel.sys.bean.result;

import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.User;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserResult extends User {

    private static final long serialVersionUID = -7338291404537479306L;

    public  UserResult(){
    }

    public  UserResult(User user){
        BaseUtil.copyProperties(user,this);
    }


    /**
     * 用户角色集合
     */
    private Set<String> roles;

    /**
     * 用户权限集合
     */
    private Set<String> permissions;

    /**
     * 用户角色集合
     */
    private List<RoleResult> roleList;

    /**
     * 用户权限集合
     */
    private List<PermissionResult> permissionList;

    /**
     * 用户菜单集合
     */
    private List<MenuResult> menus;
}
