package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ：lyc
 * @description：角色表返回bean
 * @date ：2019/12/20 9:47
 */
@Data
public class MenuResult extends Menu {

    private static final long serialVersionUID = -1567228179843411351L;

    /**
     * 子菜单层
     */
    private List<MenuResult> list;

    /**
     * 上级菜单名称
     */
    private String supername;

    /**
     * 应用名称
     */
    private String appname;

    /**
     * 权限id
     */
    private String permissionid;
}
