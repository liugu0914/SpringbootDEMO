package com.jiopeel.sys.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.config.exception.Assert;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.User;
import com.jiopeel.sys.bean.UserRole;
import com.jiopeel.sys.bean.form.UserForm;
import com.jiopeel.sys.bean.query.UserQuery;
import com.jiopeel.sys.bean.result.RoleResult;
import com.jiopeel.sys.bean.result.UserResult;
import com.jiopeel.sys.bean.result.UserRoleResult;
import com.jiopeel.sys.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：lyc
 * @description：角色Logic层
 * @date ：2019/12/20 10:25
 */
@Slf4j
@Service
public class UserLogic extends BaseLogic {

    @Resource
    private UserDao dao;

    @Autowired
    private RoleLogic roleLogic;

    /**
     * @param id
     * @return App
     * @Description: 根据id获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public User get(String id) {
        User bean = new User();
        if (!BaseUtil.empty(id))
            bean = dao.queryOneById(User.class, id);
        return bean;
    }

    /**
     * @param account
     * @return UserResult
     * @Description: 根据账号获取应用信息与数据库一致
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public UserResult getByAccount(String account) {
        UserResult bean = null;
        if (!BaseUtil.empty(account)) {
            User user = dao.queryOneByColumn(User.class, "account", account);
            if (user == null)
                return null;
            bean = new UserResult(user);
        }
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
    public UserResult getInfo(String id) {
        UserResult bean = new UserResult();
        if (!BaseUtil.empty(id))
            bean = dao.queryOne("user.getInfo", id);
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
    public Page<UserResult> getListPage(UserQuery query, Page<UserResult> page) {
        Page<UserResult> PageList = dao.queryPageList("user.getListPage", query, page);
        return PageList;
    }


    /**
     * @param query 查询对象
     * @return Base
     * @Description: 根据搜索条件查询数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    public Base list(UserQuery query) {
        List<UserResult> list = dao.query("user.list", query);
        return Base.suc(list);
    }

    /**
     * 保存用户的角色配置
     *
     * @param sets
     * @param userId
     * @return Base
     * @author lyc
     * @date 2020年07月24日18:04:34
     */
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public boolean saveConfigRoles(Set<String> sets, String userId) {
        if (BaseUtil.empty(userId))
            return false;
        dao.del(UserRole.class, "userid", userId);
        if (sets == null || sets.isEmpty()) {
            return true;
        }
        List<UserRole> items = new ArrayList<>();
        for (String id : sets) {
            UserRole bean = new UserRole();
            bean.setRoleid(id);
            bean.setUserid(userId);
            bean.createTime();
            bean.createID();
            items.add(bean);
        }
        dao.addBatch(items);
        return true;
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
    public User save(UserForm form) {
        String id = form.getId();
        User bean = new User();
        if (BaseUtil.empty(id)) {//添加
            BaseUtil.copyProperties(form, bean);
            if (BaseUtil.empty(bean.getId()))
                bean.createID();
            bean.createTime();
            bean.setEnable(Constant.ENABLE_YES);
            if (BaseUtil.empty(bean.getType()))
                bean.setType(UserConstant.USER_TYPE_LOCAL);
            if (BaseUtil.empty(bean.getUsername()))
                bean.setUsername(bean.getAccount());
            String salt = BaseUtil.getUUID();//用UUID加盐
            SimpleHash simpleHash = new SimpleHash(Sha256Hash.ALGORITHM_NAME, bean.getPassword(), salt, Constant.SALT_TIMES);
            bean.setPassword(simpleHash.toBase64());
            bean.setSalt(salt);
            dao.add(bean);
        } else {//修改
            CheckBean(form);
            bean = get(id);
            BaseUtil.copyProperties(form, bean);
            bean.updTime();
            dao.upd4n(bean, "id", "username", "email", "enable", "updtime");
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
    @Transactional(rollbackFor = {Exception.class, ServerException.class})
    public Base del(String ids) {
        Assert.isNull(ids, "未选择不能删除");
        String[] ids_ = ids.split(",");
        if (ids_.length <= 0) {
            Assert.isNull("", "未选择不能删除");
        }

        dao.delByIds(User.class, ids_);
        //删除用户对应的角色
        dao.del(UserRole.class, "userid", ids_);
        return Base.suc("删除成功");
    }

    /**
     * 查询角色
     *
     * @param id 用户id
     * @author lyc
     * @date 2020年07月24日17:24:03
     */
    public List<RoleResult> getRoles(String id) {
        //所有角色
        List<RoleResult> roles = roleLogic.getList();
        //查询用户id已配置的角色
        List<UserRoleResult> list = dao.query("user.getHasRoles", id);
        Set<String> sans = new HashSet<>();
        for (UserRoleResult result : list) {
            sans.add(result.getRoleid());
        }
        for (RoleResult item : roles) {
            if (sans.contains(item.getId()))
                item.setUsed(Constant.YES);
        }
        return roles;
    }

    /**
     * @param form 表单提交对象
     * @Description: 检查对象数据
     * @author lyc
     * @version 1.0.0
     * @date 2019年12月20日17:46:46
     */
    private void CheckBean(UserForm form) {
        Assert.isNull(form, "对象不能为空");
        Assert.isNull(form.getAccount(), "账号不能为空");
        Assert.isNull(form.getUsername(), "昵称不能为空");
    }
}
