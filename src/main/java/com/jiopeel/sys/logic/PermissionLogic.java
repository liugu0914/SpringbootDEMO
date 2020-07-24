package com.jiopeel.sys.logic;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.TreeNode;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Permission;
import com.jiopeel.sys.bean.form.PermissionForm;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.constant.SysConstant;
import com.jiopeel.sys.dao.PermissionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ：lyc
 * @description：权限Logic层
 * @date ：2020年07月12日17:52:10
 */
@Slf4j
@Service
public class PermissionLogic extends BaseLogic {

    @Resource
    private MenuLogic menuLogic;

    @Resource
    private PermissionDao dao;

    /**
     * @param id
     * @return Permission
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2020年07月14日14:15:10
     */
    public Permission get(String id) {
        Permission bean = null;
        if (!BaseUtil.empty(id))
            bean = dao.queryOneById(Permission.class, id);
        if (BaseUtil.empty(bean))
            bean = new Permission();
        return bean;
    }

    /**
     * @param id
     * @return Permission
     * @Description: 根据id获取应用信息 自定义
     * @author lyc
     * @version 1.0.0
     * @date 2020年07月14日14:15:27
     */
    public PermissionResult getInfo(String id) {
        PermissionResult bean = null;
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("permission.getInfo", id);
        if (BaseUtil.empty(bean))
            bean = new PermissionResult();
        return bean;
    }


    /**
     * 根据appid 和type 获取对于信息
     * @param appid
     * @param type
     * @return List<PermissionResult>
     * @author lyc
     * @date 2020年07月14日14:15:27
     */
    public List<PermissionResult> list(String appid, String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", appid);
        map.put("type", type);
        List<PermissionResult> list = dao.query("permission.list", map);
        return list;
    }


    /**
     * @param form 表单提交对象
     * @return Base
     * @Description: 保存数据
     * @author lyc
     * @version 1.0.0
     * @date 2020年07月14日15:40:23
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base save(PermissionForm form) {
        CheckBean(form);
        String id = form.getId();
        Permission bean = new Permission();
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
        Assert.isNull(ids, "未选择不能删除");
        String[] ids_ = ids.split(",");
        if (ids_.length <= 0) {
            throw new ServerException("未选择不能删除");
        }
        dao.delByIds(Permission.class, ids_);
        return Base.suc("删除成功");
    }


    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(PermissionForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getName(), "菜单名称不能为空");
        Assert.isNull(form.getAppid(), "应用ID不能为空");
        Assert.isNull(form.getTarget(), "目标值不能为空");
        Assert.isNull(form.getType(), "权限类型不能为空");
        Assert.isNull(form.getEnable(), "是否可用不能为空");
    }

    /**
     * @Description :查询对于应用配置菜单
     * @Param: param2Map
     * @Return: List<TreeNode>
     * @auhor: lyc
     * @Date: 2020/7/12 17:52
     */
    public List<TreeNode> configTree(Map<String, String> param2Map) {
        String appId = param2Map.get("appid");
        //查询所有菜单
        List<MenuResult> menus = menuLogic.Menulist();
        List<MenuResult> list = new ArrayList<>();
        for (MenuResult menu : menus) {
            if (appId.equals(menu.getAppid()))
                list.add(menu);
        }
        //查询已配置的菜单
        Map<String, PermissionResult> map = getHasMenuPermission(appId);

        List<TreeNode> treeNodes = new ArrayList<>();
        for (MenuResult result : list) {
            String id = result.getId();// 菜单id
            if (!map.containsKey(id))
                continue;
            TreeNode node = new TreeNode();
            node.setId(id);
            node.setPid(result.getSuperid());
            node.setName(result.getName());
            node.setParent(BaseUtil.parseBoolean(result.getParent()));
            if (node.isParent())
                node.setOpen(true);
            node.setIconfont(result.getIcon());
            node.setUrl(result.getUrl());
            treeNodes.add(node);
        }
        return treeNodes;
    }


    /**
     * @Description :查询配置菜单
     * @Param: param2Map
     * @Return: List<TreeNode>
     * @auhor: lyc
     * @Date: 2020/7/12 17:52
     */
    public List<TreeNode> menuTree(Map<String, String> param2Map) {
        String appId = param2Map.get("appid");
        //查询所有菜单
        List<MenuResult> menus = menuLogic.Menulist();
        List<MenuResult> list = new ArrayList<>();
        for (MenuResult menu : menus) {
            if (appId.equals(menu.getAppid()))
                list.add(menu);
        }
        //查询已配置的菜单
        Map<String, PermissionResult> map = getHasMenuPermission(appId);

        List<TreeNode> treeNodes = new ArrayList<>();
        for (MenuResult result : list) {
            String id = result.getId();// 菜单id
            TreeNode node = new TreeNode();
            node.setId(id);
            node.setPid(result.getSuperid());
            node.setName(result.getName());
            node.setParent(BaseUtil.parseBoolean(result.getParent()));
            if (node.isParent())
                node.setOpen(true);
            node.setIconfont(result.getIcon());
            node.setUrl(result.getUrl());
            if (map.containsKey(id)) //选中
                node.setChecked(true);
            treeNodes.add(node);
        }
        return treeNodes;
    }


