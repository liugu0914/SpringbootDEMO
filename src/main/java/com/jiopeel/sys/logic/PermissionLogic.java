package com.jiopeel.sys.logic;

import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.TreeNode;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Permission;
import com.jiopeel.sys.bean.form.PermissionForm;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.dao.PermissionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<PermissionResult> list(String appid, boolean isSys) {
        Map<String ,String> map= new HashMap<String ,String>();
        map.put("appid",appid);
        if (isSys)
            map.put("isSys","true");
        List<PermissionResult> list = dao.query("permission.list", map);
        return list;
    }

    /**
     * @Description :返回树结构
     * @Param:
     * @Return:
     * @auhor:lyc
     * @Date:2020/7/12 17:52
     */
    public List<TreeNode> menuTree() {
        //查询菜单树
        List<MenuResult> list = menuLogic.Menulist();
        List<TreeNode> treeNodes = new ArrayList<>();
        for (MenuResult result : list) {
            TreeNode node = new TreeNode();
            node.setId(result.getId());
            node.setPid(result.getSuperid());
            node.setName(result.getName());
            node.setParent(BaseUtil.parseBoolean(result.getParent()));
            if(node.isParent())
                node.setOpen(true);
            node.setIconfont(result.getIcon());
            node.setChecked(true);
            node.setUrl(result.getUrl());
            treeNodes.add(node);
        }
        return treeNodes;
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

}