    /**
     * 查询已配置的菜单
     *
     * @Return: Map<String, PermissionResult>
     * @auhor: lyc
     * @Date: 2020/7/20 15:49
     */
    public Map<String, PermissionResult> getHasMenuPermission(String appId) {
        //查询已配置的菜单
        List<PermissionResult> menuPermissions = this.list(appId, SysConstant.PERMISSION_TYPE_MENU);
        Map<String, PermissionResult> map = new LinkedHashMap<>();
        for (PermissionResult menuPermission : menuPermissions)
            map.put(menuPermission.getTarget(), menuPermission); // 菜单id 权限
        return map;
    }

    /**
     * 保存菜单树结构
     *
     * @Param: list
     * @Return:
     * @auhor: lyc
     * @Date: 2020/7/20 15:49
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Object saveMenu(List<TreeNode> list, Map<String, String> params) {
        if (list == null || list.isEmpty())
            return null;
        String appId = params.get("appid");
        //查询已配置的菜单
        Map<String, PermissionResult> map = getHasMenuPermission(appId);
        //查询菜单树
        List<Permission> menus = new ArrayList<>();
        for (TreeNode node : list) {
            //id 为菜单id
            String id = node.getId();
            //已存在的删除
            if (map.containsKey(id)) {//已配置
                dao.delByIds(Permission.class, map.get(id).getId());
                dao.del(Permission.class, "menuid", id); //删除菜单下的权限配置
                continue;
            }
            //不存在的添加
            Permission permission = new Permission();
            permission.createID();
            permission.createTime();
            permission.setAppid(appId);
            permission.setType(SysConstant.PERMISSION_TYPE_MENU);
            permission.setTarget(id);
            permission.setName(node.getName());
            permission.setEnable(Constant.ENABLE_YES);
            menus.add(permission);
        }
        if (!menus.isEmpty())
            dao.addBatch(menus);
        return null;
    }

    /**
     * 查询菜单下的功能权限
     *
     * @Param: id 菜单id
     * @Return: List<PermissionResult>
     * @auhor: lyc
     * @Date: 2020/7/20 15:49
     */
    public List<PermissionResult> getFucPermission(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("menuid", id);
        map.put("type", SysConstant.PERMISSION_TYPE_FUC);
        List<PermissionResult> query = dao.query("permission.getFucPermission", map);
        return query;
    }

    /**
     * 通过APPId查询菜单下的功能权限
     *
     * @Param: id 菜单id
     * @Return: List<PermissionResult>
     * @auhor: lyc
     * @Date: 2020/7/20 15:49
     */
    public Map<String, List<PermissionResult>> getFucPermissionByAppId(String appId) {
        Map<String, List<PermissionResult>> map = new HashMap<>();
        List<PermissionResult> list = list(appId, SysConstant.PERMISSION_TYPE_FUC);
        for (PermissionResult item : list) {
            String menuId =item.getMenuid();// 菜单id
            if(map.containsKey(menuId)){
                map.get(menuId).add(item);
            }else {
                List<PermissionResult> sans =new ArrayList<>();
                sans.add(item);
                map.put(menuId,sans);
            }
        }
        return map;
    }

    /**
     * 通过APPId查询菜单下的菜单权限
     * @param appId
     * @return List<PermissionResult>
     * @author lyc
     * @date 2020年07月14日14:15:27
     */
    public List<MenuResult> getMenuPermissionByAppId(String appId) {
        //查询已配置的菜单
        Map<String, PermissionResult> Map = getHasMenuPermission(appId);
        //查询所有菜单
        List<MenuResult> menus = menuLogic.Menulist();
        List<MenuResult> items = new ArrayList<>();
        for (MenuResult menu : menus) {
            String menuId =menu.getId(); // 菜单id
            if (Map.containsKey(menuId)) {
                String permissionId = Map.get(menuId).getId();
                menu.setPermissionid(permissionId);
                items.add(menu);
            }
        }
        return items;
    }
}
